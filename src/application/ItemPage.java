package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ItemPage {
	
	private HBox itemPagePane;
	private GridPane formGridRootPane, addItemFormPane, addItemTextFieldsPane, createItemPane, addItemBtnPane, addStockFormPane, addStockBtnPane, createItemBtnPane, tablePane;
	private static TextField searchTf, idTf, nameTf, priceInTf, priceOutTf, categoryTf, brandTf, stockQtyTf, stockBrandTf, stockCategoryTf;
	private HBox searchHbox;
	private VBox tableBox;
	private static ComboBox<String> categoryCombo, brandCombo, stockIdCombo, stockNameCombo;
	private static RadioButton addRadio, updateRadio;
	private static TextField imagePathTf;
	
	ItemPage() throws SQLException{
		
		// Search bar
		Label searchLbl = new Label("Search: ");
		searchTf = new TextField();
		searchTf.setPrefWidth(200);
		searchTf.setPromptText("Search");
		
		// Create a HBox container and add search label and search text field
		searchHbox = new HBox(10);
		searchHbox.setAlignment(Pos.CENTER_LEFT);
		searchHbox.setPadding(new Insets(2,2,2,2));
		searchHbox.setId("searchBar");
		// Adding items to the search bar pane/panel
		searchHbox.getChildren().addAll(searchLbl, searchTf);
		
		// Creating VBox container for the item table
		tableBox = new VBox(10);
		tableBox.setPrefSize(880, 800);
		tableBox.setAlignment(Pos.TOP_CENTER);
		tableBox.setPadding(new Insets(10,10,10,10));
		tableBox.setId("tableBox");
		// Add table to the VBox container
		tableBox.getChildren().add(new ItemTable().getTable());
		
		// Grid pane for the items table
		tablePane = new GridPane();
		tablePane.setAlignment(Pos.TOP_LEFT);
		tablePane.setPrefSize(700, 1000);
		tablePane.setVgap(5);
		tablePane.setHgap(10);
		tablePane.setPadding(new Insets(10,10,10,10));
		tablePane.getStyleClass().add("tablePane");
		
		// Mounting the search bar
		tablePane.add(searchHbox, 0, 0);
		
		// Mounting the item table
//		tablePane.add(ItemTable.getTable(),0,1);
		tablePane.add(tableBox,0,1);
				
		// Create item grid pane
		createItemPane = new GridPane();
		createItemPane.setAlignment(Pos.CENTER);
		createItemPane.setHgap(10);
		createItemPane.setVgap(10);
		createItemPane.setPrefSize(150,220);
		createItemPane.setPadding(new Insets(5,5,5,5));
		createItemPane.getStyleClass().add("addItemTextFieldsPane");
		
		// Create item title pane
		TitledPane createItemTitledPane = new TitledPane("Create item", createItemPane);
		createItemTitledPane.setExpanded(false);
//		formGridRootPane.add(createItemTitledPane, 0, 0);
		
		// ** Create item form **//
		// Category text field
		Label categoryTfLbl = new Label("Category");
		createItemPane.add(categoryTfLbl, 0, 0);
		categoryTf = new TextField();
		categoryTf.setMaxWidth(200);
		createItemPane.add(categoryTf, 1, 0);
		// Brand text field
		Label brandTfLbl = new Label("Brand");
		createItemPane.add(brandTfLbl, 0, 1);
		brandTf = new TextField();
		brandTf.setMaxWidth(200);
		createItemPane.add(brandTf, 1, 1);
		
		// Button pane
		createItemBtnPane = new GridPane();
		createItemBtnPane.setAlignment(Pos.CENTER);
		createItemBtnPane.setHgap(10);
		createItemBtnPane.setVgap(10);
		createItemBtnPane.setMinSize(250, 90);
		createItemBtnPane.setPadding(new Insets(5,0,5,2));
		createItemBtnPane.getStyleClass().add("fg-addItemBtnPane");
		
		// Add item button
		Button addBtn = new Button("Create");
		createItemBtnPane.add(addBtn, 0, 0);
		addBtn.setId("fg-addItemBtn");
		addBtn.setOnAction(event -> {
			if (!getCategoryTf().getText().isEmpty() && !getBrandTf().getText().isEmpty()) {
				try {
					createCategory();
					categoryCombo.getItems().add(categoryTf.getText());
					createBrand();					
					brandCombo.getItems().add(brandTf.getText());
					clearCreateItemInputs();
					Message.showInfoAlert("Successful!");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else if (!getCategoryTf().getText().isEmpty() && getBrandTf().getText().isEmpty()) {
				try {
					createCategory();
					categoryCombo.getItems().add(categoryTf.getText());
					clearCreateItemInputs();
					Message.showInfoAlert("Successful!");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (getCategoryTf().getText().isEmpty() && !getBrandTf().getText().isEmpty()) {
				try {
					createBrand();
					brandCombo.getItems().add(brandTf.getText());
					clearCreateItemInputs();
					Message.showInfoAlert("Successful!");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				Message.showErrorAlert("You didn't enter any input!. "
						+ "\nPlease enter input into the text field and try again.");
			}
		});
				
		// Update button
		Button udateBtn1 = new Button("Update");
		createItemBtnPane.add(udateBtn1, 1, 0);
		udateBtn1.setId("fg-updateItemBtn");
		
		// Update button
		Button clearBtn1 = new Button("Clear");
		createItemBtnPane.add(clearBtn1, 0, 1);
		clearBtn1.setId("fg-clearBtn");
		clearBtn1.setOnAction(event -> clearCreateItemInputs());
		
		// Delete item button
		Button deleteBtn1 = new Button("Delete");
		createItemBtnPane.add(deleteBtn1, 1, 1);
		deleteBtn1.setId("fg-removeItemBtn");
		deleteBtn1.setOnAction(event -> {			
			try {
				Alert alert = new Alert(AlertType.CONFIRMATION.CONFIRMATION,"hello" );
				alert.setContentText("Are you sure you want to delete the selected item from the stock list?");
				alert.show();
				ItemTable.deleteRecord();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		// Mounting the button panel
		createItemPane.add(createItemBtnPane, 0, 3, 2, 1);
		
		// instantiate the form Grid pane
		addItemFormPane = new GridPane();
		addItemFormPane.setAlignment(Pos.CENTER);
		addItemFormPane.setHgap(10);
		addItemFormPane.setVgap(10);
		addItemFormPane.setPrefSize(400, 300);
		addItemFormPane.setPadding(new Insets(10,5,5,5));
		addItemFormPane.getStyleClass().add("addItemTextFieldsPane");
				
		// Create item title pane for radioPane, addItem pane and buttonPane
		TitledPane addItemTitledPane = new TitledPane("Add new item", addItemFormPane);
		addItemTitledPane.setExpanded(false);
//		formGridRootPane.add(addItemTitledPane, 0, 1);
		
		// Radio button pane
		HBox radioPane = new HBox(10);
		radioPane.setAlignment(Pos.CENTER);
			
		// Toggle group
		final ToggleGroup tg = new ToggleGroup();
		tg.selectedToggleProperty().addListener((ob, ov, nv) -> {
			if(tg.getSelectedToggle() != null) {
//				if(nv.getUserData().equals("updateItem")) {
//					idTf.setEditable(false);
//				}else if(nv.getUserData().equals("addItem")){
//					idTf.setEditable(true);
//				}
			}
		});
				
		// Initialising the radio buttons
		addRadio = new RadioButton("Add item");
		addRadio.setUserData("addItem");
		addRadio.setSelected(true);
		
		updateRadio = new RadioButton("Update item");
		updateRadio.setUserData("updateItem");

		// adding to togglegroup
		addRadio.setToggleGroup(tg);
		updateRadio.setToggleGroup(tg);
		
		// Adding the radio buttons to the radio pane
		radioPane.getChildren().addAll(addRadio, updateRadio);

		// Add item form pane
		addItemTextFieldsPane = new GridPane();
		addItemTextFieldsPane.setAlignment(Pos.CENTER);
		addItemTextFieldsPane.setHgap(10);
		addItemTextFieldsPane.setVgap(10);
		addItemTextFieldsPane.setPrefSize(400, 300);
		addItemTextFieldsPane.setPadding(new Insets(5,5,5,5));
		addItemTextFieldsPane.getStyleClass().add("addItemTextFieldsPane");
		
		// Category combobox
		Label categoryComboLbl = new Label("Category");
		addItemTextFieldsPane.add(categoryComboLbl, 0, 0);
		categoryCombo = new ComboBox<String>(ItemCategory.getList());
		categoryCombo.setPromptText("--Select--");
		categoryCombo.setEditable(true);
		categoryCombo.setVisibleRowCount(5);
		categoryCombo.setMinWidth(210);
		addItemTextFieldsPane.add(categoryCombo, 1, 0, 3, 1);
		categoryCombo.valueProperty().addListener((ov, t, name) -> {
//			System.out.println(categoryCombo.getValue());
//			ItemCategory.setName(categoryCombo.getValue());
		});
		
		// Brand combobox
		Label brandComboLbl = new Label("Brand");
		addItemTextFieldsPane.add(brandComboLbl, 0, 1);
		brandCombo = new ComboBox<String>(ItemBrand.getList());
		brandCombo.setPromptText("--Select--");
		brandCombo.setVisibleRowCount(5);
		brandCombo.setMinWidth(210);
		brandCombo.setEditable(true);
		brandCombo.valueProperty().addListener((ov, t, name) -> {
//			System.out.println(brandCombo.getValue());
//			ItemBrand.setName(brandCombo.getValue());
		});
		addItemTextFieldsPane.add(brandCombo, 1, 1, 3, 1);
		
		// Id text field
		Label idTfLbl = new Label("Id");
		addItemTextFieldsPane.add(idTfLbl, 0, 2);
		idTf = new TextField();
		idTf.setMaxWidth(70);
		addItemTextFieldsPane.add(idTf, 1, 2);
		
		// Name text field
		Label nameTfLbl = new Label("Name");
		addItemTextFieldsPane.add(nameTfLbl, 0, 3);
		nameTf = new TextField();
		nameTf.setMaxWidth(210);
		addItemTextFieldsPane.add(nameTf, 1, 3, 3, 1);
		
		// PriceIn or buying price text field
		Label priceInTfLbl = new Label("Price In");
		addItemTextFieldsPane.add(priceInTfLbl, 0, 4);
		priceInTf = new TextField();
		priceInTf.setMaxWidth(70);
		addItemTextFieldsPane.add(priceInTf, 1, 4);
		
		// PriceOut or selling price text field
		Label priceOutTfLbl = new Label("Price Out");
		addItemTextFieldsPane.add(priceOutTfLbl, 2, 4);
		priceOutTf = new TextField();
		priceOutTf.setMaxWidth(70);
		addItemTextFieldsPane.add(priceOutTf, 3, 4);		
		
		// Selected image file path
		imagePathTf = new TextField();
		imagePathTf.setMaxWidth(200);
		imagePathTf.setEditable(false);
		imagePathTf.setPromptText("Select image");
		addItemTextFieldsPane.add(imagePathTf, 0, 5, 3, 1);
		
		// Browse for image button
		Button browse = new Button("Browse");
		browse.setPrefWidth(70);
		addItemTextFieldsPane.add(browse, 3, 5);
		browse.setOnAction(event -> imagePathTf.setText(CommonSource.selectImageFile()));
				
		// Button pane
		addItemBtnPane = new GridPane();
		addItemBtnPane.setAlignment(Pos.CENTER);
		addItemBtnPane.setHgap(10);
		addItemBtnPane.setVgap(10);
		addItemBtnPane.setMinSize(140, 90);
		addItemBtnPane.setPadding(new Insets(5,0,5,2));
		addItemBtnPane.getStyleClass().add("fg-addItemBtnPane");
		
		// Add item button
		Button addItemBtn = new Button("Add Item");
		addItemBtnPane.add(addItemBtn, 0, 0);
		addItemBtn.setId("fg-addItemBtn");
		addItemBtn.setOnAction(event -> {
			try {
				addItem();
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
		});
				
		// Update button
		Button updateBtn2 = new Button("Update");
		addItemBtnPane.add(updateBtn2, 1, 0);
		updateBtn2.setId("fg-updateItemBtn");
		updateBtn2.setOnAction(event -> {
			try {
//				updateItem();
				ItemTable.readImageFromDb("TO23", "D:\\temp");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// Update button
		Button clearBtn2 = new Button("Clear");
		addItemBtnPane.add(clearBtn2, 0, 1);
		clearBtn2.setId("fg-clearBtn");
		clearBtn2.setOnAction(event -> clearAddItemInputs());
		
		// Delete item button
		Button deleteBtn2 = new Button("Delete");
		addItemBtnPane.add(deleteBtn2, 1, 1);
		deleteBtn2.setId("fg-removeItemBtn");
		deleteBtn2.setOnAction(event -> {			
			try {
				ItemTable.deleteRecord();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
	//	addItemTextFieldsPane.add(radioPane, 1, 5, 3, 1);
		
		// Adding the button pane to the form grid pane
//		addItemFormPane.getChildren().addAll(radioPane, addItemTextFieldsPane, addItemBtnPane);
		addItemFormPane.add(radioPane, 0, 0, 2, 1);	
		addItemFormPane.add(addItemTextFieldsPane, 0, 1);
		addItemFormPane.add(addItemBtnPane, 0, 2, 2, 1);
		
		
		//*****************start of add stock form*************************
			
	    // Add stock form pane
		addStockFormPane = new GridPane();
		addStockFormPane.setAlignment(Pos.CENTER);
		addStockFormPane.setHgap(10);
		addStockFormPane.setVgap(10);
		addStockFormPane.setPrefSize(400, 300);
		addStockFormPane.setPadding(new Insets(10,5,5,5));
		addStockFormPane.getStyleClass().add("addItemTextFieldsPane");
		
        // Create stock title pane for radioPane, addStock pane and buttonPane
		TitledPane addStockTitledPane = new TitledPane("Add stock", addStockFormPane);
		addStockTitledPane.setExpanded(false);
//		formGridRootPane.add(addStockTitledPane, 0, 2);

		
		// Name text field
		Label stockNameTfLbl = new Label("Item Name");
		addStockFormPane.add(stockNameTfLbl, 0, 0);
		stockNameCombo = new ComboBox<String>(CommonSource.getTableColumnData("SELECT name FROM item;", "name"));
		stockNameCombo.setPromptText("--Select--");
		stockNameCombo.setEditable(true);
		stockNameCombo.setVisibleRowCount(8);
		stockNameCombo.setMaxWidth(210);
		//adding the combo to the panel
		addStockFormPane.add(stockNameCombo, 1, 0);
		stockNameCombo.setOnAction(event -> {
			try {
				ItemTable.autoFillComboName();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		
		// Id text field
		Label stockIdTfLbl = new Label("Id");
		addStockFormPane.add(stockIdTfLbl, 0, 1);
		stockIdCombo = new ComboBox<String>(CommonSource.getTableColumnData("SELECT id FROM item;", "id"));
		stockIdCombo.setPromptText("--Select--");
		stockIdCombo.setEditable(true);
		stockIdCombo.setVisibleRowCount(8);
		stockIdCombo.setMaxWidth(80);
		addStockFormPane.add(stockIdCombo, 1, 1);
		stockIdCombo.setOnAction(event -> {
			try {
				ItemTable.autoFillComboId();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		// Stock quantity text field
		Label stockQtyTfLbl = new Label("Quantity");
		addStockFormPane.add(stockQtyTfLbl, 0, 2);
		stockQtyTf = new TextField();
		stockQtyTf.setMaxWidth(80);
		addStockFormPane.add(stockQtyTf, 1, 2);
		
		// Stock brand text field
		Label stockBrandTfLbl = new Label("Brand");
		addStockFormPane.add(stockBrandTfLbl, 0, 3);
		stockBrandTf = new TextField();
		stockBrandTf.setEditable(false);
		stockBrandTf.setMaxWidth(200);
		addStockFormPane.add(stockBrandTf, 1, 3);
		
		// Stock category text field
		Label stockCategoryTfLbl = new Label("Category");
		addStockFormPane.add(stockCategoryTfLbl, 0, 4);
		stockCategoryTf = new TextField();
		stockCategoryTf.setEditable(false);
		stockCategoryTf.setMaxWidth(200);
		addStockFormPane.add(stockCategoryTf, 1, 4);
		
		// Button pane
		addStockBtnPane = new GridPane();
		addStockBtnPane.setAlignment(Pos.CENTER);
		addStockBtnPane.setHgap(10);
		addStockBtnPane.setVgap(10);
		addStockBtnPane.setMinSize(140, 90);
		addStockBtnPane.setPadding(new Insets(5,0,5,2));
		addStockBtnPane.getStyleClass().add("fg-addStockBtnPane");
		
		// Add stock button
		Button addStockBtn = new Button("Add stock");
		addStockBtnPane.add(addStockBtn, 0, 0);
		addStockBtn.setId("fg-addItemBtn");
		addStockBtn.setOnAction(event -> {
			try {
				StockTable.addToStock();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
				
		// Update stock button
		Button updateStockBtn = new Button("Update stock");
		addStockBtnPane.add(updateStockBtn, 1, 0);
		updateStockBtn.setId("fg-updateItemBtn");
		updateStockBtn.setOnAction(event -> {
			try {
				updateStockItemQuantity();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		// Clear button
		Button clearStockBtn = new Button("Clear");
		addStockBtnPane.add(clearStockBtn, 0, 1);
		clearStockBtn.setId("fg-clearBtn");
		clearStockBtn.setOnAction(event -> clearAddStockInputs());
		
		// Delete stock button
		Button deleteStockBtn = new Button("Delete");
		addStockBtnPane.add(deleteStockBtn, 1, 1);
		deleteStockBtn.setId("fg-removeItemBtn");
		deleteStockBtn.setOnAction(event -> {			
			
		});
		
		// Adding the button pane 
		addStockFormPane.add(addStockBtnPane, 0, 6, 2, 1);
				
		//*****************end of add stock form***************************
		
		// According
		Accordion accordion = new Accordion();
		accordion.getPanes().add(createItemTitledPane);
		accordion.getPanes().add(addItemTitledPane);
		accordion.getPanes().add(addStockTitledPane);
		
		// Form pane
		formGridRootPane = new GridPane();
		formGridRootPane.setAlignment(Pos.TOP_CENTER);
		formGridRootPane.setHgap(10);
		formGridRootPane.setVgap(2);
		formGridRootPane.setMaxSize(450, 600);
		formGridRootPane.setPadding(new Insets(5,5,5,5));
		formGridRootPane.setId("formGridPane2");
		
		// Adding the accordion container to the form root grid
		formGridRootPane.add(accordion, 0, 0);
		
//		VBox vb = new VBox();
//		vb.getChildren().add(ItemTable.readImageFromDb("TO23", "temp.jpg"));
//		formGridRootPane.add(vb, 0, 1);
		
		// Main or parent pane
		itemPagePane = new HBox(10);
		itemPagePane.setPrefSize(940, 800);
		itemPagePane.setId("itemPagePane");
		itemPagePane.getChildren().addAll(formGridRootPane, tablePane);
	}
	
	
	// Add items to stock list
	public void addItem() throws NumberFormatException, SQLException {
		if(isAllInputsFilled() == true) {
			ItemTable.addItem(
					idTf.getText().toUpperCase(), 
					nameTf.getText(),
					brandCombo.getValue(),
					categoryCombo.getValue(),
					Double.valueOf(priceInTf.getText()), 
					Double.valueOf(priceOutTf.getText()));
		}else {
			Message.showErrorAlert("Could not add the item."
					+ "\nEither some input fields are empty or brand and category values are not correct."
					+ " \nPlease check and try again.");
			return;
		}
		
	}
	
	
	// Get the item table pane
	public VBox getItemTablePane() {
		return tableBox;
	}
	
////	 Set the item table pane OR refresh the item table
//	public void setItemTablePane() throws SQLException {
//		tablePane.add(ItemTable.getTable(),0,1);
//	}

	// Create brand
	public static void createBrand() throws SQLException {
		String sql = "INSERT INTO brand(name) VALUES(?);";
		Connection conn = null;
		PreparedStatement ps = null;	
		try {
			conn = DBConnection.connect();
			ps = conn.prepareStatement(sql);
			ps.setString(1, getBrandTf().getText());
			ps.executeUpdate();
			conn.commit();
		}catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		finally {
			ps.close();
			conn.close();
		}
	}
		
	// Create category
	public static void createCategory() throws SQLException {
		String sql = "INSERT INTO category(name)VALUES(?);";
		Connection conn = null;
		PreparedStatement ps = null;		
		try {
			conn = DBConnection.connect();
			ps = conn.prepareStatement(sql);
			ps.setString(1, getCategoryTf().getText());
			ps.executeUpdate();
			conn.commit();
		}catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		finally {
			ps.close();
			conn.close();
		}
	}
	
	// Update items 
	public void updateItem() throws SQLException {
		String sql = "UPDATE item SET name=?, brand=?, category=?, priceIn=?, priceOut=? WHERE id=?;";
		Connection conn = null;
		PreparedStatement ps = null;
		if(isAllInputsFilled() == true) {
			try {
				conn = DBConnection.connect();
				ps = conn.prepareStatement(sql);
				ps.setString(1, getNameTf().getText());
				ps.setString(2, getBrandCombo().getEditor().getText());
				ps.setString(3, getCategoryCombo().getEditor().getText());				
				ps.setDouble(4, Double.valueOf(getPriceInTf().getText()));
				ps.setDouble(5, Double.valueOf(getPriceOutTf().getText()));
				ps.setString(6, getIdTf().getText());
				ps.executeUpdate();
				conn.commit();
				ItemTable.getData().removeAll(ItemTable.getData());
				ItemTable.setData();
				clearAddItemInputs();
				Message.showInfoAlert("Update successful.");
			}catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
				Message.showErrorAlert("Could not perform the update! Please try again.");
			}
			finally {
				ps.close();
				conn.close();
			}
		}
		else {
		Message.showErrorAlert("Could not update the item."
				+ "\nEither some input fields are empty or brand and category values are not correct."
				+ " \nPlease check and try again.");
		return;
		}
	}
	
	
	// Update stock item quantity
	public void updateStockItemQuantity() throws SQLException {
		String sql = "UPDATE stock SET quantity=(SELECT (quantity + ?) FROM stock WHERE itemId=?) WHERE itemId=?;";
		Connection conn = null;
		PreparedStatement ps = null;
		if(!stockIdCombo.getEditor().getText().isEmpty()) {
			try {
				conn = DBConnection.connect();
				ps = conn.prepareStatement(sql);
				ps.setInt(1, Integer.valueOf(stockQtyTf.getText()));
				ps.setString(2, stockIdCombo.getEditor().getText());
				ps.setString(3, stockIdCombo.getEditor().getText());
				ps.executeUpdate();
				conn.commit();
//				ItemTable.getData().removeAll(ItemTable.getData());
				SalesItemsTable.getData().clear();
				SalesItemsTable.setData();
				clearAddStockInputs();
				Message.showInfoAlert("Update successful.");
			}catch(Exception e) {
				conn.rollback();
				System.out.println("Error: " + e.getMessage());
				Message.showErrorAlert("Could not perform the update! Please try again.");
			}
			finally {
				if(ps != null) {
					ps.close();
				}
				if(conn != null){
					conn.close();
				}
			}
		}
		else {
			Message.showWarningAlert("Wrong! Please select an item.");
			return;
		}
	}
	
	
	// Check input fields
	public static boolean isAllInputsFilled() throws SQLException {
		if(!idTf.getText().isEmpty() 
				&& !nameTf.getText().isEmpty()
				&& !priceInTf.getText().isEmpty()
				&& !priceOutTf.getText().isEmpty() 
				&& checkComboList(categoryCombo, ItemCategory.getList()) == true
				&& checkComboList(brandCombo, ItemBrand.getList()) == true) {
			return true;
		}
		return false;
	}

	
	// Clear the input fields of the create item form
	public void clearCreateItemInputs() {
		if (!categoryTf.getText().isEmpty() || !brandTf.getText().isEmpty()) {
			categoryTf.clear();
			brandTf.clear();
		}
		else {
			return;
		}
	}
	
	// Clear the add item form fields
	public static void clearAddItemInputs() {
		if (!idTf.getText().isEmpty() 
				|| !nameTf.getText().isEmpty() 
				|| !priceInTf.getText().isEmpty() 
				|| !priceOutTf.getText().isEmpty()
				|| !imagePathTf.getText().isEmpty()) {
			
			categoryCombo.getEditor().setText("--Select--");
			brandCombo.getEditor().setText("--Select--");
			idTf.clear();
			nameTf.clear();
			priceInTf.clear();
			priceOutTf.clear();
			imagePathTf.clear();
		}
		else {
			return;
		}
	}
	
	// Clear add stock form inputs
	public static void clearAddStockInputs() {
		if (!stockBrandTf.getText().isEmpty() 
				|| !stockNameCombo.getEditor().getText().isEmpty() 
				|| !stockQtyTf.getText().isEmpty()
				|| !stockIdCombo.getEditor().getText().isEmpty() 
				|| !stockCategoryTf.getText().isEmpty()) {
			stockNameCombo.getEditor().setText("--Select--");
			stockIdCombo.getEditor().setText("--Select--");
			stockBrandTf.clear();
			stockCategoryTf.clear();
			stockQtyTf.setText("0");
		}
		else {
			return;
		}
	}

	// Check if the editable combobox value exists in db
	public static boolean checkComboList(ComboBox<String> cb, ObservableList<String> list) {
		String editorTxt = cb.getEditor().getText();
		for (String i : list) {
			if (i.equals(editorTxt))
			return true;
		}
		return false;
	}
	
	
	// Update item image
	public static void saveImage() throws SQLException, IOException {
		String sql = "INSERT INTO stock (image) VALUES (?) WHERE itemId=?;";
//		FileInputStream fs = null;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnection.connect();
			if (!imagePathTf.getText().isEmpty()) {
				ps = conn.prepareStatement(sql);
				ps.setBytes(1, CommonSource.readImageFile(imagePathTf.getText()));
				ps.setString(2, idTf.getText());
				ps.executeUpdate();
				conn.commit();
			}
		}catch(Exception e) {
			e.printStackTrace();
			Message.showErrorAlert("Could not upload the image!");
		}finally {
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
	}
	
	// Update item image
	public static void updateImage() throws SQLException, IOException {
		String sql = "UPDATE stock SET image=? WHERE itemId=?;";
		FileInputStream fs = null;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnection.connect();
			if (!imagePathTf.getText().isEmpty()) {
				ps = conn.prepareStatement(sql);
				ps.setBytes(1, CommonSource.readImageFile(imagePathTf.getText()));
				ps.setString(2, idTf.getText());
				ps.executeUpdate();
				conn.commit();
			}
		}catch(Exception e) {
			e.printStackTrace();
			Message.showErrorAlert("Could not upload the image!");
		}finally {
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
			if(fs != null) {
				fs.close();
			}
		}
	}
	
	
	//**** Getters and setters ****//
	
// Returns the parent pane
	public HBox getPane() {
		return itemPagePane;
	}
	
	public static TextField getSearchTf() {
		return searchTf;
	}

	public static void setSearchTf(TextField searchTf) {
		ItemPage.searchTf = searchTf;
	}

	public static RadioButton getAddRadio() {
		return addRadio;
	}
	
	public static void setAddRadio(RadioButton addRadio) {
		ItemPage.addRadio = addRadio;
	}
	
	public static RadioButton getUpdateRadio() {
		return updateRadio;
	}
	
	public static void setUpdateRadio(RadioButton updateRadio) {
		ItemPage.updateRadio = updateRadio;
	}
	
	public static ComboBox<String> getCategoryCombo() {
		return categoryCombo;
	}
	
	public static void setCategoryCombo(ComboBox<String> categoryCombo) {
		ItemPage.categoryCombo = categoryCombo;
	}
	
	public static ComboBox<String> getBrandCombo() {
		return brandCombo;
	}
	
	public static void setBrandCombo(ComboBox<String> brandCombo) {
		ItemPage.brandCombo = brandCombo;
	}
	
	public static TextField getCategoryTf() {
		return categoryTf;
	}
	
	public static void setCategoryTf(TextField categoryTf) {
		ItemPage.categoryTf = categoryTf;
	}
	
	public static TextField getBrandTf() {
		return brandTf;
	}
	
	public static void setBrandTf(TextField brandTf) {
		ItemPage.brandTf = brandTf;
	}
	
	public static TextField getIdTf() {
		return idTf;
	}
	
	public static void setIdTf(TextField idTf) {
		ItemPage.idTf = idTf;
	}
	
	public static TextField getNameTf() {
		return nameTf;
	}
	
	public static void setNameTf(TextField nameTf) {
		ItemPage.nameTf = nameTf;
	}
	
	public static TextField getPriceInTf() {
		return priceInTf;
	}
	
	public static void setPriceInTf(TextField priceInTf) {
		ItemPage.priceInTf = priceInTf;
	}
	
	public static TextField getPriceOutTf() {
		return priceOutTf;
	}
	
	public static void setPriceOutTf(TextField priceOutTf) {
		ItemPage.priceOutTf = priceOutTf;
	}


	public static ComboBox<String> getStockIdCombo() {
		return stockIdCombo;
	}


	public static void setStockIdCombo(ComboBox<String> stockIdCombo) {
		ItemPage.stockIdCombo = stockIdCombo;
	}


	public static ComboBox<String> getStockNameCombo() {
		return stockNameCombo;
	}


	public static void setStockNameCombo(ComboBox<String> stockNameCombo) {
		ItemPage.stockNameCombo = stockNameCombo;
	}


	public static TextField getStockQtyTf() {
		return stockQtyTf;
	}


	public static void setStockQtyTf(TextField stockQtyTf) {
		ItemPage.stockQtyTf = stockQtyTf;
	}


	public static TextField getStockBrandTf() {
		return stockBrandTf;
	}


	public static void setStockBrandTf(TextField stockBrandTf) {
		ItemPage.stockBrandTf = stockBrandTf;
	}


	public static TextField getStockCategoryTf() {
		return stockCategoryTf;
	}


	public static void setStockCategoryTf(TextField stockCategoryTf) {
		ItemPage.stockCategoryTf = stockCategoryTf;
	}


	public static TextField getImagePathTf() {
		return imagePathTf;
	}


	public static void setImagePathTf(TextField imagePathTf) {
		ItemPage.imagePathTf = imagePathTf;
	}

}
