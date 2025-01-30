package io.tasks.managers;

import io.tasks.api.ClientAPI;
import io.tasks.api.ServerAPI;
import io.tasks.impl.ClientAPIImpl;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServiceManager {
    private final Director myDirector;
    
    private final String SERVER_API_SERVICE_NAME = "BroadcastChatService";
    private static volatile boolean isConnected = false;
    private static ServerAPI serverAPIImpl = null;
    private static ClientAPI clientAPIImpl = null;
    
    ServiceManager(Director director) {
        myDirector = director;
    }
    
    boolean startService() {
        /*
        Step1: create and store object of the clientAPIImpl.
        Step2: get reigstry.
        Step3: look up for the impl class of the server.
        */
        try {
            clientAPIImpl = new ClientAPIImpl();
        } catch (RemoteException ex) {
            myDirector.serviceManagerError("""
                Could not create impl object to share with the server.
                contact the developer.""");
            System.err.println(ex);
            return false;
        }
        
        Registry reg;
        try {
            reg = LocateRegistry.getRegistry(1099);
        } catch (AccessException ex) {
            myDirector.serviceManagerError("Denied access to registry.");
            return false;
        } catch (RemoteException ex) {
            myDirector.serviceManagerError("Could not load the registry object.");
            return false;
        }
        
        try {
            serverAPIImpl = (ServerAPI) reg.lookup(SERVER_API_SERVICE_NAME);
        } catch (NotBoundException ex) {
            myDirector.serviceManagerError("Service is not bounded.");
            return false;
        } catch (RemoteException ex) {
            myDirector.serviceManagerError("Could not load the impl object.");
            return false;
        }
        
        isConnected = true;
        return true;
    }
    
    void stopService() {
        serverAPIImpl = null;
        clientAPIImpl = null;
        
        isConnected = false;
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public static ServerAPI getServerAPIImpl() {
        return serverAPIImpl;
    }   
    
    public static ClientAPI getClientAPIImpl() {
        return clientAPIImpl;
    }
}
