package io.tasks.fxml;

import io.tasks.api.ClientAPI;
import io.tasks.entities.Employee;
import io.tasks.managers.Director;
import io.tasks.managers.ServiceManager;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddPageController implements Initializable {
    private Director myDirector;
    
    @FXML
    private TextField nameField;
    
    private String defaultStatusString;
    private final String successStatusString = 
            "Employee added successfully with ID ";
    private final String failedStatusString = 
            "Operation failed.";
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
        nameField.clear();
        statusLabel.setText(defaultStatusString);
        myDirector.goBackToMain();
    }
    
    @FXML
    public void handleAdd() {
        String employeeName = nameField.getText();
        if (employeeName.isBlank()) {
            return;
        }
        
        myDirector.connect();
        
        if (!myDirector.isConnected()) {
            myDirector.displayError("You are not connected to the server.");
            return;
        }
        
        ClientAPI impl = ServiceManager.getClientAPI();
        
        Employee newEmployee = new Employee();
        newEmployee.setName(employeeName);
        
        try {
            Integer newID = impl.addEmployee(newEmployee);
            
            if (newID == null) {
                myDirector.displayError("An unknown error happened at the server.");
                statusLabel.setText(failedStatusString);
                return;
            }
            
            if (newID == -1) {
                myDirector.displayError("Name already exists.");
                statusLabel.setText(failedStatusString);
                return;
            }
            
            statusLabel.setText(successStatusString + newID);
        } catch (RemoteException ex) {
            statusLabel.setText(failedStatusString);
            myDirector.displayError(ex.getMessage());
        }
    }
}
