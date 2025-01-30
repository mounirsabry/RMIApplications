package io.tasks.dao;

import io.tasks.dbconnection.ConnectionManager;
import io.tasks.entities.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class EmployeeDao {
    public Boolean isExists(int employeeID) {
        try (Connection connection = ConnectionManager.getConnection();
                Statement statement = connection.createStatement();) {
            String query = "SELECT * FROM Employee WHERE employee_ID = " 
                    + employeeID + ";";
 
            ResultSet resultSet = statement.executeQuery(query);
            
            boolean isExists = resultSet.next();
            resultSet.close();
            
            return isExists;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public Boolean isExists(String employeeName) {
        try (Connection connection = ConnectionManager.getConnection();
                Statement statement = connection.createStatement();) {
            String query = "SELECT * FROM Employee WHERE name = " 
                    + "'" + employeeName + "'" + ";";
 
            ResultSet resultSet = statement.executeQuery(query);
            
            boolean isExists = resultSet.next();
            resultSet.close();
            
            return isExists;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Employee> getAllEmployees() {
        try (Connection connection = ConnectionManager.getConnection();
                Statement statement = connection.createStatement();) {
            String query = "SELECT * FROM Employee;";
            
            ResultSet resultSet = statement.executeQuery(query);
            List<Employee> allEmployees = new ArrayList<>();
            
            while (resultSet.next()) {
                allEmployees.add(new Employee(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getTimestamp(3)
                ));
            }
            resultSet.close();
            return allEmployees;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Integer addEmployee(Employee newEmployee) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO Employee VALUES(0, ?, DEFAULT);");
            insertStatement.setString(1, newEmployee.getName());
            int rowsAffected = insertStatement.executeUpdate();
            insertStatement.close();
            
            if (rowsAffected == 0) {
                return -1;
            }
            
            Statement queryStatement = connection.createStatement();
            String query = "SELECT MAX(employee_ID) FROM Employee;";
            ResultSet resultSet = queryStatement.executeQuery(query);
            if (resultSet.next() == false) {
                return null;
            }
            
            int newID = resultSet.getInt(1);
            resultSet.close();
            queryStatement.close();
            return newID;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Boolean updateEmployee(int employeeID, String newUserName) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                        "UPDATE Employee SET name = ? WHERE employee_ID = ?;");
            
            statement.setString(1, newUserName);
            statement.setInt(2, employeeID);
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected != 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Boolean deleteEmployee(int employeeID) {
        try (Connection connection = ConnectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM Employee WHERE employee_ID = ?;");
            
            statement.setInt(1, employeeID);
            int rowsAffected = statement.executeUpdate();
            statement.close();
            return rowsAffected != 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
}
