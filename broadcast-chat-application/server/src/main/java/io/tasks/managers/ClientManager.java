package io.tasks.managers;

import io.tasks.api.ClientAPI;
import io.tasks.classes.Delays;
import io.tasks.classes.LoggedUser;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientManager {
    private final List<String> messagesList;
    private final List<LoggedUser> usersList;
    
    private final ScheduledExecutorService onlineChecker;
    
    public ClientManager() {
        messagesList = Collections.synchronizedList(new ArrayList<>());
        usersList = Collections.synchronizedList(new ArrayList<>());
        
        onlineChecker = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = () -> {
            checkList();
        };
        onlineChecker.scheduleAtFixedRate(runnable,
            0,
            Delays.TIMEOUT_USER_CHECK_DELAY,
            TimeUnit.SECONDS);
        
        onlineChecker.shutdown();
    }
    
    public void shutDown() {
        onlineChecker.shutdownNow();
    }   
    
    public boolean sendHeartBeat(String name) throws RemoteException {
        Iterator<LoggedUser> iterator = usersList.iterator();
        LoggedUser user;
        while (iterator.hasNext()) {
            user = iterator.next();
            if (user.getName().equals(name)) {
                user.refreshLastHeartBeat();
                return true;
            }
        }
        
        return false;
    }
    
    public List<String> getAllMessages() {
        return messagesList;
    }
    
    public List<String> getAllUsers() {
        List<String> names = new ArrayList();
        
        Iterator<LoggedUser> iterator = usersList.iterator();
        LoggedUser user;
        while (iterator.hasNext()) {
            user = iterator.next();
            names.add(user.getName());
        }
        return names;
    }
    
    public void sendMessage(String message) {
        Iterator<LoggedUser> iterator = usersList.iterator();
        LoggedUser user;
        while (iterator.hasNext()) {
            user = iterator.next();
            try {
                user.getRegisteredObject().receiveMessage(message);
            } catch (RemoteException ex) {
                System.err.println("The user with name: " + user.getName()
                        + " object's has thrown an exception.");
                System.err.println(ex);
                iterator.remove();
            }
        }
        messagesList.add(message);
    }
    
    public boolean isNameExists(String name) {
        Iterator<LoggedUser> iterator = usersList.iterator();
        LoggedUser user;
        while (iterator.hasNext()) {
            user = iterator.next();
            if (user.getName().equals(name)) {
                return true;
            }
        }
        
        return false;
    }
    
    public void registerUser(String name, ClientAPI object) {
        Iterator<LoggedUser> iterator = usersList.iterator();
        LoggedUser user;
        while (iterator.hasNext()) {
            user = iterator.next();
            try {
                user.getRegisteredObject().userJoined(name);
            } catch (RemoteException ex) {
                System.err.println("The user with name: " + user.getName()
                        + " object's has thrown an exception.");
                System.err.println(ex);
                iterator.remove();
            }
        }
        usersList.add(new LoggedUser(name, object));
    }
    
    public void removeUser(String name) {
        Iterator<LoggedUser> iterator = usersList.iterator();
        LoggedUser user;
        while (iterator.hasNext()) {
            user = iterator.next();
            try {
                user.getRegisteredObject().userLeft(name);
            } catch (RemoteException ex) {
                System.err.println("The user with name: " + user.getName()
                        + " object's has thrown an exception.");
                System.err.println(ex);
                iterator.remove();
            }
        }
        
        iterator = usersList.iterator();
        while (iterator.hasNext()) {
            user = iterator.next();
            if (user.getName().equals(name)) {
                iterator.remove();
                break;
            }
        }
    }
    
    private void userTimeout(String name) {
        Iterator<LoggedUser> iterator = usersList.iterator();
        LoggedUser user;
        while (iterator.hasNext()) {
            user = iterator.next();
            try {
                user.getRegisteredObject().userTimeout(name);
            } catch (RemoteException ex) {
                System.err.println("The user with name: " + user.getName()
                        + " object's has thrown an exception.");
                System.err.println(ex);
                iterator.remove();
            }
        }
        
        for (int i = 0; i < usersList.size(); i++) {
            if (usersList.get(i).getName().equals(name)) {
                usersList.remove(i);
                break;
            }
        }
    }
    
    private void checkList() {
        Iterator<LoggedUser> iterator = usersList.iterator();
        Timestamp now = Timestamp.from(Instant.now());
        
        LoggedUser user;
        while (iterator.hasNext()) {
            user = iterator.next();
            long difference = now.getTime() -
                    user.getLastHeartBeat().getTime();

            difference = difference / 1000;
            if (difference >= Delays.USER_TIMEOUT_DELAY) {
                userTimeout(user.getName());
            }
        }
    }
}
