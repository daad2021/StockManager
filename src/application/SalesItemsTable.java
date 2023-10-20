package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class SalesItemsTable extends VBox{
	
	private static TableView<SalesModel> table;
	private TableColumn<SalesModel, String> id;
	private TableColumn<SalesModel, String> name;
	private TableColumn<SalesModel, String> brand;
	private TableColumn<SalesModel, String> category;
	private TableColumn<SalesModel, Double> price;
	private TableColumn<SalesModel, Integer> quantity;
	private TableColumn<SalesModel, Button> statusBtn;
	private static ObservableList<SalesModel> data;
	private static ObservableList<SalesModel> cartData;
	private static String seletedItemId;
	private static ObservableList<Object> list;
	
	public SalesItemsTable() throws SQLException {
		data = FXCollections.observableArrayList();
		setData();
		list = FXCollections.observableArrayList();
		
		// Initialising the table object 
		table = new TableView<SalesModel>();
		
		// Initialising the table columns
		id = new TableColumn<SalesModel, String>("ID");
		name = new TableColumn<SalesModel, String>("Name");
		brand = new TableColumn<SalesModel, String>("Brand");
		category = new TableColumn<SalesModel, String>("Category");
		price = new TableColumn<SalesModel, Double>("Unit price");
		quantity = new TableColumn<SalesModel, Integer>("Stock");
		statusBtn = new TableColumn<SalesModel, Button>("Status");
		
		// Linking the table cell to the model class fields
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
		category.setCellValueFactory(new PropertyValueFactory<>("category"));
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		statusBtn.setCellValueFactory(new PropertyValueFactory<>("statusBtn"));
		
		// Adding the columns to the table
		table.getColumns().addAll(id,name,brand,category,price,quantity,statusBtn);
		
		// Adding a listener to the table selected row
		try {
			table.getSelectionModel()
				 .selectedItemProperty()
				 .addListener((obValue, oldValue, newValue) -> {
	
					if(newValue != null) {
						// Setting the form fields with the selected items
						SalesPage.getCategoryTf().setText(newValue.getCategory());
						SalesPage.getBrandTf().setText(newValue.getBrand());
						SalesPage.getNameTf().setText(newValue.getName());
						SalesPage.getIdTf().setText(newValue.getId());
						SalesPage.getQuantitySp().getValueFactory().setValue(1);
						SalesPage.getQuantitySp().setEditable(true);
//						table.getSelectionModel().clearSelection();
//						SalesPage.getQuantitySp().getEditor().requestFocus();
						SalesPage.getQuantitySp().getEditor().selectAll();
						SalesPage.getUnitPriceTf().setText(String.valueOf(newValue.getPrice()));
						mountItemImage(newValue.getId());
						SalesPage.getTotalAmountLbl()
								 .setText(String.valueOf(
										 (newValue.getPrice() * SalesPage.getQuantitySp().getValue())));
						CartTable.getTable().getSelectionModel().clearSelection();
						SalesPage.getQuantitySp().setDisable(false);
						SalesPage.getQuantitySp().getEditor().requestFocus();
					}
			});	
			table.setItems(getFilteredData(getData()));
		}catch(java.lang.IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
//		table.setMaxSize(530, 155);
		table.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	}
	
	//
	public static TableView<SalesModel> getTable() {
		return table;
	}

	public static void setTable(TableView<SalesModel> table) {
		SalesItemsTable.table = table;
	}

	// Getters and setters
	public static String getSeletedItemId() {
		return seletedItemId;
	}

	public static void setSeletedItemId(String seletedItemId) {
		SalesItemsTable.seletedItemId = seletedItemId;
	}
	
	public static ObservableList<SalesModel> getCartData() {
		return cartData;
	}

	public void setCartData(ObservableList<SalesModel> data) {
		cartData = data;
	}

	public static ObservableList<SalesModel> getData() {
		return data;
	}
	
	// Retrieving records from the db to fill the sales items table
	public static void setData() throws SQLException {
//		data = FXCollections.observableArrayList();
		String sql = "SELECT i.id, i.name, i.brand, i.category, i.priceOut, s.quantity "
					+ "FROM item AS i "
					+ "JOIN stock AS s "
					+ "WHERE i.id=s.itemId";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;		
		try {
			conn = DBConnection.connect();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()) {
				data.add(new SalesModel(
						rs.getString("id"), 
						rs.getString("name"),
						rs.getString("brand"),
						rs.getString("category"),
						rs.getDouble("priceOut"), 
						rs.getInt("quantity")));
			}
		}catch(SQLException e) {
			System.out.println("Could not load the data: " + e.getMessage());
		}finally {
			rs.close();
			st.close();
			conn.close();
		}		
	}
	
	// Filter table records for search bar
	public SortedList<SalesModel> getFilteredData(ObservableList<SalesModel> data){
		FilteredList<SalesModel> filteredList = new FilteredList<>(data, b -> true);
		SalesPage.getSearchTf().textProperty().addListener((obs, ov, nv) -> {
			filteredList.setPredicate(item -> {
				if (nv == null || nv.isEmpty()) {
					return true;
				}else if(item.getId().toLowerCase().indexOf(nv.toLowerCase()) != -1) {
					return true;
				}
				else if(item.getName().toLowerCase().indexOf(nv.toLowerCase()) != -1) {
					return true;
				}
				else if(item.getBrand().toLowerCase().indexOf(nv.toLowerCase()) != -1) {
					return true;
				}
				else if(item.getCategory().toLowerCase().indexOf(nv.toLowerCase()) != -1) {
					return true;
				}
				else {
					return false;
				}
			});
		}); 
		SortedList<SalesModel> sortedData = new SortedList<>(filteredList);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		
		return sortedData;
	}
	
	// Update the stock table with the selected quantity to cart
	public static void deductFromStockAndUpdate(String id, int qty) throws SQLException {
		String sql = "UPDATE stock SET quantity=(SELECT (quantity - ?)FROM stock WHERE itemId=?) WHERE itemId=?;";
		Connection conn = null;
		PreparedStatement ps = null;		
		try {
			conn = DBConnection.connect();
			ps = conn.prepareStatement(sql);			
			ps.setInt(1, qty);
			ps.setString(2, id);
			ps.setString(3, id);
			ps.executeUpdate();
			conn.commit();
			data.removeAll(data);
			SalesItemsTable.setData();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			ps.close();
			conn.close();
		}
	}
	
	
	// Update the stock table with the selected quantity from cart
	public static void deductFromCartAndUpdate(String id, int qty) throws SQLException {
		String sql = "UPDATE stock SET quantity=(SELECT (quantity + ?)FROM stock WHERE itemId=?) WHERE itemId=?;";
		Connection conn = null;
		PreparedStatement ps = null;		
		try {
			conn = DBConnection.connect();
			ps = conn.prepareStatement(sql);			
			ps.setInt(1, qty);
			ps.setString(2, id);
			ps.setString(3, id);
			ps.executeUpdate();
			conn.commit();
			data.removeAll(data);
			SalesItemsTable.setData();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			ps.close();
			conn.close();
		}
	}
	
	// Check stock level	
	public static void checkStockLevel(ObservableList<SalesModel> data) {
		for (SalesModel i : data) {
			if((i.getQuantity() >= StockLevel.HIGH.number)) {
				i.getStatusBtn().setStyle("-fx-background-color: GREEN");
			}
			
			if((i.getQuantity() >= StockLevel.MEDIUM.number) && (i.getQuantity() < StockLevel.HIGH.number)) {
				i.getStatusBtn().setStyle("-fx-background-color: YELLOW");
			}
			if((i.getQuantity() >= StockLevel.LOW.number) && (i.getQuantity() < StockLevel.MEDIUM.number)) {
				i.getStatusBtn().setStyle("-fx-background-color: ORANGE");
			}
			
			if(i.getQuantity() <= StockLevel.LOW.number) {
				i.getStatusBtn().setStyle("-fx-background-color: RED");
			}
		}
	}

	// Mount item image
	public static void mountItemImage(String itemId) {
		if (!list.isEmpty()) {
			SalesPage.getImageBox().getChildren().remove(0);
		}
		list.add(0, ItemTable.readImageFromDb(itemId, "temp.jpg"));
		SalesPage.getImageBox().getChildren().add((Node)list.get(0));
	}
	
//	// Get the selected item for update
//	public static void pickAndUpdateItem() {
//		 SalesModel item = table.getSelectionModel().getSelectedItem();
//		 int quantity = 0;
//		 if (item != null) {
////			 item.
//			 System.out.println("Selected item: " + item.getId());
//			 quantity += item.getQuantity() - SalesPage.getQuantitySp().getValue();
//			 item.setQuantity(quantity);
//			 System.out.println("new quantity: " + quantity);
//			 System.out.println("quantitySp value: " + SalesPage.getQuantitySp().getValue());
//		 }else {
//			 Message.showErrorAlert("No item has been selected. "
//			 		+ "\nPlease select and item and try again!");
//			 return;
//		 }
//	}
}
