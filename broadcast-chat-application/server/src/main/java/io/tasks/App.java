package io.tasks;

import io.tasks.classes.Delays;
import io.tasks.classes.ServerCommand;

public class App {
    @SuppressWarnings("SleepWhileInLoop")
    public static void main(String[] args) {
        System.out.println("Server is starting.");

        boolean isStarted = ServiceManager.startService();
        if (!isStarted) {
            System.err.println("Could not publish the service.");
            System.err.println("Server will exit.");
            return;
        }
        
        ServerManager myManager = new ServerManager();
        ServiceManager.getServerAPIImpl().setServerManager(
                myManager);
        
        System.out.println("Server is up and running.");
        
        while (true) {
            ServerCommand nextCommand = myManager.getAndSetNextCommand(
                    ServerCommand.WAIT);
            
            if (nextCommand == ServerCommand.SHUT_DOWN) {
                ServiceManager.stopService();
                break;
            }
            
            try {
                Thread.sleep(Delays.SERVER_CHECK_NEXT_COMMAND_DELAY);
            } catch (InterruptedException ex) {
                System.out.println("Something interrupted the main thread.");
                return;
            }
        }
    } 
}
