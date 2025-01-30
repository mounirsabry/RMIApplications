package io.tasks.managers;

import io.tasks.fxml.MainPageController;
import io.tasks.impl.ClientAPIImpl;
import java.rmi.RemoteException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import io.tasks.api.ServerAPI;
import io.tasks.refresh.HeartBeatSender;

public class Director {
    private final Stage myStage;
    
    private final ServiceManager serviceManager = 
            new ServiceManager(this);
    private HeartBeatSender sender = null;
    private String connectedName = null;
    
    private final Parent mainPageParent;
    private final Image connectedDotImage;
    private final MainPageController mainPageController;
    private Scene mainPageScene;
    
    public Director(Stage stage,
            Parent mainPageParent,
            Image connectedDotImage,
            MainPageController mainPageController) {
        myStage = stage;
        
        this.mainPageParent = mainPageParent;
        this.connectedDotImage = connectedDotImage;
        this.mainPageController = mainPageController;
    }
    
    public void startWorking() {
        mainPageScene = new Scene(mainPageParent);
        mainPageController.setConnectedImage(connectedDotImage);
        mainPageController.setDirector(this);
        
        myStage.setOnCloseRequest((e) -> {
            disconnect();
        });
        
        mainPageController.perform();
        myStage.setScene(mainPageScene);
        myStage.show();
    }
    
    public void displayError(String message) {
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(message);
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Director could not display error "
                    + "alert with message: " + message);
        }
    }

    // Service Manager's Director controlled functions.
    public boolean isConnected() {
        return serviceManager.isConnected();
    }
    
    public boolean connect(String userName) {
        if (serviceManager.isConnected()) {
            displayError("Director says you are already connected.");
            return false;
        }
        
        boolean isSuccessful = serviceManager.startService();
        if (!isSuccessful) {
            displayError("Failed to connect to server.");
            return false;
        }
        
        ClientAPIImpl clientImpl = (ClientAPIImpl) ServiceManager.getClientAPIImpl();
        clientImpl.setInitData(this, mainPageController);
        
        ServerAPI serverAPI = ServiceManager.getServerAPIImpl();
        try {
            serverAPI.registerNewUser(userName, clientImpl);
        } catch (RemoteException ex) {
            displayError(ex.getMessage());
            disconnect();
            return false;
        }
        
        sender = new HeartBeatSender(
                this, serverAPI, userName);
        sender.start();
        connectedName = userName;
        return true;
    }
    
    public void disconnect() {
        if (sender != null) {
            sender.shutDown();
        }
        
        ServerAPI serverAPI;
        try {
            serverAPI = ServiceManager.getServerAPIImpl();
            if (serverAPI != null) {
                serverAPI.removeRegisteredUser(connectedName);
            }
        } catch (RemoteException ex) {
            System.err.println(ex);
            displayError(
                "Could not remove registration from server properly.");
        }
        serviceManager.stopService();
        connectedName = null;
    }
    
    // Managers section.
    void serviceManagerError(String message) {
        // Approach 1, display exception to error.
        displayError(message);
        
        // Approach 2, display one message in all cases.
        //displayError("Could not connect to the server.");
    }
}
