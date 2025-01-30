package io.tasks.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientAPI extends Remote {
    public void receiveMessage(String message) throws RemoteException;
    public void userJoined(String name) throws RemoteException;
    public void userLeft(String name) throws RemoteException;
    public void userTimeout(String name) throws RemoteException;
}
