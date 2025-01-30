package io.tasks.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerAPI extends Remote {
    public boolean sendHeartBeat(String name) throws RemoteException;
    public void serverShutDown() throws RemoteException;
    public List<String> getAllMessages() throws RemoteException;
    public List<String> getAllUsers() throws RemoteException;
    public void sendMessage(String message) throws RemoteException;
    public void registerNewUser(String name, ClientAPI clientAPI) throws RemoteException;
    public void removeRegisteredUser(String name) throws RemoteException;
}

