/*****************************************************************************************
 *  Student Names: Laurie Shields (034448142)
 *                 Mark Lindan (063336143)
 *  CJV805 - DBUtilities.java
 * **************************************************************************************/
package ca.myseneca.model;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DBUtilities {
	private Properties dbProps;

	public DBUtilities(String filePath) throws FileNotFoundException, IOException {
		super();
		this.setDbProps(filePath);
	}

	/**
	 * @return the dbProps
	 */
	public Properties getDbProps() {
		return dbProps;
	}

	/**
	 * @param dbProps the dbProps to set
	 */
	public void setDbProps(String filePath) throws FileNotFoundException, IOException, IllegalArgumentException  {
		this.dbProps = new Properties();
		FileInputStream fis = new FileInputStream(filePath);
		this.dbProps.load(fis);
	}
	
	public Connection getConnection(String propName) {
		Connection conn = null;
		String driver = dbProps.getProperty("ORACLE_DB_DRIVER");
        String connUrl = getDbProps().getProperty(propName);
        Properties connProps = new Properties();
		connProps.put("user", this.dbProps.getProperty("ORACLE_DB_USERNAME"));
		connProps.put("password", this.dbProps.getProperty("ORACLE_DB_PASSWORD"));
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(connUrl, connProps);
		} catch (ClassNotFoundException cnfex) {
            System.err.println("Failed to load JDBC/ODBC driver.");
        } catch (SQLException e) {
            System.out.println("The error is:  " + e.getMessage());
            e.printStackTrace();
        }
		return conn;
	}
	
	public Connection getThinConnection() {
		return getConnection("ORACLE_DB_URL_THIN");
	}
	
	public Connection getOCIConnection() {
		return getConnection("ORACLE_DB_URL_OCI");
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
