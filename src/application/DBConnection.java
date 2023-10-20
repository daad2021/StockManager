package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	public static Connection connect() {
		
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C:\\sqlite\\db\\inventoryDB.db");
			connection.setAutoCommit(false);
		}
		catch(SQLException e) {
			System.err.print(e.getClass().getName() + ": " + e.getMessage());
			System.out.println("Could not connect to the DB.");
		}
		return connection;
	}
}
