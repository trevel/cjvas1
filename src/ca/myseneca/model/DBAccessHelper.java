package ca.myseneca.model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

import oracle.jdbc.*;

public final class DBAccessHelper {

	private static Connection conn = null;

	/*private DBAccessHelper(Connection connection) {
		conn = connection;
	}
*/
	public static void setConn(Connection connection) {
		conn = connection;
	}

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
			return 0;
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
			DBUtilities.printWarnings(stmt.getWarnings());
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
			return null;
		}

		try {
			tmpList = new ArrayList<Employee>();
			stmt = conn.prepareStatement("SELECT employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id FROM employees");
			rs = stmt.executeQuery();
			DBUtilities.printWarnings(stmt.getWarnings());
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
			return null;
		}
		try {
			tmpList = new ArrayList<Employee>();
			stmt = conn.prepareStatement("SELECT employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id FROM employees WHERE department_id=?");
			stmt.setInt(1, depid);
			rs = stmt.executeQuery();
			DBUtilities.printWarnings(stmt.getWarnings());
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

	public static Employee getEmployeeByID(int empid) {
		OracleCallableStatement stmt = null;
		OracleResultSet ors = null;
		Employee emp = null;
		// you should call the stored procedure P_EMP_INFO in the P_SECURITY
		// package
		if (conn == null) {
			return null;
		}
		try {
			stmt = (OracleCallableStatement) conn.prepareCall("{call cjv805_151a21.P_SECURITY.P_EMP_INFO( ?, ? ) }");
			// set up the IN and OUT parameters
			stmt.setInt(1, empid); // declare type of first parameter
			stmt.registerOutParameter(2, OracleTypes.CURSOR); // declare type of
																// second
																// parameter
			stmt.execute();
			DBUtilities.printWarnings(stmt.getWarnings());
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
		if (conn == null || emp == null) {
			return;
		}
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sqlQuery);
			DBUtilities.printWarnings(stmt.getWarnings());
			if (rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE) {
				rs.moveToInsertRow();
				rs.updateInt(1, emp.getEmployee_id());
				rs.updateString(2, emp.getFirst_name());
				rs.updateString(3, emp.getLast_name());
				rs.updateString(4, emp.getEmail());
				rs.updateString(5, emp.getPhone_number());
				rs.updateDate(6, emp.getHire_date());
				rs.updateString(7, emp.getJob_id());
				if (emp.getSalary().compareTo(BigDecimal.ZERO) == 0) {
					rs.updateBigDecimal(8, null);
				} else {
					rs.updateBigDecimal(8, emp.getSalary());
				}
				if (emp.getComm_pct().compareTo(BigDecimal.ZERO) == 0) {
					rs.updateBigDecimal(9, null);
				} else {
					rs.updateBigDecimal(9, emp.getComm_pct());
				}
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
		if (conn == null || emp == null) {
			return 0;
		}
		try {
			stmt = conn.prepareStatement(
							"SELECT employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id from employees where employee_id=?",
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, emp.getEmployee_id());
			rs = stmt.executeQuery();
			DBUtilities.printWarnings(stmt.getWarnings());
			// Check the result set is an updatable result set
			if (rs.getConcurrency() == ResultSet.CONCUR_UPDATABLE) {
				rs.first();
				rs.updateInt("DEPARTMENT_ID", 30);
				rs.updateRow();
				retVal = 1;
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
			return 0;
		}
		try {
			stmt = conn.prepareStatement("DELETE FROM employees WHERE employee_id=?");
			stmt.setInt(1, empid);
			DBUtilities.printWarnings(stmt.getWarnings());
			retVal = stmt.executeUpdate();
			DBUtilities.printWarnings(stmt.getWarnings());
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
		if (conn == null) {
			return false;
		}
		return false;
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
			if (rs.wasNull()) {
				emp.setSalary(BigDecimal.ZERO);
			}
			emp.setComm_pct(rs.getBigDecimal(9));
			if (rs.wasNull()) {
				emp.setComm_pct(BigDecimal.ZERO);
			}
			emp.setManager_id(rs.getInt(10));
			emp.setDept_id(rs.getInt(11));
			return emp;
		} else {
			return null;
		}
	}
}
