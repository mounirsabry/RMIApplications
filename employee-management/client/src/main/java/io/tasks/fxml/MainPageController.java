package io.tasks.fxml;

import io.tasks.api.ClientAPI;
import io.tasks.entities.Employee;
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
import javafx.scene.control.ListView;

public class MainPageController implements Initializable {
    private Director myDirector;
    
    ObservableList<String> employeesList;
    @FXML
    ListView employeesListView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        employeesList = FXCollections.<String>observableArrayList();
        employeesListView.setItems(employeesList);
    }
    
    public void setDirector(Director director) {
        myDirector = director;
    }
    
    public void perform() {
        myDirector.connect();
        
        if (!myDirector.isConnected()) {
            myDirector.displayError("You are not connected to the server.");
            return;
        }
        
        ClientAPI impl = ServiceManager.getClientAPI();
        
        List<Employee> employees;
        try {
            employees = impl.getAllEmployees();
        } catch (RemoteException ex) {
            myDirector.displayError(ex.getMessage());
            return;
        }
        
        truncateEmployeesList();
        fillEmployeesList(employees);
    }
    
    private void truncateEmployeesList() {
        Platform.runLater(() -> {
            employeesList.removeIf((p) -> true);
        });
    }
    
    private void fillEmployeesList(List<Employee> list) {
        Platform.runLater(() -> {
            StringBuilder builder = new StringBuilder();
            
            for (Employee e : list) {
                builder.setLength(0);
                
                builder.append(e.getEmployeeID());
                builder.append(" | ");
                
                builder.append(e.getName());
                builder.append(" | ");
                
                builder.append(e.getCreatedAt());
                
                employeesList.add(builder.toString());
            }
        });
    }
    
    @FXML
    public void handleAdd() {
        myDirector.addEmployee();
    }
    
    @FXML
    public void handleUpdate() {
        myDirector.updateEmployee();
    }
    
    @FXML
    public void handleDelete() {
        myDirector.deleteEmployee();
    }
}
