package io.tasks;

import io.tasks.dbconnection.ConnectionManager;

public class App {
    @SuppressWarnings({"CallToPrintStackTrace", "SleepWhileInLoop"})
    public static void main( String[] args ) {
        boolean isOkay = ConnectionManager.initDeviceManager();
        System.out.println();
        if (!isOkay) {
            System.err.println("Server could not connect to db.");
            System.err.println("Server will shut down.");
            return;
        }
        
        boolean isServiceStarted = ServiceManager.startService();
        if (!isServiceStarted) {
            System.out.println();
            System.err.println("Server could register/start the service for the clients.");
            System.err.println("Server will shut down.");
            return;
        }
        
        System.out.println("Server is up and running.");
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.err.println("Something interrupted the server thread.");
                ex.printStackTrace();
                break;
            }
        }
    }
}
