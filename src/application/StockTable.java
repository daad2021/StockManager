package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StockTable {
	private String itemId;
	private String name;
	private int quantity;
	private Button statusBtn, deleteBtn, updateBtn;
	private TableView<StockTable> table;
	private ObservableList<StockTable> list;

	StockTable(String itemId, String name, int quantity){
		this.itemId = itemId;
		this.name = name;
		this.quantity = quantity;
	}
	
	StockTable(){}
	
	
	
	// Function for building the stock table
	public TableView<StockTable> getTable() throws SQLException{
		
		table = new TableView<>();
//		TableColumn<Stock, Integer> idCol = new TableColumn<>("Id");
		TableColumn<StockTable, String> itemIdCol = new TableColumn<>("Item Id");
		TableColumn<StockTable, String> nameCol = new TableColumn<>("Item Name");
		TableColumn<StockTable, Integer> quantityCol = new TableColumn<>("Quantity");
		TableColumn<StockTable, Button> statusBtnCol = new TableColumn<>("Status");
		TableColumn<StockTable, Button> updateBtnCol = new TableColumn<>("");
		TableColumn<StockTable, Button> deleteBtnCol = new TableColumn<>("");
		
		table.getColumns().setAll(itemIdCol, nameCol, quantityCol, statusBtnCol, updateBtnCol, deleteBtnCol);
		
//		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		statusBtnCol.setCellValueFactory(new PropertyValueFactory<>("statusBtn"));
		updateBtnCol.setCellValueFactory(new PropertyValueFactory<>("updateBtn"));
		deleteBtnCol.setCellValueFactory(new PropertyValueFactory<>("deleteBtn"));
		
		getStockList();
		table.setPrefWidth(200);
		return table;
	}
	
	// Getters and setters
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	//
	public void getStockList() throws SQLException {
		
		ObservableList<StockTable> list = FXCollections.observableArrayList();
		
		String qry = "SELECT * FROM stock;";
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			
			conn = DBConnection.connect();
			st = conn.createStatement();			
			rs = st.executeQuery(qry);
			while(rs.next()) {
				list.add(new StockTable( 
						rs.getString("itemId"), 
						rs.getString("name"), 
						rs.getInt("quantity")));
			}			
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			rs.close();
			st.close();
			conn.close();
		}
		table.setItems(list);
	}
	
	// Add to stock
	public static void addToStock() throws SQLException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String currentDateTime = sdf.format(new Date());
		String sql = "INSERT INTO stock (itemId, brandId, categoryId, quantity, dateCreated) VALUES(?,(SELECT brand.id FROM brand JOIN item ON item.brand=brand.name WHERE item.id=?),(SELECT category.id FROM category JOIN item ON item.category=category.name WHERE item.id=?),?,?);";

		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnection.connect();
			ps = conn.prepareStatement(sql);			
			ps.setString(1, ItemPage.getStockIdCombo().getEditor().getText()); // itemId
			ps.setString(2, ItemPage.getStockIdCombo().getEditor().getText()); // itemId
			ps.setString(3, ItemPage.getStockIdCombo().getEditor().getText());
			ps.setInt(4, Integer.valueOf(ItemPage.getStockQtyTf().getText())); // qty
//			ps.setString(5, ItemPage.getStockIdCombo().getEditor().getText());
			ps.setString(5, currentDateTime);
			ps.executeUpdate();		
			conn.commit();
			SalesItemsTable.getData().removeAll(SalesItemsTable.getData());
			SalesItemsTable.setData();
			ItemPage.clearAddStockInputs();
			Message.showInfoAlert("Stock update successful!");
		}catch(Exception e) {
			if(e.getMessage().contains("UNIQUE constraint failed")) {
				Message.showErrorAlert("The item already exists!");
			}
		}
		finally {
			ps.close();
			conn.close();
		}
	}
	
	
//	// Get the item id
//	public static ObservableList<String> getStockId() throws SQLException {		
//		ObservableList<String> list = FXCollections.observableArrayList();
//		
//		String qry = "SELECT itemId FROM stock;";
//		
//		Connection conn = null;
//		Statement st = null;
//		ResultSet rs = null;	
//		try {	
//			conn = DBConnection.connect();
//			st = conn.createStatement();			
//			rs = st.executeQuery(qry);
//			while(rs.next()) {
//				list.add(rs.getString("itemId")); 
//			}			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		finally {
//			rs.close();
//			st.close();
//			conn.close();
//		}
//		return list;
//	}	
}
