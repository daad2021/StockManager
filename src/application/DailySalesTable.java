package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DailySalesTable extends VBox {
	private static TableView<DailySales> table;
	private TableColumn<DailySales, String> itemNameCol;
	private TableColumn<DailySales, String> brandNameCol;
	private TableColumn<DailySales, String> categoryNameCol;
	private TableColumn<DailySales, Integer> totalStockCol;
	private TableColumn<DailySales, Integer> quantitySold;
	private TableColumn<DailySales, Integer> remainingStockCol;
	private TableColumn<DailySales, Double> totalSalesCol;
	private TableColumn<DailySales, String> transactionDateCol;
	private static ObservableList<DailySales> data;
	
	public DailySalesTable() throws SQLException {
		data = FXCollections.observableArrayList();
		setCurrentDayData();
		
		// Initialising the table object 
		table = new TableView<DailySales>();
		
		// Initialising the table columns

		itemNameCol = new TableColumn<DailySales, String>("Name");
		brandNameCol = new TableColumn<DailySales, String>("Brand");
		categoryNameCol = new TableColumn<DailySales, String>("Category");
		totalStockCol = new TableColumn<DailySales, Integer>("Opening stock");
		quantitySold = new TableColumn<DailySales, Integer>("Qty Sold");
		remainingStockCol = new TableColumn<DailySales, Integer>("Closing Stock");
		totalSalesCol = new TableColumn<DailySales, Double>("Total sales");
		transactionDateCol = new TableColumn<DailySales, String>("Date");
		
		// Linking the table cell to the model class fields
		itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		brandNameCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
		categoryNameCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		totalStockCol.setCellValueFactory(new PropertyValueFactory<>("totalStock"));
		quantitySold.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
		remainingStockCol.setCellValueFactory(new PropertyValueFactory<>("remainingStock"));
		totalSalesCol.setCellValueFactory(new PropertyValueFactory<>("totalSales"));
		transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("transDate"));
		
		// Adding the columns to the table
		table.getColumns().addAll(itemNameCol, brandNameCol, categoryNameCol, totalStockCol, quantitySold, remainingStockCol, totalSalesCol, transactionDateCol);
		table.setPrefSize(650, 300);
		table.setItems(getData());
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		getChildren().add(table);
	}
	
	public static TableView<DailySales> getTable() {
		return table;
	}

	public static void setTable(TableView<DailySales> table) {
		DailySalesTable.table = table;
	}

	public static ObservableList<DailySales>getData() {
		return data;
	}

	// Get the default sales data from DB using the current day's date and add to the observable list
	public static void setCurrentDayData() throws SQLException {
		
		String qry = "SELECT item.name AS itemName, brand.name AS brandName, category.name AS categoryName, sales.totalStock AS totalStock, SUM(sales.quantity) AS quantitySold, stock.quantity AS remainingStock, SUM(sales.totalPrice) AS totalSales, sales.transDate AS transDate "
				+ "FROM sales "
				+ "JOIN item "
				+ "ON item.id=sales.itemId "
				+ "JOIN brand "
				+ "ON brand.id=sales.brandId "
				+ "JOIN category "
				+ "ON category.id=sales.categoryId "
				+ "JOIN stock "
				+ "ON stock.itemId=item.id "
				+ "WHERE sales.transDate= ? "
				+ "GROUP BY sales.itemId "
				+ "ORDER BY quantitySold DESC";	
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;		
		try {			
			conn = DBConnection.connect();
			ps = conn.prepareStatement(qry);
			ps.setString(1, CommonSource.getDateAndTime("yyyyMMdd"));
			rs = ps.executeQuery();
			data.clear();
			while(rs.next()) {
				data.add(new DailySales(
						rs.getString("itemName"),
						rs.getString("brandName"),
						rs.getString("categoryName"),
						rs.getInt("totalStock"),
						rs.getInt("quantitySold"),
						rs.getInt("remainingStock"),
						rs.getDouble("totalSales"),
						rs.getString("transDate")
						));
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}	
	}
	
	// Get the sales data for all item from DB by date and add to the observable list
	public static void queryAllByDate() throws SQLException {
		
		String qry = "SELECT item.name AS itemName, brand.name AS brandName, category.name AS categoryName, sales.totalStock AS totalStock, SUM(sales.quantity) AS quantitySold, stock.quantity AS remainingStock, SUM(sales.totalPrice) AS totalSales, sales.transDate AS transDate "
				+ "FROM sales "
				+ "JOIN item "
				+ "ON item.id=sales.itemId "
				+ "JOIN brand "
				+ "ON brand.id=sales.brandId "
				+ "JOIN category "
				+ "ON category.id=sales.categoryId "
				+ "JOIN stock "
				+ "ON stock.itemId=item.id "
				+ "WHERE sales.transDate BETWEEN ? AND ? "
				+ "GROUP BY sales.itemId "
				+ "ORDER BY transDate DESC";	

		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {			
			conn = DBConnection.connect();
			ps = conn.prepareStatement(qry);
			ps.setString(1, ReportPage.getFromDateVar());
			ps.setString(2, ReportPage.getToDateVar());
				
			rs = ps.executeQuery();

			data.clear();
			while(rs.next()) {
				data.add(new DailySales(
						rs.getString("itemName"),
						rs.getString("brandName"),
						rs.getString("categoryName"), 
						rs.getInt("totalStock"),
						rs.getInt("quantitySold"),
						rs.getInt("remainingStock"),
						rs.getDouble("totalSales"),
						rs.getString("transDate")
						));
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}	
	}
	
	// Get the sales data from DB by item id, name, date and add to the observable list
	public static void queryByIdNameAndDate() throws SQLException {
		
		String qrySpecific = "SELECT item.name AS itemName, brand.name AS brandName, category.name AS categoryName, MAX(sales.totalStock) AS totalStock, SUM(sales.quantity) AS quantitySold, SUM(sales.totalPrice) AS totalSales, sales.remainingStock AS remainingStock, sales.transDate AS date "
				+ "FROM sales "
				+ "JOIN item "
				+ "ON item.id=sales.itemId "
				+ "JOIN brand "
				+ "ON brand.id=sales.brandId "
				+ "JOIN category "
				+ "ON category.id=sales.categoryId "
				+ "JOIN stock "
				+ "ON stock.itemId=item.id "
				+ "WHERE sales.itemId=? AND item.name=? AND sales.transDate BETWEEN ? AND ? "
				+ "GROUP BY date "
				+ "ORDER BY date DESC";	
		
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {			
			conn = DBConnection.connect();
				
			ps = conn.prepareStatement(qrySpecific);
			ps.setString(1, ReportPage.getIdCombo().getEditor().getText());
			ps.setString(2, ReportPage.getNameCombo().getEditor().getText());
			ps.setString(3, ReportPage.getFromDateVar());
			ps.setString(4, ReportPage.getToDateVar());
				
			rs = ps.executeQuery();

			data.clear();
			while(rs.next()) {
				data.add(new DailySales(
						rs.getString("itemName"), 
						rs.getString("brandName"),
						rs.getString("categoryName"),
						rs.getInt("totalStock"),
						rs.getInt("quantitySold"),
						rs.getInt("remainingStock"),
						rs.getDouble("totalSales"),
						rs.getString("date")
						));
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}	
	}
	
	
	// Get the sales data from DB by category and date and add to the observable list
	public static void queryByCategoryAndDate() throws SQLException {
		
		String qrySpecific = "SELECT  item.name AS itemName, brand.name AS brandName, category.name categoryName, MAX(sales.totalStock) AS totalStock, SUM(sales.quantity) AS quantitySold, SUM(sales.totalPrice) AS totalSales, sales.remainingStock AS remainingStock, sales.transDate AS date "
				+ "FROM sales "
				+ "JOIN item "
				+ "ON item.id=sales.itemId "
				+ "JOIN brand "
				+ "ON brand.id=sales.brandId "
				+ "JOIN category "
				+ "ON category.id=sales.categoryId "
				+ "JOIN stock "
				+ "ON stock.itemId=item.id "
				+ "WHERE sales.categoryId=(SELECT id FROM category WHERE name=?) AND sales.transDate BETWEEN ? AND ? "
				+ "GROUP BY categoryName "
				+ "ORDER BY date DESC";	
		
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {			
			conn = DBConnection.connect();			
			ps = conn.prepareStatement(qrySpecific);
			ps.setString(1, ReportPage.getCategoryCombo().getEditor().getText());
			ps.setString(2, ReportPage.getFromDateVar());
			ps.setString(3, ReportPage.getToDateVar());		
			rs = ps.executeQuery();
			data.clear();
			while(rs.next()) {
				data.add(new DailySales(
						rs.getString("itemName"), 
						rs.getString("brandName"),
						rs.getString("categoryName"),
						rs.getInt("totalStock"),
						rs.getInt("quantitySold"),
						rs.getInt("remainingStock"),
						rs.getDouble("totalSales"),
						rs.getString("date")
						));
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}	
	}
	
	// Get the sales data from DB for all category and date and add to the observable list
	public static void queryByAllCategoryAndDate() throws SQLException {
		
		String qrySpecific = "SELECT item.name AS itemName, brand.name AS brandName, category.name categoryName, MAX(sales.totalStock) AS totalStock, SUM(sales.quantity) AS quantitySold, SUM(sales.totalPrice) AS totalSales, sales.remainingStock AS remainingStock, sales.transDate AS date "
				+ "FROM sales "
				+ "JOIN item "
				+ "ON item.id=sales.itemId "
				+ "JOIN stock "
				+ "ON stock.itemId=item.id "
				+ "JOIN brand "
				+ "ON brand.id=sales.brandId "
				+ "JOIN category "
				+ "ON category.id=sales.categoryId "
				+ "WHERE sales.categoryId=category.id AND sales.transDate BETWEEN ? AND ? "
				+ "GROUP BY categoryName "
				+ "ORDER BY date DESC";	
		
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {			
			conn = DBConnection.connect();
				
			ps = conn.prepareStatement(qrySpecific);
			ps.setString(1, ReportPage.getFromDateVar());
			ps.setString(2, ReportPage.getToDateVar());
				
			rs = ps.executeQuery();

			data.clear();
			while(rs.next()) {
				data.add(new DailySales(
						rs.getString("itemName"), 
						rs.getString("brandName"),
						rs.getString("categoryName"),
						rs.getInt("totalStock"),
						rs.getInt("quantitySold"),
						rs.getInt("remainingStock"),
						rs.getDouble("totalSales"),
						rs.getString("date")
						));
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}	
	}
	
	// Get the sales data from DB by category and date and add to the observable list
	public static void queryByBrandAndDate() throws SQLException {
		
		String qrySpecific = "SELECT item.name AS itemName, brand.name AS brandName, category.name AS categoryName, MAX(sales.totalStock) AS totalStock, SUM(sales.quantity) AS quantitySold, SUM(sales.totalPrice) AS totalSales, sales.remainingStock AS remainingStock, sales.transDate AS date "
				+ "FROM sales "
				+ "JOIN item "
				+ "ON item.id=sales.itemId "
				+ "JOIN stock "
				+ "ON stock.itemId=item.id "
				+ "JOIN category "
				+ "ON category.id=sales.categoryId "
				+ "JOIN brand "
				+ "ON brand.id=sales.brandId "
				+ "WHERE sales.brandId=(SELECT id FROM brand WHERE name=?) AND sales.transDate BETWEEN ? AND ? "
				+ "GROUP BY date "
				+ "ORDER BY date DESC";	
		
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {			
			conn = DBConnection.connect();				
			ps = conn.prepareStatement(qrySpecific);
			ps.setString(1, ReportPage.getBrandCombo().getEditor().getText());
			ps.setString(2, ReportPage.getFromDateVar());
			ps.setString(3, ReportPage.getToDateVar());			
			rs = ps.executeQuery();
			data.clear();
			while(rs.next()) {
				data.add(new DailySales(
						rs.getString("itemName"), 
						rs.getString("brandName"),
						rs.getString("categoryName"),
						rs.getInt("totalStock"),
						rs.getInt("quantitySold"),
						rs.getInt("remainingStock"),
						rs.getDouble("totalSales"),
						rs.getString("date")
						));
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}	
	}
	
	// Get the sales data from DB for all category and date and add to the observable list
	public static void queryByAllBrandAndDate() throws SQLException {
		
		String qrySpecific = "SELECT item.name AS itemName, brand.name AS brandName, category.name categoryName, MAX(sales.totalStock) AS totalStock, SUM(sales.quantity) AS quantitySold, SUM(sales.totalPrice) AS totalSales, sales.remainingStock AS remainingStock, sales.transDate AS date "
				+ "FROM sales "
				+ "JOIN item "
				+ "ON item.id=sales.itemId "
				+ "JOIN stock "
				+ "ON stock.itemId=item.id "
				+ "JOIN brand "
				+ "ON brand.id=sales.brandId "
				+ "JOIN category "
				+ "ON category.id=sales.categoryId "
				+ "WHERE sales.brandId=brand.id AND sales.transDate BETWEEN ? AND ? "
				+ "GROUP BY brandName "
				+ "ORDER BY date DESC";	
		
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {			
			conn = DBConnection.connect();			
			ps = conn.prepareStatement(qrySpecific);
			ps.setString(1, ReportPage.getFromDateVar());
			ps.setString(2, ReportPage.getToDateVar());			
			rs = ps.executeQuery();
			data.clear();
			while(rs.next()) {
				data.add(new DailySales(
						rs.getString("itemName"), 
						rs.getString("brandName"),
						rs.getString("categoryName"),
						rs.getInt("totalStock"),
						rs.getInt("quantitySold"),
						rs.getInt("remainingStock"),
						rs.getDouble("totalSales"),
						rs.getString("date")
						));
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}	
	}
	
	// Calculate the total sales amount for the query from the list data
	public static double getTotalAmount() {
		double amount = 0.0;
		for (DailySales item : data) {
			amount += item.getTotalSales();
		}
		return amount;
	}
	
	// Calculate the total number of item sold for the query from the list data
	public static int getTotalItemsSold() {
		int total = 0;
		for (DailySales item : data) {
			total += item.getQuantitySold();
		}
		return total;
	}
	
	// Find the opening stock
	public static int getOpeningStock(ObservableList<DailySales> data) {
		int openingStock = 0;
		int transDate = 20801231;
		int j = 0;
		for (int i=0; i<data.size(); i++) {
			if (Integer.valueOf(data.get(i).getTransDate()) < transDate) {
				transDate = Integer.valueOf(data.get(i).getTransDate());
				j = i;
			}
			openingStock = data.get(j).getTotalStock();
		}
		return openingStock;
	}
	
	// Find the closing stock
	public static int getClosingStock(ObservableList<DailySales> data) {
		int closingStock = 0;
		int transDate = 1;
		int j = 0;
		for (int i=0; i<data.size(); i++) {
			if (Integer.valueOf(data.get(i).getTransDate()) > transDate) {
				transDate = Integer.valueOf(data.get(i).getTransDate());
				j = i;
			}
			closingStock = data.get(j).getRemainingStock();
		}
		return closingStock;
	}
	
	// Calculate the total number of the items remaining in stock or the closing stock for the query from the list data
	public static int getTotalClosingStock(ObservableList<DailySales> data) {
		int total = 0;
		for (DailySales item : data) {
			total += item.getRemainingStock();
		}
		return total;
	}
		
	
	// Reload or refresh the sales data
//	public static void refreshSalesData() throws SQLException {
//		table.refresh();
////		DailySalesTable.getDefaultData().clear();
////		DailySalesTable.setData();
//		DailySalesTable.getTable().getItems().removeAll();
//		DailySalesTable.getTable().getItems().addAll(getDefaultData());
//	}
}
