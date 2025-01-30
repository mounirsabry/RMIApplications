package io.tasks.impl;

import io.tasks.api.ClientAPI;
import io.tasks.fxml.MainPageController;
import io.tasks.managers.Director;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientAPIImpl extends UnicastRemoteObject 
        implements ClientAPI {
    private Director myDirector;
    private MainPageController mainPageController;
    
    public ClientAPIImpl() throws RemoteException {
        super();
    }
    
    public void setInitData(Director director,
            MainPageController mainPageController) {
        myDirector = director;
        this.mainPageController = mainPageController;
    }
    
    @Override
    public void receiveMessage(String message) throws RemoteException {
        if (message == null || message.isBlank()) {
            myDirector.displayError("Server sent invalid message.");
            return;
        }
        
        mainPageController.addLineToMessages(message);
    }

    @Override
    public void userJoined(String name) throws RemoteException {
        if (name == null || name.isBlank()) {
            myDirector.displayError("Server sent invalid username.");
            return;
        }
        
        mainPageController.addLineToUsersList(name + " Joined.");
    }

    @Override
    public void userLeft(String name) throws RemoteException {
        if (name == null || name.isBlank()) {
            myDirector.displayError("Server sent invalid username.");
            return;
        }
        
        mainPageController.addLineToUsersList(name + " Left.");
    }
    
    @Override
    public void userTimeout(String name) throws RemoteException {
        if (name == null || name.isBlank()) {
            myDirector.displayError("Server sent invalid username.");
            return;
        }
        
        mainPageController.addLineToUsersList(name + " Timeout.");
    }
}
