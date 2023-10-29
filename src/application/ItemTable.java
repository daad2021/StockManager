package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ItemTable extends VBox{
	
	private static TableView<Item> table;
	private TableColumn<Item, String> idCol;
	private TableColumn<Item, String> nameCol, brandNameCol, categoryCol;
//	private TableColumn<Item, byte[]> imageCol;
	private TableColumn<Item, Double> priceInCol;
	private TableColumn<Item, Double> priceOutCol;
	private TableColumn<Item, Double> profitMarginCol;
	private TableColumn<Item, String> availabilityCol;
	private static ObservableList<Item> data;
	private static String names;
	private static ObservableList<String> itemNames;
	private static String seletedItemId;
	
	public ItemTable() throws SQLException {
		data = FXCollections.observableArrayList();
		// Initialising the data
		setData();
		
		//
		table = new TableView<Item>();
		idCol = new TableColumn<>("Id");
		nameCol = new TableColumn<>("Item Name");
		brandNameCol = new TableColumn<>("Brand");
		categoryCol = new TableColumn<>("Category");
//		imageCol  = new TableColumn<>("Image");
		priceInCol = new TableColumn<>("Price In");
		priceOutCol = new TableColumn<>("Price Out");
		profitMarginCol = new TableColumn<>("Profit Margin");
		availabilityCol = new TableColumn<>("Availability");
		
		// 
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		brandNameCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
//		imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
		priceInCol.setCellValueFactory(new PropertyValueFactory<>("priceIn"));
		priceOutCol.setCellValueFactory(new PropertyValueFactory<>("priceOut"));
		profitMarginCol.setCellValueFactory(new PropertyValueFactory<>("profitMargin"));
		
		table.getColumns().setAll(idCol, nameCol, brandNameCol, categoryCol, priceInCol, priceOutCol, profitMarginCol);
		table.setMinHeight(520);
		table.setItems(getFilteredData(getData()));
		
		// Adding selection listener to the items table
		table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null && ItemPage.getUpdateRadio().isSelected() == true) {
            	ItemPage.getNameTf().setText(newValue.getName());
            	ItemPage.getBrandCombo().getEditor().setText(newValue.getBrand());
            	ItemPage.getCategoryCombo().getEditor().setText(newValue.getCategory());				
				ItemPage.getIdTf().setText(newValue.getId());
				ItemPage.getPriceInTf().setText(String.valueOf(newValue.getPriceIn()));
				ItemPage.getPriceOutTf().setText(String.valueOf(newValue.getPriceOut()));
				ItemPage.getIdTf().setEditable(false);
            }
//            else {
//            	Message.showInfoAlert("Please select the \"Update item\" radio button first.");
//            	return;
//            }
        });	
	}
	
	public static TableView<Item> getTable() {
		return table;
	}

	public static void setTable(TableView<Item> table) {
		ItemTable.table = table;
	}

	//
	public static String getSeletedItemId() {
		return seletedItemId;
	}

	public static void setSeletedItemId(String itemId) {
		seletedItemId = itemId;
	}
	
	public static String getName() {
		return names;
	}

	public static void setName(String name) {
		names = name;
	}

	// Getting the records
	public static ObservableList<Item> getData(){
		return data;
	}
	
