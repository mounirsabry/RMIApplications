package io.tasks.managers;

import io.tasks.fxml.AddPageController;
import io.tasks.fxml.DeletePageController;
import io.tasks.fxml.MainPageController;
import io.tasks.fxml.UpdatePageController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Director {
    private final Stage myStage;
    
    private boolean isConnected = false;
    private final ServiceManager serviceManager = 
            new ServiceManager(this);
    
    private final Parent mainPageParent;
    private final MainPageController mainPageController;
    private Scene mainPageScene;
    
    private final Parent addPageParent;
    private final AddPageController addPageController;
    private Scene addPageScene;
    
    private final Parent updatePageParent;
    private final UpdatePageController updatePageController;
    private Scene updatePageScene;
    
    private final Parent deletePageParent;
    private final DeletePageController deletePageController;
    private Scene deletePageScene;
    
    public Director(Stage stage,
            Parent mainPageParent, MainPageController mainPageController,
            Parent addPageParent, AddPageController addPageController,
            Parent updatePageParent, UpdatePageController updatePageController,
            Parent deletePageParent, DeletePageController deletePageController) {
        myStage = stage;
        
        this.mainPageParent = mainPageParent;
        this.mainPageController = mainPageController;
        
        this.addPageParent = addPageParent;
        this.addPageController = addPageController;
        
        this.updatePageParent = updatePageParent;
        this.updatePageController = updatePageController;
        
        this.deletePageParent = deletePageParent;
        this.deletePageController = deletePageController;
    }
    
    public void startWorking() {
        mainPageScene = new Scene(mainPageParent);
        mainPageController.setDirector(this);
        
        addPageScene = new Scene(addPageParent);
        addPageController.setDirector(this);
        
        updatePageScene = new Scene(updatePageParent);
        updatePageController.setDirector(this);
        
        deletePageScene = new Scene(deletePageParent);
        deletePageController.setDirector(this);
        
        myStage.setOnCloseRequest((e) -> {
            isConnected = serviceManager.stopService();
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
        } catch (Exception ex) {
            System.err.println("Director could not display error alert "
                    + "with message: " + message);
        }
    }
    
    // Service Manager's Director controlled functions.
    public boolean isConnected() {
        return isConnected;
    }
    
        public void connect() {
        if (isConnected) {
            return;
        }
        
        isConnected = serviceManager.startService();
    }
    
    public void disconnect() {
        isConnected = serviceManager.stopService();
    }
    
    // Used to indicate an error happened in the connection.
    public void setDisconnected() {
        isConnected = false;
    }
    
    // Managers section.
    void serviceManagerError(String message) {
        // Approach 1, display exception to error.
        displayError(message);
        
        // Approach 2, display one message in all cases.
        //displayError("Could not connect to the server.");
    }
    
    // Transitions.
    public void addEmployee() {
        addPageController.perform();
        myStage.setScene(addPageScene);
    }
    
    public void updateEmployee() {
        updatePageController.perform();
        myStage.setScene(updatePageScene);
    }
    
    public void deleteEmployee() {
        deletePageController.perform();
        myStage.setScene(deletePageScene);
    }
    
    public void goBackToMain() {
        mainPageController.perform();
        myStage.setScene(mainPageScene);
    }
}
