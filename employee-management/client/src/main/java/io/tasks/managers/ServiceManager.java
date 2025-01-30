package io.tasks.managers;

import io.tasks.api.ClientAPI;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServiceManager {
    private final Director myDirector;
    
    private final String CLIENT_NAME_SERVICE_NAME = "EmployeeService";
    private static ClientAPI clientAPIImpl = null;
    
    ServiceManager(Director director) {
        myDirector = director;
    }
    
    boolean startService() {
        /*
        Step1: get reigstry.
        Step2: look up for the impl class of the server.
        */
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
            clientAPIImpl = (ClientAPI) reg.lookup(CLIENT_NAME_SERVICE_NAME);
        } catch (NotBoundException ex) {
            myDirector.serviceManagerError("Service is not bounded.");
            return false;
        } catch (RemoteException ex) {
            myDirector.serviceManagerError("Could not load the impl object.");
            return false;
        }
        
        return true;
    }
    
    boolean stopService() {
        clientAPIImpl = null;
        return true;
    }
    
    public static ClientAPI getClientAPI() {
        return clientAPIImpl;
    }   
}
