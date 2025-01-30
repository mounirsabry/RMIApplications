package io.tasks;

import io.tasks.api.ServerAPI;
import io.tasks.impl.ServerAPIImpl;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServiceManager {
    private static final String SERVICE_NAME = "BroadcastChatService";
    private static ServerAPI serverAPI;
    
    static boolean startService() {
        /*
        Step1: get the registry object.
        Step2: create an object of the server (object of the impl class).
        Step3: publish the server object in the registry under a name.
        */
        
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(1099);
        } catch (AccessException ex) {
            System.err.println("Server denied access to registry.");
            return false;
        } catch (RemoteException ex) {
            System.err.println("Server could not create a registry.");
            return false;
        }
        
        try {
            serverAPI = new ServerAPIImpl();
        } catch (RemoteException ex) {
            System.err.println("Server could create an impl object.");
            return false;
        }
        
        try {
            reg.rebind(SERVICE_NAME, serverAPI);
        } catch (RemoteException ex) {
            System.err.println("Server could not publish the impl.");
            return false;
        }
        return true;
    }
    
    static boolean stopService() {
        Registry reg;
        try {
            reg = LocateRegistry.getRegistry(1099);
        } catch (AccessException ex) {
            System.err.println("Server denied access to registry.");
            return false;
        } catch (RemoteException ex) {
            System.err.println("Server could not create a registry.");
            return false;
        }
        
        try {
            reg.unbind(SERVICE_NAME);
        } catch (NotBoundException ex) {
            System.out.println("The service is already unbounded.");
        } catch (RemoteException ex) {
            System.err.println("Failed to unbind the service.");
            return false;
        }
        
        return true;
    }
    
    static ServerAPIImpl getServerAPIImpl() {
        return (ServerAPIImpl) serverAPI;
    }
}