//	// Setting the list items
//	public void setData(ObservableList<Item> data) {
//		this.data = data;
//	}
		
	// Get the data from DB and add to the observable list
	public static void setData() throws SQLException {
//		data = FXCollections.observableArrayList();
		
		String qry = "SELECT * FROM item;";		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;		
		try {			
			conn = DBConnection.connect();
			st = conn.createStatement();			
			rs = st.executeQuery(qry);
			while(rs.next()) {
				data.add(new Item(
						rs.getString("id"), 
						rs.getString("name"),
						rs.getString("brand"),
						rs.getString("category"),
						rs.getDouble("priceIn"), 
						rs.getDouble("priceOut"),
						rs.getDouble("profitMargin")));
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			rs.close();
			st.close();
			conn.close();
		}	
	}
	
	
//	// Get the item names from DB and add to the observable list
//	public static ObservableList<String> getItemNames() throws SQLException {
//		itemNames = FXCollections.observableArrayList();		
//		String qry = "SELECT name FROM item;";		
//		Connection conn = null;
//		Statement st = null;
//		ResultSet rs = null;		
//		try {			
//			conn = DBConnection.connect();
//			st = conn.createStatement();			
//			rs = st.executeQuery(qry);
//			while(rs.next()) {
//				System.out.println(rs.getString("name"));
//				itemNames.add(rs.getString("name"));
//			}	
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		finally {
//			rs.close();
//			st.close();
//			conn.close();
//		}	
//		return itemNames;
//	}
		
	
	// Add items to list in DB
	public static void addItem(
			String id, 
			String name,
			String brand,
			String category,
			double priceIn, 
			double priceOut) throws SQLException {
		
		String sql = "INSERT INTO item("
				+ "id, name, brand, category, image, priceIn, priceOut, profitMargin) VALUES(?,?,?,?,?,?,?,?);";
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			double profitMargin = priceOut - priceIn;
			conn = DBConnection.connect();
			ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, name);
			ps.setString(3, brand);
			ps.setString(4, category);
			ps.setBytes (5, CommonSource.readImageFile(ItemPage.getImagePathTf().getText()));
			ps.setDouble(6, priceIn);
			ps.setDouble(7, priceOut);
			ps.setDouble(8, profitMargin);
			ps.execute();
			conn.commit();
			// Reset the table data list
			data.removeAll(data);
			ItemTable.setData();
			Message.showInfoAlert("Successful!");
			ItemPage.clearAddItemInputs();
		}catch(Exception e) {
			if (e.getMessage().contains("UNIQUE constraint failed")) {
				Message.showErrorAlert("Could not add the item! The item ID already exist.");
			}else {
				Message.showErrorAlert("Something went wrong. Please try again.");
			}			
		}
		finally {
			ps.close();
			conn.close();
		}
	}

//	// Get the item names
//	public static ObservableList<String> getItemName() throws SQLException {		
//		ObservableList<String> list = FXCollections.observableArrayList();		
//		String qry = "SELECT name FROM item;";		
//		Connection conn = null;
//		Statement st = null;
//		ResultSet rs = null;	
//		try {	
//			conn = DBConnection.connect();
//			st = conn.createStatement();			
//			rs = st.executeQuery(qry);
//			while(rs.next()) {
//				list.add(rs.getString("name")); 
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
	
