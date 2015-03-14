/*****************************************************************************************
 *  Student Names: Laurie Shields (034448142)
 *                 Mark Lindan (063336143)
 *  CJV805 - DBUtilities.java
 * **************************************************************************************/
package ca.myseneca.model;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public final class DBUtilities {
	private static final String PROPERTY_FILE_NAME = "database.properties";
	private static Properties dbProps = null;

	private static Properties getDbProps() {
		return dbProps;
	}

	private static void loadDbProps() throws FileNotFoundException, IOException {
		dbProps = new Properties();
		FileInputStream fis = new FileInputStream(PROPERTY_FILE_NAME);
		dbProps.load(fis);
	}
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			if (getDbProps() == null) {
				loadDbProps();
			}
			String driver = getDbProps().getProperty("ORACLE_DB_DRIVER");
	        String connUrl = getDbProps().getProperty("ORACLE_DB_CONN_URL");
	        Properties connProps = new Properties();
			connProps.put("user", getDbProps().getProperty("ORACLE_DB_USERNAME"));
			connProps.put("password", getDbProps().getProperty("ORACLE_DB_PASSWORD"));
			Class.forName(driver);
			conn = DriverManager.getConnection(connUrl, connProps);
		} catch (FileNotFoundException fnfex) {
			System.err.println(PROPERTY_FILE_NAME + " file not found");
		} catch (IOException ioex) {
			System.err.println("Error loading " + PROPERTY_FILE_NAME);
		} catch (ClassNotFoundException cnfex) {
            System.err.println("Failed to load JDBC/ODBC driver.");
        } catch (SQLException e) {
            System.out.println("The error is:  " + e.getMessage());
            e.printStackTrace();
        }
		return conn;
	}
		
	public static void closeConnection(Connection conn) {
		try {
	        if (conn != null) {
	        	conn.close();
	        	conn = null;
	        }	
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
	
	public static void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: "
						+ ((SQLException) e).getSQLState());
				System.err.println("Error Code: "
						+ ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.err.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
	
	public static void printWarnings(SQLWarning warning) throws SQLException {
		if (warning != null) {
			System.err.println("\n---Warning---\n");
			while (warning != null) {
				System.err.println("Message: " + warning.getMessage());
				System.err.println("SQLState: " + warning.getSQLState());
				System.err.print("Vendor error code: ");
				System.err.println(warning.getErrorCode());
				System.err.println("");
				warning = warning.getNextWarning();
			}
		}
	}

	public static void printBatchUpdateException(BatchUpdateException b) {
		System.err.println("----BatchUpdateException----");
		System.err.println("SQLState:  " + b.getSQLState());
		System.err.println("Message:  " + b.getMessage());
		System.err.println("Vendor:  " + b.getErrorCode());
		System.err.print("Update counts:  ");
		int[] updateCounts = b.getUpdateCounts();
		for (int i = 0; i < updateCounts.length; i++) {
			System.err.print(updateCounts[i] + "   ");
		}
	}
}
