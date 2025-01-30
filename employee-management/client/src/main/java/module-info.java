module io.tasks {
    requires java.rmi;
    requires java.sql;
    
    requires javafx.controls;
    requires javafx.fxml;
    
    opens io.tasks.fxml to javafx.fxml;
    exports io.tasks;
}