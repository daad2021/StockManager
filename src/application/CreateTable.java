package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class CreateTable {
	
	private String sql_cust;
	private String sql_acc;
	private String sql_trans;
	private String sql_acctype;
	private String sql_login;
	private String sql_transtype, sql_stock, sql_sales;
	DBConnection dbc;
	
	public CreateTable() {
		dbc = new DBConnection();
		
	//Create the customer table
	 this.sql_cust = "CREATE TABLE customer(" + 
										"custId 	INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + 
										"firstName 	TEXT NOT NULL, " +
										"middleName TEXT, " +
										"lastName 	TEXT NOT NULL, " + 
										"dob 		CHAR(50), " + 
										"address 	TEXT, " +
										"phone 		TEXT );";
	
	//Create account table
	this.sql_acc = "CREATE TABLE account(" + 
										"accNo 		INTEGER PRIMARY KEY AUTOINCREMENT," +
										"custId 	INTEGER NOT NULL, " +
										"balance 	REAL NOT NULL," + 
										"typeId 	INTEGER NOT NULL," +
										"created 	TEXT 	DEFAULT CURRENT_TIMESTAMP NOT NULL," +
										"FOREIGN 	KEY(custid) REFERENCES customer(custid) ON UPDATE CASCADE, " + 
										"FOREIGN 	KEY(typeid) REFERENCES accounttype(typeId) ON UPDATE CASCADE);";
	
	//Create transaction table
	this.sql_trans = "CREATE TABLE transactions(" + 
											"trxnId 		INTEGER PRIMARY KEY AUTOINCREMENT," +
											"accNo		 	INTEGER	NOT NULL, " +
											"amount 		REAL 	NOT NULL," + 
											"trxnDate 		TEXT 	DEFAULT CURRENT_TIMESTAMP NOT NULL," +
											"operationId 	INTEGER NOT NULL, " +
															"FOREIGN KEY(accNo) REFERENCES account(accNo)," +
															"FOREIGN KEY(operationId) REFERENCES transactiontype(typeId));";
	
	//Create accounttype table
	this.sql_acctype = "CREATE TABLE accounttype(" + 
											"typeId 	INTEGER PRIMARY KEY AUTOINCREMENT," +
											"typeName 	TEXT NOT NULL, " + 
											"created 	TEXT 	DEFAULT CURRENT_TIMESTAMP NOT NULL);";
	
		
	//Create logincredentials table
	this.sql_login = "CREATE TABLE logincredentials(" + 
											"id 		INTEGER PRIMARY KEY AUTOINCREMENT," +
											"accNo 		INTEGER NOT NULL, " +
											"custId 	INTEGER NOT NULL, " +
											"password 	TEXT NOT NULL, " +
											"created 	TEXT 	DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
														"FOREIGN KEY(accNo) REFERENCES account(accno) ON UPDATE CASCADE," +
														"FOREIGN KEY(custId) REFERENCES customer(custId) ON UPDATE CASCADE );";
	
	//Create transaction table
	this.sql_transtype = "CREATE TABLE transactiontype(" + 
											"typeId 	INTEGER PRIMARY KEY AUTOINCREMENT," +
											"typeName 	TEXT NOT NULL);";

//	//Create stock table
//	this.sql_stock = "CREATE TABLE stock(" + 
//											"id 		INTEGER PRIMARY KEY AUTOINCREMENT," +
//											"itemId 		INTEGER NOT NULL, " +
//											"name 		TEXT NOT NULL, " +
//											"quantity 	INTEGER NOT NULL, " +
//														"FOREIGN KEY(itemId) REFERENCES item(id) ON UPDATE CASCADE);";

//******************************************************//	
	
	//Create stock table
	this.sql_stock = "CREATE TABLE stock(" + 
											"id 			INTEGER PRIMARY KEY AUTOINCREMENT," +
											"itemId 		TEXT NOT NULL, " +
											"brandId 		TEXT NOT NULL, " +
											"categoryId 	TEXT NOT NULL, " +
											"quantity 		INTEGER DEFAULT 0 NOT NULL, " +
											"dateCreated	 		TEXT NOT NULL, " +
															"FOREIGN KEY(itemId) REFERENCES item(id) ON UPDATE CASCADE," +
															"FOREIGN KEY(brandId) REFERENCES brand(id) ON UPDATE CASCADE, " +
															"FOREIGN KEY(categoryId) REFERENCES category(id) ON UPDATE CASCADE);";

	//Create stock table
		this.sql_sales = "CREATE TABLE sales(" + 
												"id 			INTEGER PRIMARY KEY AUTOINCREMENT," +
												"itemId 		TEXT NOT NULL, " +
												"brandId 		TEXT NOT NULL, " +
												"categoryId 	TEXT NOT NULL, " +
												"totalStock 	INTEGER DEFAULT 0 NOT NULL, " +
												"unitPrice	 	REAL 	NOT NULL," +
												"quantity 		INTEGER DEFAULT 0 NOT NULL, " +
												"totalPrice	 	REAL 	NOT NULL," +
												"remainingStock INTEGER DEFAULT 0 NOT NULL, " +
												"transDate	 	TEXT NOT NULL, " +
												"transNo	 	INTEGER NOT NULL, " +
																"FOREIGN KEY(itemId) REFERENCES item(id) ON UPDATE CASCADE," +
																"FOREIGN KEY(brandId) REFERENCES brand(id) ON UPDATE CASCADE, " +
																"FOREIGN KEY(categoryId) REFERENCES category(id) ON UPDATE CASCADE);";

	
	}
	
	
	public String getSQLCustQry() {
		return this.sql_cust;
	}
	
	public String getSQLAccQry() {
		return this.sql_acc;
	}
	
	public String getSQLTransQry() {
		return this.sql_trans;
	}
	
	public String getSQLAcctypeQry() {
		return this.sql_acctype;
	}
	
	public String getSQLLoginCredentialsQry() {
		return this.sql_login;
	}
	
	public String getSQLTransTypeQry() {
		return this.sql_transtype;
	}
	
	public String getSQLStockQry() {
		return this.sql_stock;
	}
	
	public String getSQLSalesQry() {
		return this.sql_sales;
	}
	
	
	// Create table function
	public static void createTable(String sql, String tableName) {
		try (Connection conn = DBConnection.connect(); 
				Statement stm = conn.createStatement()){
			stm.executeUpdate(sql);
			stm.close();
			conn.commit();
			conn.close();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		System.out.println(tableName + " table created successfully");
	}
	
	
	//Drop table function
	public static void dropTable(String name) {
		String sql = "DROP TABLE " + name + ";";
		try (Connection conn = DBConnection.connect(); Statement stm = conn.createStatement()){
			stm.executeUpdate(sql);
			stm.close();
			conn.commit();
			conn.close();
		}catch (SQLException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		System.out.println(name + " table deleted successfully");
	}
	
	
	//Insert data in the account type table
	public void insertAccountType(String name) {
		String sql = "INSERT INTO accounttype(typeName) VALUES(?);";
		try(Connection conn = DBConnection.connect(); PreparedStatement ps = conn.prepareStatement(sql);){
			ps.setString(1, name);
			ps.executeUpdate();
			ps.close();
			conn.commit();
			conn.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		System.out.println("Data insertion successful.");
	}	
}
