package io.tasks.fxml;

import io.tasks.api.ServerAPI;
import io.tasks.managers.Director;
import io.tasks.managers.ServiceManager;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class MainPageController implements Initializable {
    private Director myDirector;
    
    private Image disconnectedImage;
    private Image connectedImage;

    private String loggedUserName = null;
    @FXML
    private TextField userNameField;
    
    @FXML
    private Button connectButton;
    
    @FXML
    private ImageView conIndicatorImageView;

    @FXML
    private TextField messageTextField;
    
    @FXML
    private Button broadcastButton;
    
    private ObservableList<String> messagesList;
    @FXML
    private ListView messagesListView;
    
    private ObservableList<String> usersList;
    @FXML
    private ListView usersListView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disconnectedImage = conIndicatorImageView.getImage();

        broadcastButton.setDisable(true);

        messagesList = FXCollections.<String>observableArrayList();
        messagesListView.setItems(messagesList);
        
        usersList = FXCollections.<String>observableArrayList();
        usersListView.setItems(usersList);
        
        messageTextField.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleBroadcast();
            }
        });
    }
    
    public void setConnectedImage(Image greenDot) {
        this.connectedImage = greenDot;
    }
    
    public void setDirector(Director director) {
        myDirector = director;
    }
    
    public void perform() {
        // Placeholder for any action that will
        // be executed when switching to this scene.
    }
    
    private void setConnectedInterface() {
        Platform.runLater(() -> {
            conIndicatorImageView.setImage(connectedImage);
            connectButton.setText("Disconnect");
            broadcastButton.setDisable(false);
        });
    }
    
    private void setDisconnectedInterface() {
        Platform.runLater(() -> {
            messagesList.clear();
            usersList.clear();

            conIndicatorImageView.setImage(disconnectedImage);
            connectButton.setText("Connect");
            broadcastButton.setDisable(true);
        });
        loggedUserName = null;
    }
    
    // Functions that will be called from the API.
    public void addLineToMessages(String line) {
        Platform.runLater(() -> {
            messagesList.add(line);
        });
    }
    
    public void addLineToUsersList(String line) {
        Platform.runLater(() -> {
            usersList.add(line);
        });
    }
    
    private void callOnConnectionError(String message) {
        myDirector.displayError(message);
        myDirector.disconnect();
        setDisconnectedInterface();
    }
   
    private void connect(String userName) {
        if (myDirector.isConnected()) {
            myDirector.displayError("You are already connected.");
            return;
        }
        
        myDirector.connect(userName);
        
        if (!myDirector.isConnected()) {
            myDirector.displayError("Could not connect to server.");
            myDirector.disconnect();
            setDisconnectedInterface();
            return;
        }
        
        ServerAPI serverAPI = ServiceManager.getServerAPIImpl();
        if (serverAPI == null) {
            callOnConnectionError("Server API was null in the "
                    + "connection function.");
            return;
        }
        
        List<String> oldMessages;
        List<String> onlineUsers;
        try {
            oldMessages = serverAPI.getAllMessages();
            onlineUsers = serverAPI.getAllUsers();
        } catch (RemoteException ex) {
            System.err.println(ex);
            callOnConnectionError(
                    "Could not load the lists from the server.");
            return;
        }
        
        if (oldMessages == null) {
            callOnConnectionError("Messages list was null.");
            return;
        }
        
        if (onlineUsers == null) {
            callOnConnectionError("Users list was null.");
            return;
        }
        
        Platform.runLater(() -> {
           for (String message : oldMessages) {
                messagesList.add(message);
           }
        });
        
        Platform.runLater(() -> {
            for (String user : onlineUsers) {
                usersList.add(user);
            }
        });
        
        loggedUserName = userName;
        setConnectedInterface();
    }
    
    private void disconnect() {    
        if (!myDirector.isConnected()) {
            myDirector.displayError("You are already disconnected.");
            return;
        }
        
        ServerAPI serverAPI = ServiceManager.getServerAPIImpl();
        if (serverAPI == null) {
            callOnConnectionError(
                    "Server API was null in the disconnect function.");
            return;
        }

        myDirector.disconnect();
        setDisconnectedInterface();
    }
    
    @FXML
    public void handleConnect() {    
        String userName = userNameField.getText();
        if (userName.isBlank()) {
            myDirector.displayError("User name cannot be empty.");
            return;
        } 
        userName = userName.trim();
        
        String buttonAction = connectButton.getText();
        switch (buttonAction) {
            case "Connect" -> connect(userName);
            case "Disconnect" -> disconnect();
            default -> myDirector.displayError("Connect button action is invalid.");
        }
    }
    
    @FXML
    public void handleBroadcast() {
        String message = messageTextField.getText();
        if (message.isBlank()) {
            return;
        }
        
        ServerAPI serverAPI = ServiceManager.getServerAPIImpl();
        if (serverAPI == null) {
            callOnConnectionError(
                    "Server API was null in the broadcast function.");
            return;
        }
        
        try {
            serverAPI.sendMessage(message);
        } catch (RemoteException ex) {
            System.err.println(ex);
            callOnConnectionError(
                    "Failed to send message to server.");
            return;
        }

        messageTextField.setText("");
    }
}
