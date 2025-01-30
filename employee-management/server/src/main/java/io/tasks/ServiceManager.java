package io.tasks;

import io.tasks.topcontrollers.ClientAPIImpl;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServiceManager {
    public static boolean startService() {
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
        
        ClientAPIImpl impl;
        try {
            impl = new ClientAPIImpl();
        } catch (RemoteException ex) {
            System.err.println("Server could create an impl object.");
            return false;
        }
        
        try {
            reg.rebind("EmployeeService", impl);
        } catch (RemoteException ex) {
            System.err.println("Server could not publish the impl.");
            return false;
        }
        return true;
    }
}
