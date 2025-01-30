package io.tasks.fxml;

import io.tasks.api.ClientAPI;
import io.tasks.managers.Director;
import io.tasks.managers.ServiceManager;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UpdatePageController implements Initializable {
    private Director myDirector;
    
    @FXML
    private TextField IDField;
    
    @FXML
    private TextField newNameField;
    
    private String defaultMessageString;
    private final String successfulOperationString = "Update was done successfully";
    private final String failedOperationString = "Operation failed";
    
    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        defaultMessageString = statusLabel.getText();
    }
   
    public void setDirector(Director director) {
        myDirector = director;
    }
    
    public void perform() {  
    }
    
    @FXML
    public void handleGoBack() {
        IDField.clear();
        newNameField.clear();
        statusLabel.setText(defaultMessageString);
        myDirector.goBackToMain();
    }
    
    @FXML
    public void handleUpdate() {
        String employeeIDStr = IDField.getText();
        if (employeeIDStr.isBlank()) {
            return;
        }
        
        int employeeID;
        try {
            employeeID = Integer.parseInt(employeeIDStr);
        } catch (NumberFormatException ex) {
            myDirector.displayError("ID must be a whole number.");
            return;
        }
        
        String newName = newNameField.getText();
        if (newName.isBlank()) {
            return;
        }
        
        myDirector.connect();
        
        if (!myDirector.isConnected()) {
            return;
        }
        
        ClientAPI impl = ServiceManager.getClientAPI();
        
        try {
            Boolean result = impl.updateEmployee(employeeID, newName);
            if (result == null) {
                myDirector.displayError("An unknown error happened at the server.");
                statusLabel.setText(failedOperationString);
                return;
            }
            
            if (result == false) {
                myDirector.displayError("Eiter ID does not exist"
                        + ", or name already exists.");
                statusLabel.setText(failedOperationString);
                return;
            }
            
            statusLabel.setText(successfulOperationString);
        } catch (RemoteException ex) {
            statusLabel.setText(failedOperationString);
            myDirector.displayError(ex.getMessage());
        }
    }
    
}
