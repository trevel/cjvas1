/*****************************************************************************************
 *  Student Names: Laurie Shields (034448142)
 *                 Mark Lindan (063336143)
 *  CJV805 - DBAccessServer.java
 * **************************************************************************************/
package ca.myseneca.rmi.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class DBAccessServer {

	public DBAccessServer() {
		try {
			DBAccess dba = new DBAccessImpl();
			LocateRegistry.createRegistry(1299);
			Naming.rebind("rmi://localhost:1299/DBAService", dba);
		} catch (Exception e) {
			System.out.println("Something went wrong: " + e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new DBAccessServer();
	}

}
