package io.tasks.impl;

import io.tasks.ServerManager;
import io.tasks.api.ClientAPI;
import io.tasks.api.ServerAPI;
import io.tasks.classes.ServerCommand;
import io.tasks.managers.ClientManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerAPIImpl extends UnicastRemoteObject 
        implements ServerAPI {
    private final ClientManager clientManager = new ClientManager();
    private ServerManager serverManager = null;
    
    public ServerAPIImpl() throws RemoteException {
        super();
    }
    
    public void setServerManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }
    
    @Override
    public boolean sendHeartBeat(String name) throws RemoteException {
        return clientManager.sendHeartBeat(name);
    }
    
    @Override
    public void serverShutDown() throws RemoteException {
        if (serverManager == null) {
            throw new RemoteException("Server Remote Manager is offline.");
        }
        
        clientManager.shutDown();
        serverManager.setNextCommand(ServerCommand.SHUT_DOWN);
    }
    
    @Override
    public List<String> getAllMessages() throws RemoteException {
        return clientManager.getAllMessages();
    }
    
    @Override
    public List<String> getAllUsers() throws RemoteException {
        return clientManager.getAllUsers();
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        if (message == null || message.isBlank()) {
            throw new RemoteException("Message cannot be null or empty.");
        }
        clientManager.sendMessage(message);
    }

    @Override
    public void registerNewUser(String name, ClientAPI clientAPI) throws RemoteException {
        if (name == null || name.isBlank()) {
            throw new RemoteException("name cannot be null or empty.");
        }
        if (clientAPI == null) {
            throw new RemoteException("api object is null.");
        }
        
        if (clientManager.isNameExists(name)) {
            throw new RemoteException("""
                    A user with the same name is already online.
                    Pick up another name."""); 
        }
        
        clientManager.registerUser(name, clientAPI);
    }

    @Override
    public void removeRegisteredUser(String name) throws RemoteException {
        if (name == null || name.isBlank()) {
            throw new RemoteException("name cannot be null or empty.");
        }
        clientManager.removeUser(name);
    }
}
