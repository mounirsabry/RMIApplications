package io.tasks.refresh;

import io.tasks.api.ServerAPI;
import io.tasks.managers.Director;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartBeatSender {
    private final Director myDirector;
    private final ServerAPI serverAPIImpl; 
    private final String userName;
    private final ScheduledExecutorService senderExecutor;
    
    public HeartBeatSender(Director director, ServerAPI impl, String name) {
        myDirector = director;
        serverAPIImpl = impl;
        userName = name;
        senderExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        Runnable sender = () -> {
            sendHeartBeat();
        };
        
        senderExecutor.scheduleAtFixedRate(
            sender, 
            0,
            Delays.HEARTBEAT_SEND_DELAY,
            TimeUnit.SECONDS);
        
        // Prevents any more scheduling of the tasks.
        senderExecutor.shutdown();
    }
    
    public void shutDown() {
        senderExecutor.shutdownNow();
    }
    
    private void sendHeartBeat() {
        try {
            serverAPIImpl.sendHeartBeat(userName);
        } catch (RemoteException ex) {
            myDirector.displayError(ex.getMessage());
        }
    }
}
