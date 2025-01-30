module io.tasks {
    requires java.rmi;
    
    requires javafx.controls;
    requires javafx.fxml;
    
    opens io.tasks.fxml to javafx.fxml;
    opens io.tasks.api to java.rmi;
    exports io.tasks;
}