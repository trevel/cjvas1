package ca.myseneca.rmi.server;

import java.util.ArrayList;

import ca.myseneca.model.DBAccessHelper;
import ca.myseneca.model.DBUtilities;
import ca.myseneca.model.Employee;

public class DBAccessImpl extends java.rmi.server.UnicastRemoteObject implements DBAccess {
	DBUtilities myUtil = null;
	
	public DBAccessImpl() throws java.rmi.RemoteException {
		super();
		try {
			myUtil= new DBUtilities("database.properties");
			DBAccessHelper.setConn(myUtil.getOCIConnection());
		} catch (Exception e) {
			System.out.println("Something went wrong: " + e);
			e.printStackTrace();
		}
	}
	
	public int getEmployeeID(String user, String password) throws java.rmi.RemoteException {
		return(DBAccessHelper.getEmployeeID(user, password));
	}
	
	public ArrayList<Employee> getAllEmployees() throws java.rmi.RemoteException {
		return(DBAccessHelper.getAllEmployees());
	}
	
	public ArrayList<Employee> getEmployeesByDepartmentID(int depid) throws java.rmi.RemoteException {
		return(DBAccessHelper.getEmployeesByDepartmentID(depid));
	}
	
	public Employee getEmployeeByID(int empid) throws java.rmi.RemoteException {
		return(DBAccessHelper.getEmployeeByID(empid));
	}
		
	public void addNewEmployee(Employee emp) throws java.rmi.RemoteException {
		DBAccessHelper.addNewEmployee(emp);
	}
	
	public int updateEmployee(Employee emp) throws java.rmi.RemoteException {
		return(DBAccessHelper.updateEmployee(emp));
	}
	
	public int deleteEmployeeByID(int empid) throws java.rmi.RemoteException {
		return(DBAccessHelper.deleteEmployeeByID(empid));
	}
	
	public boolean batchUpdate(String[] SQLs) throws java.rmi.RemoteException {
		return(DBAccessHelper.batchUpdate(SQLs));
	}
}
