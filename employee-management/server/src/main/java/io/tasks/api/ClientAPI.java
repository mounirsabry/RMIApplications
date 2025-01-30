package io.tasks.api;

import io.tasks.entities.Employee;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientAPI extends Remote {
    public List<Employee> getAllEmployees() throws RemoteException;
    public Integer addEmployee(Employee newEmployee) throws RemoteException;
    public Boolean updateEmployee(int employeeID, String newUserName) throws RemoteException;
    public Boolean deleteEmployee(int employeeID) throws RemoteException;
}
