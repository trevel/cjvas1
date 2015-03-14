/*****************************************************************************************
 *  Student Names: Laurie Shields (034448142)
 *                 Mark Lindan (063336143)
 *  CJV805 - Employee.java
 *  		 Java Bean Class for the Employee records in the employees table
 * **************************************************************************************/
package ca.myseneca.model;

import java.math.BigDecimal;

public class Employee implements java.io.Serializable {
	// Properties
	int employee_id;
	String last_name;
	String first_name;
	String email;
	String phone_number;
	java.sql.Date hire_date;
	String job_id;
	BigDecimal salary = null;
	BigDecimal comm_pct = null;
	int manager_id;
	int dept_id;
	
	public Employee() {
		super();
	}
	
	/**
	 * @return the employee_id
	 */
	public int getEmployee_id() {
		return employee_id;
	}
	/**
	 * @param employee_id the employee_id to set
	 */
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}
	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}
	/**
	 * @param last_name the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}
	/**
	 * @param first_name the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the phone_number
	 */
	public String getPhone_number() {
		return phone_number;
	}
	/**
	 * @param phone_number the phone_number to set
	 */
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	/**
	 * @return the hire_date
	 */
	public java.sql.Date getHire_date() {
		return hire_date;
	}
	/**
	 * @param hire_date the hire_date to set
	 */
	public void setHire_date(java.sql.Date hire_date) {
		this.hire_date = hire_date;
	}
	/**
	 * @return the job_id
	 */
	public String getJob_id() {
		return job_id;
	}
	/**
	 * @param job_id the job_id to set
	 */
	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}
	/**
	 * @return the salary
	 */
	public BigDecimal getSalary() {
		return salary;
	}
	/**
	 * @param salary the salary to set
	 */
	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
	/**
	 * @return the comm_pct
	 */
	public BigDecimal getComm_pct() {
		return comm_pct;
	}
	/**
	 * @param comm_pct the comm_pct to set
	 */
	public void setComm_pct(BigDecimal comm_pct) {
		this.comm_pct = comm_pct;
	}
	/**
	 * @return the manager_id
	 */
	public int getManager_id() {
		return manager_id;
	}
	/**
	 * @param manager_id the manager_id to set
	 */
	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}
	/**
	 * @return the dept_id
	 */
	public int getDept_id() {
		return dept_id;
	}
	/**
	 * @param dept_id the dept_id to set
	 */
	public void setDept_id(int dept_id) {
		this.dept_id = dept_id;
	}
	
	public String toString() {
    	String out = "Employee ID: " + this.getEmployee_id() + "\n";
    	out += "Employee Name: " + this.getFirst_name() + " " + this.getLast_name() + "\n";
    	out += "Email: " + this.getEmail() + "\n";
    	out += "Phone Number: " + this.getPhone_number() + "\n";
    	out += "Hire Date: " + this.getHire_date() + "\n";
    	out += "Job ID: " + this.getJob_id() + "\n";
    	if (this.getSalary() != null) {
    		out += "Salary: $" + this.getSalary().toString() + "\n";	
    	} else {
    		out += "Salary: NULL \n";
    	}
    	if (this.getComm_pct() != null) {
    		out += "Commission %: " + this.getComm_pct().toString() + "\n";		
    	} else {
    		out += "Commission %: NULL \n";
    	}
    	out += "Manager ID: " + this.getManager_id() + "\n";		    	
    	out += "Department ID: " + this.getDept_id() + "\n";	
		return out;
	}
}
