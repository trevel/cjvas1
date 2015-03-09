package ca.myseneca.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

import ca.myseneca.model.DBAccessHelper;
import ca.myseneca.model.DBUtilities;
import ca.myseneca.model.Employee;

public class HRManagement {
	public static void main(String[] args) {
		DBUtilities myUtil;
		String username;
		String password;
		String input;
		Scanner keyboard = new Scanner(System.in);

		try {
			myUtil = new DBUtilities("database.properties");
			DBAccessHelper.setConn(myUtil.getOCIConnection());
			System.out.println("Enter a username:");
			username = keyboard.nextLine();
			System.out.println("Enter a password: ");
			password = keyboard.nextLine();
			int result = DBAccessHelper.getEmployeeID(username, password);
			if (result > 0) {
				Employee emp = DBAccessHelper.getEmployeeByID(result);
				if (emp != null) {
					System.out.println(emp.toString());
				}
				System.out.println("All employess: ");
				ArrayList<Employee> lst2 = DBAccessHelper.getAllEmployees();
				for (int i = 0; i < lst2.size(); i++) {
					System.out.println(lst2.get(i).toString());
				}
				System.out.println("*******************************************************");
				System.out.println("Enter a Department ID: ");
				input = keyboard.nextLine();
				int dept = Integer.parseInt(input);
				System.out.println("All employess in department: " + dept);
				ArrayList<Employee> lst1 = DBAccessHelper.getEmployeesByDepartmentID(dept);
				for (int i = 0; i < lst1.size(); i++) {
					System.out.println(lst1.get(i).toString());
				}
				System.out.println("*******************************************************");
				System.out.println("Adding a new employee");
				Employee emp3 = CreateNewEmployee();
				DBAccessHelper.addNewEmployee(emp3);
				Employee emp4 = DBAccessHelper.getEmployeeByID(emp3.getEmployee_id());
				if (emp4 != null) {
					System.out.println(emp4.toString());
				}
				System.out.println("*******************************************************");
				System.out.println("Enter an employee ID to delete: ");
				input = keyboard.nextLine();
				int empid = Integer.parseInt(input);
				if (DBAccessHelper.deleteEmployeeByID(empid) > 0) {
					System.out.println("Deleted employee with id: " + empid);
				} else {
					System.out.println("Failed to delete employee with id: " + empid);
				}
				System.out.println("*******************************************************");
				System.out.println("Enter an employee ID to update: ");
				input = keyboard.nextLine();
				empid = Integer.parseInt(input);
				Employee emp2 = DBAccessHelper.getEmployeeByID(empid);
				if (emp2 != null) {
					if (DBAccessHelper.updateEmployee(emp2) > 0) {
						emp2 = DBAccessHelper.getEmployeeByID(empid);
						if (emp2 != null) {
							System.out.println(emp2.toString());
						}
					} else {
						System.out.println("Failed to update employee with id: " + empid);				
					}
				} else {
					System.out.println("Failed to find employee with id: " + empid);			
				}

			} else {
				System.out.println("Unauthorized user -- terminating program");
			}

		} catch (FileNotFoundException e) {
			System.err.println("Could not find file: database.properties");
			return;
		} catch (IOException e) {
			System.err.println("Error parsing file: database.properties");
			return;
		} catch (Exception e) {
			System.err.println("Some other issue!");
			e.printStackTrace();
			return;
		} finally {
			keyboard.close();
		}

	}
	
	private static Employee CreateNewEmployee() {
		Employee emp = new Employee();
		java.util.Date javaDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
		emp.setEmployee_id(500);
		emp.setFirst_name("Laurie");
		emp.setLast_name("Shields");
		emp.setEmail("llshields");
		emp.setPhone_number("905-123-4567");
		emp.setHire_date(sqlDate);
		emp.setJob_id("IT_PROG");
		emp.setSalary(new BigDecimal(11111));
		emp.setComm_pct(new BigDecimal(0));
		emp.setManager_id(205);
		emp.setDept_id(110);
		return emp;
	}

}
