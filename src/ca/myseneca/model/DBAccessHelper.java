/*****************************************************************************************
 *  Student Names: Laurie Shields (034448142)
 *                 Mark Lindan (063336143)
 *  CJV805 - DBAccessHelper.java
 * **************************************************************************************/
package ca.myseneca.model;

import java.sql.*;
import java.util.ArrayList;
import oracle.jdbc.*;

public final class DBAccessHelper {

	private static Connection conn = null;

	// All these method should handle SQLException and/or BatchUpdateException
	// by printing out SQL state, error message, error code and etc. on error
	// console.
	// Make sure each of the JDBC Statement (for executing static SQL),
	// PreparedStatement, CallableStatement, OracleCallableStatment or
	// UpdatableResultSet should be used at least once in the above methods.

	public static int getEmployeeID(String user, String password) {
		CallableStatement stmt = null;
		int retVal = 0;
		// You should call the PL/SQL function F_SECURITY in the P_SECURITY
		// package; the method will return a 0 value for unauthorized user.
		if (conn == null) {
			if ((conn = DBUtilities.getConnection()) == null) {
				// couldn't get a connection
				return 0;
			}
		}
		try {
			stmt = conn.prepareCall("{ ? = call cjv805_151a21.P_SECURITY.F_SECURITY( ?, ? ) }");
			// set up the input parameters and the return value
			stmt.registerOutParameter(1, Types.INTEGER); // declare type of
															// function return
															// value
			stmt.setString(2, user); // declare type of second parameter
			stmt.setString(3, password); // declare type of third parameter

			// call the stored function
			stmt.execute();
			retVal = stmt.getInt(1);
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				DBUtilities.printSQLException(e);
			}
		} // Finally
		return retVal;
	}

	// The CRUD methods (create, read, update and delete) operations for the
	// Employees tables.

	public static ArrayList<Employee> getAllEmployees() {
		ArrayList<Employee> tmpList = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (conn == null) {
			if ((conn = DBUtilities.getConnection()) == null) {
				// couldn't get a connection
				return null;
			}
		}

		try {
			tmpList = new ArrayList<Employee>();
			stmt = conn.prepareStatement("SELECT employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id FROM employees");
			rs = stmt.executeQuery();
			while (rs.next()) {
				Employee emp = populateEmp(rs);
				tmpList.add(emp);
			}
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				DBUtilities.printSQLException(e);
			}
		} // finally
		return tmpList;
	}

	public static ArrayList<Employee> getEmployeesByDepartmentID(int depid) {
		ArrayList<Employee> tmpList = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (conn == null) {
			if ((conn = DBUtilities.getConnection()) == null) {
				// couldn't get a connection
				return null;
			}
		}
		try {
			tmpList = new ArrayList<Employee>();
			stmt = conn
					.prepareStatement("SELECT employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id FROM employees WHERE department_id=?");
			stmt.setInt(1, depid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Employee emp = populateEmp(rs);
				tmpList.add(emp);
			}
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				DBUtilities.printSQLException(e);
			}
		} // finally
		return tmpList;
	}

	// call the stored procedure P_EMP_INFO in the P_SECURITY package
	public static Employee getEmployeeByID(int empid) {
		OracleCallableStatement stmt = null;
		OracleResultSet ors = null;
		Employee emp = null;
		
		if (conn == null) {
			if ((conn = DBUtilities.getConnection()) == null) {
				// couldn't get a connection
				return null;
			}
		}
		try {
			stmt = (OracleCallableStatement) conn.prepareCall("{call cjv805_151a21.P_SECURITY.P_EMP_INFO( ?, ? ) }");
			// set up the IN and OUT parameters
			stmt.setInt(1, empid); // declare type of first parameter
			stmt.registerOutParameter(2, OracleTypes.CURSOR); // declare type of
																// second
																// parameter
			stmt.execute();
			ors = (OracleResultSet) stmt.getCursor(2);
			if (ors.next()) {
				emp = populateEmp(ors);
			}
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (ors != null)
					ors.close();
			} catch (SQLException e) {
				DBUtilities.printSQLException(e);
			}
		}
		return emp;
	}

	public static void addNewEmployee(Employee emp) {
		Statement stmt = null;
		ResultSet rs = null;
		String sqlQuery = "SELECT employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id FROM employees";
		if (emp == null) {
			// employee object is null...nothing for us to do
			return;
		}
		if (conn == null) {
			if ((conn = DBUtilities.getConnection()) == null) {
				// couldn't get a connection
				return;
			}
		}
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sqlQuery);
			if (rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE) {
				rs.moveToInsertRow();
				rs.updateInt(1, emp.getEmployee_id());
				rs.updateString(2, emp.getFirst_name());
				rs.updateString(3, emp.getLast_name());
				rs.updateString(4, emp.getEmail());
				rs.updateString(5, emp.getPhone_number());
				rs.updateDate(6, emp.getHire_date());
				rs.updateString(7, emp.getJob_id());
				rs.updateBigDecimal(8, emp.getSalary());
				rs.updateBigDecimal(9, emp.getComm_pct());
				rs.updateInt(10, emp.getManager_id());
				rs.updateInt(11, emp.getDept_id());
				rs.insertRow();
				rs.moveToCurrentRow();
			} else {
				System.out.println("ResultSet is not an updatable result set.");
			}
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				DBUtilities.printSQLException(e);
			}
		}
	}

	public static int updateEmployee(Employee emp) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int retVal = 0;
		if (emp == null) {
			// employee object is null...not much more we can do
			return 0;
		}
		if (conn == null){
			if ((conn = DBUtilities.getConnection()) == null) {
				// couldn't get a connection
				return 0;
			}
		}
		try {
			stmt = conn.prepareStatement(
							"SELECT employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id from employees where employee_id=?",
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, emp.getEmployee_id());
			rs = stmt.executeQuery();
			// Check the result set is an updatable result set
			if (rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE) {
				rs.first();
				// go through and update whatever has changed...can't change ID
				if (rs.getString(2) != emp.getFirst_name()) {
					rs.updateString(2, emp.getFirst_name());
				}
				if (rs.getString(3) != emp.getLast_name()) {
					rs.updateString(3, emp.getLast_name());
				}
				if (rs.getString(4) != emp.getEmail()) {
					rs.updateString(4, emp.getEmail());
				}
				if (rs.getString(5) != emp.getPhone_number()) {
					rs.updateString(5, emp.getPhone_number());
				}
				if (rs.getDate(6) != emp.getHire_date()) {
					rs.updateDate(6, emp.getHire_date());
				}
				if (rs.getString(7) != emp.getJob_id()) {
					rs.updateString(7, emp.getJob_id());
				}
				if (rs.getBigDecimal(8) != emp.getSalary()) {
					rs.updateBigDecimal(8, emp.getSalary());
				}
				if (rs.getBigDecimal(9) != emp.getComm_pct()) {
					rs.updateBigDecimal(9, emp.getComm_pct());
				}
				if (rs.getInt(10) != emp.getManager_id()) {
					rs.updateInt(10, emp.getManager_id());
				}
				if (rs.getInt(11) != emp.getDept_id()) {
					rs.updateInt(11, emp.getDept_id());
				}
				rs.updateRow();
				retVal = 1; // no return value from updateRow() call so just use 1
			} else {
				System.out.println("ResultSet is not an updatable result set.");
			}
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				DBUtilities.printSQLException(e);
			}
		}
		return retVal;
	}

	public static int deleteEmployeeByID(int empid) {
		int retVal = 0;
		PreparedStatement stmt = null;
		if (conn == null) {
			if ((conn = DBUtilities.getConnection()) == null) {
				// couldn't get a connection
				return 0;
			}
		}
		try {
			stmt = conn.prepareStatement("DELETE FROM employees WHERE employee_id=?");
			stmt.setInt(1, empid);
			retVal = stmt.executeUpdate();
		} catch (SQLException e) {
			DBUtilities.printSQLException(e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				DBUtilities.printSQLException(e);
			}
		}
		return retVal;
	}

	// The batch update method:
	public static boolean batchUpdate(String[] SQLs) {
		// The batch update should be executed inside a transaction to make sure
		// that either all updates are executed or none are. Any successful
		// updates
		// can be rolled back, in case of the update fail.
		Statement stmt = null;
		boolean retVal = false;
		if (SQLs.length == 0) {
			// there were no statements to execute
			return false;
		}
		if (conn == null) {
			if ((conn = DBUtilities.getConnection()) == null) {
				// couldn't get a connection
				return false;
			}
		}
		try {
			stmt = conn.createStatement();
			conn.setAutoCommit(false);
			for (int i=0; i<SQLs.length; i++) {
				stmt.addBatch(SQLs[i]);
			}
			int[] count = stmt.executeBatch();
			conn.commit();
			retVal = true;
		} catch (BatchUpdateException b) {
			DBUtilities.printBatchUpdateException(b);
		} catch (SQLException ex) {
			DBUtilities.printSQLException(ex);
		} finally {
			try {
				conn.setAutoCommit(true);
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				DBUtilities.printSQLException(e);
			}

		}
		return retVal;
	}

	// helper method to convert from the result set into the Employee class
	private static Employee populateEmp(ResultSet rs) throws SQLException {
		if (rs != null) {
			Employee emp = new Employee();
			emp.setEmployee_id(rs.getInt(1));
			emp.setFirst_name(rs.getString(2));
			emp.setLast_name(rs.getString(3));
			emp.setEmail(rs.getString(4));
			emp.setPhone_number(rs.getString(5));
			emp.setHire_date(rs.getDate(6));
			emp.setJob_id(rs.getString(7));
			emp.setSalary(rs.getBigDecimal(8));
			emp.setComm_pct(rs.getBigDecimal(9));
			emp.setManager_id(rs.getInt(10));
			emp.setDept_id(rs.getInt(11));
			return emp;
		} else {
			return null;
		}
	}
}
