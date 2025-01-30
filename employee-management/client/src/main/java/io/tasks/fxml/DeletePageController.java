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

public class DeletePageController implements Initializable {
    private Director myDirector;
    
    @FXML
    private TextField IDField;
    
    private String defaultStatusString;
    private final String successOperationString = "Deletion was successful";
    private final String failedOperationString = "Operation failed";
    @FXML
    private Label statusLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        defaultStatusString = statusLabel.getText();
    }
    
    public void setDirector(Director director) {
        myDirector = director;
    }
    
    public void perform() { 
    }
    
    @FXML
    public void handleGoBack() {
        IDField.clear();
        statusLabel.setText(defaultStatusString);
        myDirector.goBackToMain();
    }
    
    @FXML
    public void handleDelete() {
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
        
        myDirector.connect();
        
        if (!myDirector.isConnected()) {
            return;
        }
        
        ClientAPI impl = ServiceManager.getClientAPI();
        
        try {
            Boolean result = impl.deleteEmployee(employeeID);
            if (result == null) {
                myDirector.displayError("Unknown error happened at the server.");
                statusLabel.setText(failedOperationString);
                return;
            }
            
            if (result == false) {
                myDirector.displayError("ID does not exist.");
                statusLabel.setText(failedOperationString);
                return;
            }
            
            statusLabel.setText(successOperationString);
        } catch (RemoteException ex) {
            statusLabel.setText(failedOperationString);
            myDirector.displayError(ex.getMessage());
        }
    }
}
