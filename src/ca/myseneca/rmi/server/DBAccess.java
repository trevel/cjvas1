package ca.myseneca.rmi.server;

import java.util.ArrayList;

import ca.myseneca.model.Employee;

public interface DBAccess extends java.rmi.Remote {
	public int getEmployeeID(String user, String password) throws java.rmi.RemoteException; 
	public ArrayList<Employee> getAllEmployees() throws java.rmi.RemoteException;
	public ArrayList<Employee> getEmployeesByDepartmentID(int depid) throws java.rmi.RemoteException;
	public Employee getEmployeeByID(int empid) throws java.rmi.RemoteException;
	public void addNewEmployee(Employee emp) throws java.rmi.RemoteException;
	public int updateEmployee(Employee emp) throws java.rmi.RemoteException;
	public int deleteEmployeeByID(int empid) throws java.rmi.RemoteException;
	public boolean batchUpdate(String[] SQLs) throws java.rmi.RemoteException;
}