//	// Get the item id
//	public static ObservableList<String> getItemId() throws SQLException {		
//		ObservableList<String> list = FXCollections.observableArrayList();		
//		String qry = "SELECT id FROM item;";		
//		Connection conn = null;
//		Statement st = null;
//		ResultSet rs = null;	
//		try {	
//			conn = DBConnection.connect();
//			st = conn.createStatement();			
//			rs = st.executeQuery(qry);
//			while(rs.next()) {
//				list.add(rs.getString("id")); 
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
	

	// Delete an item from item list in db
	public static void deleteRecord() throws SQLException {
		String sql = "DELETE FROM item WHERE id=?;";
		Connection conn = null;
		PreparedStatement ps = null;
		// Picking the selected item id from the UI for deletion in DB
		Item item = table.getSelectionModel().getSelectedItem();
		if (item != null) {
			try {
				conn = DBConnection.connect();
				ps = conn.prepareStatement(sql);
				ps.setString(1, item.getId());
				ps.executeUpdate();
				conn.commit();
			}catch(Exception e) {
				System.out.println("Could not delete. : " + e.getMessage());
				e.printStackTrace();
			}finally {
				ps.close();
				conn.close();
			}
			// Deleting the selected item in UI/refreshing the UI
			table.getItems().remove(item);
			table.getSelectionModel().clearSelection();
		}else {
			Message.showErrorAlert("No item has been selected for removal. "
			 		+ "\nPlease select an item and try again!");
			return;
		}
	}
	
	// Filter table records for search bar
	public SortedList<Item> getFilteredData(ObservableList<Item> data){
		FilteredList<Item> filteredList = new FilteredList<>(data, b -> true);
		ItemPage.getSearchTf().textProperty().addListener((obs, ov, nv) -> {
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
		SortedList<Item> sortedData = new SortedList<>(filteredList);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		
		return sortedData;
	}
	
	// Auto fill combo
	public static void autoFillComboId() throws SQLException {				
		String qry = "SELECT id, name, brand, category FROM item WHERE id=?;";	
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;	
		try {	
			conn = DBConnection.connect();
			if(ItemPage.getStockIdCombo().getSelectionModel().getSelectedItem() != null) {
				ps = conn.prepareStatement(qry);
				ps.setString(1, ItemPage.getStockIdCombo().getSelectionModel().getSelectedItem());
				rs = ps.executeQuery();
				while(rs.next()) {
					ItemPage.getStockNameCombo().getEditor().setText(rs.getString("name"));
					ItemPage.getStockBrandTf().setText(rs.getString("brand"));
					ItemPage.getStockCategoryTf().setText(rs.getString("category"));
				}
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
	
	// Auto fill combo
	public static void autoFillComboName() throws SQLException {				
		String qry = "SELECT id, name, brand, category FROM item WHERE name=?;";	
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;	
		try {	
			conn = DBConnection.connect();
			if(ItemPage.getStockNameCombo().getSelectionModel().getSelectedItem() != null) {
				ps = conn.prepareStatement(qry);
				ps.setString(1, ItemPage.getStockNameCombo().getSelectionModel().getSelectedItem());
				rs = ps.executeQuery();
				while(rs.next()) {
					ItemPage.getStockIdCombo().getEditor().setText(rs.getString("id"));
					ItemPage.getStockBrandTf().setText(rs.getString("brand"));
					ItemPage.getStockCategoryTf().setText(rs.getString("category"));
				}
			}
		}catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
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
	
//	// Auto fill combo
//	public static void autoFillCombo(ComboBox<String> inCombo, ComboBox<String> outCombo, String inCol, String outCol) throws SQLException {				
//		String qry = "SELECT id, name FROM item WHERE" + inCol + "=?;";	
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;	
//		try {	
//			conn = DBConnection.connect();
//			ps = conn.prepareStatement(qry);
//			ps.setString(1, inCombo.getSelectionModel().getSelectedItem());
//			rs = ps.executeQuery();
//			while(rs.next()) {
//				outCombo.getEditor().setText(rs.getString(outCol)); 
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		finally {
//			rs.close();
//			ps.close();
//			conn.close();
//		}
//	}
	
	// Auto fill combo
	public static void autoFillCombo() throws SQLException {				
		String qry1 = "SELECT id, name FROM item WHERE id=?;";
		String qry2 = "SELECT id, name FROM item WHERE name=?;";	
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;	
		try {	
			conn = DBConnection.connect();
			if(ItemPage.getStockIdCombo().getSelectionModel().getSelectedItem() != null) {
				ps = conn.prepareStatement(qry1);
				ps.setString(1, ItemPage.getStockIdCombo().getSelectionModel().getSelectedItem());
				rs = ps.executeQuery();
				while(rs.next()) {
					ItemPage.getStockNameCombo().getEditor().setText(rs.getString("name")); 
				}
			}else if(ItemPage.getStockNameCombo().getSelectionModel().getSelectedItem() != null) {
				ps = conn.prepareStatement(qry2);
				ps.setString(1, ItemPage.getStockNameCombo().getSelectionModel().getSelectedItem());
				rs = ps.executeQuery();
				while(rs.next()) {
					ItemPage.getStockIdCombo().getEditor().setText(rs.getString("id")); 
				}
			}else {
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			rs.close();
			ps.close();
			conn.close();
		}
	}
	
	
	// Read image file from DB
    public static ImageView readImageFromDb(String id, String filename) {
        String qry = "SELECT image FROM item WHERE id=?";
        ResultSet rs = null;
        FileOutputStream fos = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ImageView imageView = null;
        InputStream input = null;
        try {
            conn = DBConnection.connect();
            ps = conn.prepareStatement(qry);
            ps.setString(1, id);
            rs = ps.executeQuery();

            // Write the binary stream into file
            File tempFile = new File(filename);

            fos = new FileOutputStream(tempFile);
           
            while (rs.next()) {
                input = rs.getBinaryStream("image");
                if(input != null) {
	                byte[] buffer = new byte[1024];
	                while (input.read(buffer) > 0) {
	                    fos.write(buffer);
	                }
                }
            }
            Image image = new Image(tempFile.toURI().toURL().toString());
            imageView = new ImageView(image);		
    		imageView.setFitHeight(148); 
    	    imageView.setFitWidth(148);
        } catch (SQLException | IOException e) {
        	System.out.println("Could not read the image");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }

                if (conn != null) {
                    conn.close();
                }
                if (fos != null) {
                    fos.close();
                }

            } catch (SQLException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return imageView;
    }
	
	
}
