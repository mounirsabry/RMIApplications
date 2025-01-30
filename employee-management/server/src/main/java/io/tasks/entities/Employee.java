package io.tasks.entities;

import java.io.Serializable;
import java.sql.Timestamp;

public class Employee implements Serializable {
    private int employeeID;
    private String name;
    private Timestamp createdAt;
    
    public Employee() {
        employeeID = -1;
        name = "Not Specified";
        createdAt = null;
    }

    public Employee(int employeeID, String name, Timestamp createdAt) {
        this.employeeID = employeeID;
        this.name = name;
        this.createdAt = createdAt;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Employee.class.getName());
        builder.append('{');
        
        builder.append("employeeID=");
        builder.append(employeeID);
        
        builder.append(", name=");
        builder.append(name);
        
        builder.append(", createdAt=");
        builder.append(createdAt);
        
        builder.append('}');
        return builder.toString();
    }
}
