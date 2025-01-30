package io.tasks.topcontrollers;   

import io.tasks.api.ClientAPI;
import io.tasks.dao.EmployeeDao;
import io.tasks.entities.Employee;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientAPIImpl extends UnicastRemoteObject
        implements ClientAPI {
    private final EmployeeDao employeeDao = new EmployeeDao();
    
    public ClientAPIImpl() throws RemoteException {
        super();
    }

    @Override
    public List<Employee> getAllEmployees() throws RemoteException {
        return employeeDao.getAllEmployees();
    }

    @Override
    public Integer addEmployee(Employee newEmployee) throws RemoteException {
        if (newEmployee == null || newEmployee.getName() == null
        || newEmployee.getName().isBlank()) {
            return -1;
        }
        Boolean isNameExists = employeeDao.isExists(newEmployee.getName());
        if (isNameExists == null) return null;
        if (isNameExists) return -1;
        
        return employeeDao.addEmployee(newEmployee);
    }

    @Override
    public Boolean updateEmployee(int employeeID, String newUserName) throws RemoteException {
        Boolean isIDExists = employeeDao.isExists(employeeID);
        if (isIDExists == null) return null;
        if (!isIDExists) return false;
        
        Boolean isNameExists = employeeDao.isExists(newUserName);
        if (isNameExists == null) return null;
        if (isNameExists) return false;
        
        return employeeDao.updateEmployee(employeeID, newUserName);
    }

    @Override
    public Boolean deleteEmployee(int employeeID) throws RemoteException {
        Boolean isExists = employeeDao.isExists(employeeID);
        if (isExists == null) return null;
        if (!isExists) return false;
        
        return employeeDao.deleteEmployee(employeeID);
    }
}
