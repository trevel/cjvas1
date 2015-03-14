/*****************************************************************************************
 *  Student Names: Laurie Shields (034448142)
 *                 Mark Lindan (063336143)
 *  CJV805 - HRManagement.java
 * **************************************************************************************/
package ca.myseneca.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

import ca.myseneca.model.DBAccessHelper;
import ca.myseneca.model.DBUtilities;
import ca.myseneca.model.Employee;

public class HRManagement {
	public static void main(String[] args) {
		String username;
		String password;
		String input;
		Scanner keyboard = new Scanner(System.in);

		try {
			System.out.println("Running HRManagement.............................");
			System.out.println("Enter a username:");
			username = keyboard.nextLine();
			System.out.println("Enter a password: ");
			password = keyboard.nextLine();
			int result = DBAccessHelper.getEmployeeID(username, password);
			if (result > 0) {
				System.out.println("Here is your employee information:");
				Employee emp = DBAccessHelper.getEmployeeByID(result);
				if (emp != null) {
					System.out.println(emp.toString());
				}
				System.out.println("*****************************************");
				
				System.out.println("Press enter key to proceed");
				input = keyboard.nextLine();
				
				System.out.println("Adding a new employee....................");
				Employee emp3 = CreateNewEmployee();
				DBAccessHelper.addNewEmployee(emp3);
				System.out.println("The following employee was added:");
				Employee emp4 = DBAccessHelper.getEmployeeByID(emp3.getEmployee_id());
				if (emp4 != null) {
					System.out.println(emp4.toString());
				} else {
					System.out.println("Failed to find the new employee");
				}
				System.out.println("*****************************************");		
				
				System.out.println("Press enter key to proceed");
				input = keyboard.nextLine();
				
				System.out.println("Here is a list of all employees: ");
				ArrayList<Employee> lst2 = DBAccessHelper.getAllEmployees();
				for (int i = 0; i < lst2.size(); i++) {
					System.out.println(lst2.get(i).toString());
				}
				System.out.println("****************************************");
				
				System.out.println("Press enter key to proceed");
				input = keyboard.nextLine();
				
				System.out.println("Enter a specific Department ID to view employees for: ");
				input = keyboard.nextLine();
				int dept = Integer.parseInt(input);
				System.out.println("Here are all employess in department: " + dept);
				ArrayList<Employee> lst1 = DBAccessHelper.getEmployeesByDepartmentID(dept);
				for (int i = 0; i < lst1.size(); i++) {
					System.out.println(lst1.get(i).toString());
				}
				System.out.println("*******************************************************");
				
				System.out.println("Press enter key to proceed");
				input = keyboard.nextLine();
				
				System.out.println("Enter an employee ID to delete: ");
				input = keyboard.nextLine();
				int empid = Integer.parseInt(input);
				if (DBAccessHelper.deleteEmployeeByID(empid) > 0) {
					System.out.println("Deleted employee with id: " + empid);
				} else {
					System.out.println("Failed to delete employee with id: " + empid);
				}
				System.out.println("*******************************************************");
				
				System.out.println("Press enter key to proceed");
				input = keyboard.nextLine();
				
				System.out.println("Enter an employee ID to update: ");
				input = keyboard.nextLine();
				empid = Integer.parseInt(input);
				Employee emp2 = DBAccessHelper.getEmployeeByID(empid);
				if (emp2 != null) {
					if (emp2.getDept_id() != 30) {
						System.out.println("Setting the department ID to 30 for employee ID: " + empid);
						emp2.setDept_id(30);
					} else {
						System.out.println("Setting the department ID to 90 for employee ID: " + empid);
						emp2.setDept_id(90);
					}
					emp2.setFirst_name("New");
					emp2.setLast_name("Name");
					emp2.setSalary(new BigDecimal(12345));
					emp2.setComm_pct(new BigDecimal(0.15));
					
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
				
				System.out.println("*******************************************************");
				
				System.out.println("Press enter key to proceed");
				input = keyboard.nextLine();
				
				String SQLs[] = {
					"INSERT INTO employees VALUES (501,'FName1','LName1','email1','phone1',SYSDATE,'AC_MGR',null,null,null,null)",
				    "INSERT INTO employees VALUES (502,'FName2','LName2','email2','phone2',SYSDATE,'HR_REP',2222,null,205,40)",
				    "INSERT INTO employees VALUES (503,'FName3','LName3','email3','phone3',SYSDATE,'PR_REP',3333,0.1,204,30)",
					"DELETE FROM employees WHERE employee_id = 501",
					"DELETE FROM employees WHERE employee_id = 502",
					"DELETE FROM employees WHERE employee_id = 503"};
				System.out.println("Running the batch command ....................");
				if (DBAccessHelper.batchUpdate(SQLs) == true) {
					System.out.println("The batch command was successful!");				
				} else {
					System.out.println("The batch command failed");	
				}
			} else {
				System.out.println("Unauthorized user -- terminating program");
			}
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
		emp.setComm_pct(null);
		emp.setManager_id(205);
		emp.setDept_id(110);
		return emp;
	}

}
