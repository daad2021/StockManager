package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SalesPage {
	
	private GridPane salesPane, topPane, formGridPane, bottomPane,summaryPane;
	private HBox searchBox;
	private static VBox imageBox;
	private static TextField searchTf, categoryTf, brandTf, nameTf, amountTf, idTf, unitPriceTf;
	private static Label totalAmountLbl,invoiceTotalLbl, changeAmountLbl, cartLabel;
	private static Label totalQtyLbl, uniqueItemsLbl, currentTotalSalesLbl, currentTotalItemsSoldLbl;
	private static Label stockSummaryValueLbl, totalPriceLbl;
	private static Spinner<Integer> quantitySp;
	private static final ObservableList<Cart> cartData = FXCollections.observableArrayList();
	private static ObservableList<Cart> cartBuffer;
	private static double totalInvoice;
	private static int counter, totalQty;
	private static int transNo;
	private static byte[] itemImage;
	private static ObservableList<Cart> tempCart;
	
	SalesPage() throws SQLException{
		tempCart = FXCollections.observableArrayList();
		cartBuffer  = FXCollections.observableArrayList();
		itemImage = null;
		String iconPath = "C:\\Users\\Admin\\eclipse-workspace\\StockManager\\src\\application\\icons\\";
		// Total invoice amount
		totalInvoice = 0.0;
		// Cart unique items counter
		counter = 0;
		// Quantity of items in cart
		totalQty = 0;
		
		// Grid pane to hold all the components of the Sale Page
		salesPane = new GridPane();
		// Set the preferred size
		CommonSource.setCenterPaneSize(salesPane);
		salesPane.setVgap(10);
		salesPane.setHgap(5);
		salesPane.setAlignment(Pos.TOP_LEFT);
		salesPane.setPadding(new Insets(3,3,3,3));
		salesPane.setId("salesPane");
		
		// Grid pane for the top components of the sales page
		topPane = new GridPane();
		topPane.setAlignment(Pos.TOP_LEFT);
//		topPane.setPrefSize(1350, 800);
		topPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		topPane.setVgap(5);
		topPane.setHgap(10);
		topPane.setPadding(new Insets(10,5,5,5));
		topPane.setId("topPane");		
				
		// For the search bar
		searchBox = new HBox(10);
		searchBox.setAlignment(Pos.CENTER_LEFT);
		searchBox.setPadding(new Insets(2,2,2,2));
		searchBox.setId("searchBar");
		
		Label searchLbl = new Label("Search: ");
		searchTf = new TextField();
		searchTf.setPrefWidth(200);
		// Adding components to search bar pane
		searchBox.getChildren().addAll(searchLbl, searchTf);
		// Adding components to the pane
		topPane.add(searchBox, 0, 0);
		
		// Mounting the sales table
		topPane.add(new SalesItemsTable().getTable(), 0, 1, 1, 2);
		
		// Pane for item image
		imageBox = new VBox();
		imageBox.setMinSize(150, 150);
		imageBox.setId("imageBox");
		topPane.add(imageBox, 1, 1);
		
		// Sales summary panel
		summaryPane = new GridPane();
		summaryPane.setAlignment(Pos.TOP_CENTER);
//		summaryPane.setMinWidth(295);
//		summaryPane.setMinHeight(150);
		summaryPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		summaryPane.setVgap(5);
		summaryPane.setHgap(5);
		summaryPane.setPadding(new Insets(2,2,2,2));
		summaryPane.setId("summaryPane");
		topPane.add(summaryPane, 2, 1);
		
		//
		StackPane stackPane = new StackPane();
		stackPane.setAlignment(Pos.CENTER);
//		stackPane.setMaxSize(64, 43);
		// ensure stackpane is never resized beyond it's preferred size
	    stackPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		stackPane.setId("stackPane");
		
		//
		Image cartIcon = new Image(iconPath + "shopping_cart.png");
		ImageView imageView = new ImageView(cartIcon);		
		imageView.setFitHeight(42); 
	    imageView.setFitWidth(63);
		
	    StackPane.setMargin(imageView, new Insets(0, 10, 0, -10));
	    stackPane.getChildren().add(imageView);
	    
	    cartLabel = new Label("0");
	    stackPane.getChildren().add(cartLabel);
	    cartLabel.setId("cartLabel");
	    
	    // For the cart label
 		VBox cartPanel = new VBox(10);
 		cartPanel.setAlignment(Pos.BOTTOM_RIGHT);
 		cartPanel.setPadding(new Insets(2,5,2,5));
 		cartPanel.setId("cartPanel");
	    
	    //
		uniqueItemsLbl = new Label("0 Unique item(s)");
		uniqueItemsLbl.getStyleClass().add("cartLabel");
		
		// Sum total of cart items price
		totalPriceLbl = new Label("GHC 0.0");		
		
		// mount the labels on the pane
		cartPanel.getChildren().addAll(stackPane, uniqueItemsLbl, totalPriceLbl);
		
		// For the cart label
		HBox cartLabelPane = new HBox(10);
		cartLabelPane.setAlignment(Pos.BOTTOM_RIGHT);
		cartLabelPane.setPadding(new Insets(2,2,2,2));
		cartLabelPane.setId("cartLabelPane");
		
		// Adding cart pane
		cartLabelPane.getChildren().add(cartPanel);
		
		// Mounting the cart label pane on the top pane
		topPane.add(cartLabelPane, 1, 2, 2, 1);
		
		// Grid pane for the form and cart table
		bottomPane = new GridPane();
		bottomPane.setAlignment(Pos.TOP_LEFT);
//		bottomPane.setPrefSize(1500, 800);
		bottomPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		bottomPane.setVgap(5);
		bottomPane.setHgap(10);
		bottomPane.setPadding(new Insets(2,2,2,2));
		bottomPane.setId("bottomPane");

		// Form pane
		formGridPane = new GridPane();
		formGridPane.setAlignment(Pos.CENTER);
		formGridPane.setHgap(10);
		formGridPane.setVgap(5);
		formGridPane.setPrefSize(480, 250);
		formGridPane.setPadding(new Insets(10,5,10,5));
		formGridPane.getStyleClass().add("formGridPane");
		
		// For item category
		Label categoryLbl = new Label("Category");
		formGridPane.add(categoryLbl, 0, 0);
		categoryTf = new TextField();
		categoryTf.setEditable(false);
		categoryTf.setMinWidth(90);
		formGridPane.add(categoryTf, 1, 0, 3, 1);
		
		// For item brand
		Label brandLbl = new Label("Brand");
		formGridPane.add(brandLbl, 0, 1);
		brandTf = new TextField();
		brandTf.setEditable(false);
		brandTf.setMinWidth(90);
		formGridPane.add(brandTf, 1, 1, 3, 1);
		
		// For item name
		Label nameLbl = new Label("Item Name");
		formGridPane.add(nameLbl, 0, 2);
		nameTf = new TextField();
		nameTf.setEditable(false);
		nameTf.setMinWidth(90);
		formGridPane.add(nameTf, 1, 2, 3, 1);
		
		// For item id
		Label idLbl = new Label("Item Id");
		formGridPane.add(idLbl, 0, 3);
		idTf = new TextField();
		idTf.setEditable(false);
		idTf.setMaxWidth(70);
		formGridPane.add(idTf, 1, 3);
		
		// For item price
		Label unitPriceLbl = new Label("Price/unit");
		formGridPane.add(unitPriceLbl, 2, 3);
		unitPriceTf = new TextField();
		unitPriceTf.setEditable(false);
		unitPriceTf.setPromptText("0.0");
		unitPriceTf.setMaxWidth(80);
		formGridPane.add(unitPriceTf, 3, 3);
		
		// For item selected quantity
		Label qtyLbl = new Label("Quantity");
		formGridPane.add(qtyLbl, 0, 5);
		quantitySp = new Spinner<Integer>(1, 100, 1);
		quantitySp.setMaxWidth(70);
		formGridPane.add(quantitySp, 1, 5);
		//
		quantitySp.valueProperty().addListener((obs, ov, nv) -> {
			if (!unitPriceTf.getText().isEmpty()) { 
				totalAmountLbl.setText(String.valueOf(nv * Double.valueOf(unitPriceTf.getText())));
			}
			else {
				return;
			}
		});
		// Select the content of the spinner on focus
		quantitySp.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
            	quantitySp.getEditor().selectAll();
            }
        });
		
		// For total price of the selected item
		Label totalLbl = new Label("Total: ");
		formGridPane.add(totalLbl, 2, 5);
//		totalLbl.getStyleClass().add("boldLbl");		
		totalAmountLbl = new Label("0.0");
		totalAmountLbl.getStyleClass().add("boldLbl");
		formGridPane.add(totalAmountLbl, 3, 5);
		
//		formGridPane.add(CommonSource.horizontalRule(390.0), 0, 6, 4, 1);
		
		/* Inner panel for the form */
		// 
		Label invoiceLbl = new Label("Invoice: ");
		formGridPane.add(invoiceLbl, 0, 7);
		//
		invoiceTotalLbl = new Label("0.0");
		invoiceTotalLbl.getStyleClass().add("boldLbl");
		formGridPane.add(invoiceTotalLbl, 1, 7);
		
		Label changeLbl = new Label("Change: ");
//		changeLbl.getStyleClass().add("boldLbl");
		formGridPane.add(changeLbl, 0, 8);
		changeAmountLbl = new Label("0.0");
		changeAmountLbl.getStyleClass().add("boldLbl");
		formGridPane.add(changeAmountLbl, 1, 8);
		
		// For the amount to be given as change after invoice payment
		Label amountLbl = new Label("Amount: ");
		formGridPane.add(amountLbl, 2, 7);
		amountTf = new TextField();
		amountTf.setPromptText("0.0");
		amountTf.setMaxWidth(80);
		formGridPane.add(amountTf, 3, 7);
		amountTf.textProperty().addListener(event -> {
			if (!amountTf.getText().isEmpty()) {
				try {
				changeAmountLbl.setText(String.valueOf(changeAmount()));
				}catch(Exception e) {
					changeAmountLbl.setText("0.0");
					Message.showErrorAlert("You entered a wrong value. Please enter digits only.");			
				}
			}else {
				changeAmountLbl.setText("0.0");
			}
		});
		
		GridPane gp = new GridPane();
		gp = new GridPane();
		gp.setAlignment(Pos.TOP_LEFT);
//		gp.setPrefSize(1500, 800);
		gp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		gp.setVgap(5);
		gp.setHgap(10);
		gp.setPadding(new Insets(2,2,2,2));
		gp.setId("gp");
		
		//
		
		
		// Button pane
		VBox buttonPane1 = new VBox(10);
		buttonPane1.setAlignment(Pos.CENTER);
//		buttonPane1.setMaxSize(30, 80);
		buttonPane1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		buttonPane1.setPadding(new Insets(5,5,5,5));
		buttonPane1.getStyleClass().add("fg-buttonPane");
		
		// Add item to cart button
		Image cartIcon2 = new Image(iconPath + "add-to-cart.png");
		ImageView imageView2 = new ImageView(cartIcon2);		
		imageView2.setFitHeight(30); 
	    imageView2.setFitWidth(30);
		
		Button addToCartBtn = new Button("Add to cart >>");
//		buttonPane1.add(addToCartBtn, 0, 0, 1, 2);
		addToCartBtn.setGraphic(imageView2);
		addToCartBtn.setId("fg-addToCartBtn");
		addToCartBtn.setOnAction(event -> addToCart());
		
		// Clear button
		Button clearBtn = new Button("Clear");
//		buttonPane1.add(clearBtn, 1, 0);
		clearBtn.setId("fg-clearBtn");
		clearBtn.setOnAction(event -> resetInputFields());
		
		// Delete item button icon
		Image removeIcon = new Image(iconPath + "circle.png");
		ImageView imageView3 = new ImageView(removeIcon);		
		imageView3.setFitHeight(25); 
	    imageView3.setFitWidth(25);
		
		Button removeItemBtn = new Button("Remove");
//		buttonPane1.add(removeItemBtn, 0, 1);
//		removeItemBtn.setGraphic(imageView3);
		removeItemBtn.setId("fg-removeItemBtn");
		removeItemBtn.setOnAction(event -> {
			removeFromCart();
			setCartBuffer(getCartItem());
			try {
				writeCartBuffer(getCartBuffer());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			readCartBuffer();
		});
		
		// make payment button
		Button payBtn = new Button("Pay");
//		buttonPane1.add(payBtn, 0, 2);
		payBtn.setId("fg-addItemBtn");
		payBtn.setOnAction(event -> {
			try {
				completeSales();
				clearCartBuffer();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
				
		// Update button
		Button invoiceBtn = new Button("Invoice");
//		buttonPane1.add(invoiceBtn, 0, 3);
		invoiceBtn.setId("fg-updateItemBtn");
		invoiceBtn.setOnAction(event -> generateInvoice());
		
		//
		buttonPane1.getChildren().addAll(addToCartBtn, payBtn, invoiceBtn, clearBtn, removeItemBtn);
		
		// Mounting the button pane on the form grid pane
		formGridPane.add(buttonPane1, 4, 0, 1, 9);
		
		// Mounting the form pane
		bottomPane.add(formGridPane, 0, 0, 1, 2);
		
		// Button box
		VBox buttonBox = new VBox(15);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setMaxSize(50, 150);
//		buttonBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		buttonBox.setPadding(new Insets(5,0,5,0));
		buttonBox.setId("buttonBox");
		
		// Add item button alternative
		Button addBtn = new Button(">>");
		addBtn.setOnAction(event -> addToCart());
		addBtn.setId("addBtn");
		
		// Remove item button alternative
		Button removeBtn = new Button("<<");
		removeBtn.setOnAction(event -> removeFromCart());
		removeBtn.setId("removeBtn");
		
		// Adding the buttons to the pane
		buttonBox.getChildren().addAll(addBtn, removeBtn);
		
		// Adding the buttonBox to the bottom pane
		bottomPane.add(buttonBox, 1, 0);
		
		// Mounting the cart table
		bottomPane.add(new CartTable().getTable(), 2, 0);
		
		//
		VBox vb1 = new VBox();
		vb1.setPrefWidth(265);
		vb1.setAlignment(Pos.CENTER_LEFT);
		vb1.setPadding(new Insets(5,5,5,5));
		vb1.setId("vb1");
		// Current sales summary label
		Label summaryLbl = new Label("Today's total sales");
//		summaryPane.add(summaryLbl, 0, 0);
		
		// Current daily total sales label
		currentTotalSalesLbl = new Label("$ 0.0");
		currentTotalSalesLbl.getStyleClass().add("salesSummary");
//		summaryPane.add(currentTotalSalesLbl, 0, 1, 2, 1);
		
		vb1.getChildren().addAll(summaryLbl, currentTotalSalesLbl);
		summaryPane.add(vb1, 0, 0, 2, 1);
		
		//
		VBox vb2 = new VBox();
		vb2.setPrefWidth(130);
		vb2.setAlignment(Pos.CENTER_LEFT);
		vb2.setPadding(new Insets(5,5,5,5));
		vb2.setId("vb2");
		// Current total number of items sold label
		Label itemSummaryLbl = new Label("Items sold today");
//		summaryPane.add(itemSummaryLbl, 0, 2);
		
		// Current daily total sales label
		currentTotalItemsSoldLbl = new Label("0");
		currentTotalItemsSoldLbl.getStyleClass().add("salesSummary");
//		summaryPane.add(currentTotalItemsSoldLbl, 0, 3);
		
		vb2.getChildren().addAll(itemSummaryLbl, currentTotalItemsSoldLbl);
		summaryPane.add(vb2, 0, 1);
		
		//
		VBox vb3 = new VBox();
		vb3.setPrefWidth(135);
		vb3.setAlignment(Pos.CENTER_LEFT);
		vb3.setPadding(new Insets(5,5,5,5));
		vb3.setId("vb3");
		// Current stock summary label
		Label stockLbl = new Label("Current total stock");
//		summaryPane.add(stockLbl, 1, 2);
		
		// Current remaining stock value label
		stockSummaryValueLbl = new Label("0");
		stockSummaryValueLbl.getStyleClass().add("salesSummary");
//		summaryPane.add(stockSummaryLbl, 1, 3);
		
		vb3.getChildren().addAll(stockLbl, stockSummaryValueLbl);
		summaryPane.add(vb3, 1, 1);
		
		//********************************/
		
		// Adding components to the pane
		salesPane.add(topPane, 0, 0);
		salesPane.add(bottomPane, 0, 1);
		
		
		/* Calling functions required at start-up */
		
		// Calling the function to get the current stock summary
		HomePage.getCurrentTotalStock(SalesPage.getStockSummaryValueLbl());
	}
	
	
	// Getters and setters
	public static TextField getSearchTf() {
		return searchTf;
	}

	public static void setSearchTf(TextField searchTf) {
		SalesPage.searchTf = searchTf;
	}

	public static TextField getCategoryTf() {
		return categoryTf;
	}

	public static TextField getBrandTf() {
		return brandTf;
	}

	public static TextField getNameTf() {
		return nameTf;
	}

	public static TextField getIdTf() {
		return idTf;
	}

	public void setIdTf(TextField id_Tf) {
		idTf = id_Tf;
	}

	public static TextField getUnitPriceTf() {
		return unitPriceTf;
	}

	public static void setUnitPriceTf(TextField unitPriceTf) {
		SalesPage.unitPriceTf = unitPriceTf;
	}

	public static Spinner<Integer> getQuantitySp() {
		return quantitySp;
	}

	public static Label getTotalAmountLbl() {
		return totalAmountLbl;
	}

	public static void setTotalAmountLbl(Label totalAmountLbl) {
		SalesPage.totalAmountLbl = totalAmountLbl;
	}

	public static Label getInvoiceTotalLbl() {
		return invoiceTotalLbl;
	}


	public static void setInvoiceTotalLbl(Label invoiceTotalLbl) {
		SalesPage.invoiceTotalLbl = invoiceTotalLbl;
	}

	public void mountSearchBox() {
		salesPane.add(searchBox, 0, 0);;
	}
	
	public GridPane getPane() {
		return salesPane;
	}

	public static double getTotalInvoice() {
		return totalInvoice;
	}

	public static void setTotalInvoice(double totalInvoice) {
		SalesPage.totalInvoice = totalInvoice;
	}

	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int counter) {
		SalesPage.counter = counter;
	}


	public static Label getCartLabel() {
		return cartLabel;
	}


	public static void setCartLabel(Label cartLabel) {
		SalesPage.cartLabel = cartLabel;
	}


	public static Label getTotalQtyLbl() {
		return totalQtyLbl;
	}


	public static void setTotalQtyLbl(Label totalQtyLbl) {
		SalesPage.totalQtyLbl = totalQtyLbl;
	}


	public static int getTotalQty() {
		return totalQty;
	}


	public static void setTotalQty(int totalQty) {
		SalesPage.totalQty = totalQty;
	}


	public static int getTransNo() {
		return transNo;
	}


	public static void setTransNo(int transNo) {
		SalesPage.transNo = transNo;
	}


	public static Label getCurrentTotalSalesLbl() {
		return currentTotalSalesLbl;
	}


	public static void setCurrentTotalSalesLbl(Label currentTotalSalesLbl) {
		SalesPage.currentTotalSalesLbl = currentTotalSalesLbl;
	}


	public static Label getCurrentTotalItemsSoldLbl() {
		return currentTotalItemsSoldLbl;
	}


	public static void setCurrentTotalItemsSoldLbl(Label currentTotalItemsSoldLbl) {
		SalesPage.currentTotalItemsSoldLbl = currentTotalItemsSoldLbl;
	}


	public static Label getStockSummaryValueLbl() {
		return stockSummaryValueLbl;
	}


	public static void setStockSummaryValueLbl(Label stockSummaryValueLbl) {
		SalesPage.stockSummaryValueLbl = stockSummaryValueLbl;
	}


	public static byte[] getItemImage() {
		return itemImage;
	}


	public static void setItemImage(byte[] itemImage) {
		SalesPage.itemImage = itemImage;
	}


	public static VBox getImageBox() {
		return imageBox;
	}


	public static void setImageBox(VBox imageBox) {
		SalesPage.imageBox = imageBox;
	}


	// Getting the cart data
	public static final ObservableList<Cart> getCartItem() {
		return cartData;
	}
	
	public static ObservableList<Cart> getCartBuffer() {
		return cartBuffer;
	}


	public static void setCartBuffer(ObservableList<Cart> cartBuffer) {
		SalesPage.cartBuffer = cartBuffer;
	}


	public static Label getTotalPriceLbl() {
		return totalPriceLbl;
	}


	public static void setTotalPriceLbl(Label totalPriceLbl) {
		SalesPage.totalPriceLbl = totalPriceLbl;
	}


	// set temp cart
	public static void setTempCart() {
		tempCart.add(new Cart(
				idTf.getText(), 
				nameTf.getText(),
				brandTf.getText(),
				categoryTf.getText(),
				Double.valueOf(unitPriceTf.getText()),
				quantitySp.getValue(),
				Double.valueOf(totalAmountLbl.getText()),
				Double.valueOf(invoiceTotalLbl.getText())
				));
	}
	 
	// check for duplicates and update the cart
	public static void checkDuplicatesAndUpdateCart() {
		if (cartData.size() == 0) {
			cartData.add(tempCart.get(0));
			tempCart.remove(0);
		}
		else if(cartData.size() > 0 ){
			for (int i=0; i<cartData.size(); i++) {
				if (cartData.get(i).getId().equals(tempCart.get(0).getId())) {
					int qty = tempCart.get(0).getQuantity();
					double amount = tempCart.get(0).getTotalPrice();
					tempCart.get(0).setQuantity(cartData.get(i).getQuantity() + qty);
					tempCart.get(0).setTotalPrice(cartData.get(i).getTotalPrice() + amount);
					cartData.remove(i);
					break;
				}
			}
			cartData.add(tempCart.get(0));
			tempCart.remove(0);
		}
		
//		else if(cartData.size() > 0 ){
//			cartData.stream()
//			.takeWhile(item -> item.getId().equals(tempCart.get(0).getId())) 		
//			.forEach(n -> {
//				int qty = n.getQuantity();
//				double amount = n.getTotalPrice();
//				tempCart.get(0).setQuantity(tempCart.get(0).getQuantity() + qty);
//				tempCart.get(0).setTotalPrice(tempCart.get(0).getTotalPrice() + amount);
//				System.out.println(tempCart.get(0).getId());
//				System.out.println(tempCart.get(0).getQuantity());
//				System.out.println(tempCart.get(0).getTotalPrice());
////				cartData.remove(n);
//			});
////			cartData.add(tempCart.get(0));
//			tempCart.remove(0);
//		}

	}
	
	// Setting the cart item data
	public static void setCartItem() throws SQLException {
		invoiceTotalLbl.setText(String.valueOf(Double.valueOf(invoiceTotalLbl.getText()) + Double.valueOf(totalAmountLbl.getText())));
		setTempCart();
		checkDuplicatesAndUpdateCart();
		
		// deducting the cart quantity and updating the stock table 
		SalesItemsTable.deductFromStockAndUpdate(idTf.getText(), quantitySp.getValue());
	}
	
	// Clear inputs fields
	public static void resetInputFields() {
		if (!idTf.getText().isEmpty()) {
			categoryTf.clear();
			brandTf.clear();
			nameTf.clear();
			idTf.clear();
//			totalAmountLbl.setText("0.0");
			quantitySp.getValueFactory().setValue(1); //***
			totalAmountLbl.setText("0.0");
			unitPriceTf.clear();	
		}
		else {
			return;
		}
	}
	
	// Calculate invoice amount when adding items to cart
	public static double invoiceAmount() {
		totalInvoice += Double.valueOf(totalAmountLbl.getText());
		setTotalInvoice(totalInvoice);
		return getTotalInvoice();
	}
	
//	// Calculating new invoice total amount when a cart item is removed
//	public static double newInvoiceAmount() {
//		setTotalInvoice(Double.valueOf(invoiceTotalLbl.getText()) - Double.valueOf(totalAmountLbl.getText()));
//		return getTotalInvoice();
//	}
	
	// Calculating new invoice total amount when a cart item is removed
	public static double newInvoiceAmount() {
		double amount = 0.0;
		for (Cart item :  cartData) {
			amount += item.getTotalPrice();
		}
		System.out.println(cartData.size());
		System.out.println(amount);
		setTotalInvoice(amount);
		return getTotalInvoice();
	}

	
	// Calculate change/balance when invoice payment is made
	public static double changeAmount() {
		double change = 0.0;
		change += Double.valueOf(amountTf.getText()) - getTotalInvoice(); 
		return change;
	}
	
	// Add to cart
	public static void addToCart() {
		try {
			if(idTf.getText().isEmpty()) {
				return;
			}
			else if(!idTf.getText().isEmpty() && CartTable.getTable().getSelectionModel().getSelectedItem() == null) {
				SalesPage.setCartItem();
				setTotalQty(totalQty + quantitySp.getValue());
				cartLabel.setText((String.valueOf(getTotalQty())));
				uniqueItemsLbl.setText(String.valueOf(cartData.size()) + " Unique item(s)");
				invoiceTotalLbl.setText(String.valueOf(invoiceAmount()));
				// Setting the cart total amount label
				totalPriceLbl.setText("GHC " + String.valueOf(invoiceAmount()));
				SalesPage.resetInputFields();
				searchTf.requestFocus();
				SalesItemsTable.checkStockLevel(SalesItemsTable.getData());
				// Save copy of the cart data
				setCartBuffer(getCartItem());
				try {
					writeCartBuffer(getCartBuffer());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				readCartBuffer();
			} 
			else {
				Message.showErrorAlert("You can only remove the selected item from the cart. "
						+ "\nIf you want to add an item to cart, please select an item from the stock list.");
				return;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	// Remove from cart
	public static void removeFromCart() {
		try {
			if (CartTable.getTable().getSelectionModel().getSelectedItem() != null) {
				SalesItemsTable.deductFromCartAndUpdate(idTf.getText(), quantitySp.getValue());
				setTotalQty(totalQty - Integer.valueOf(quantitySp.getEditor().getText()));
				CartTable.removeItem();
				cartLabel.setText((String.valueOf(getTotalQty())));
				invoiceTotalLbl.setText(String.valueOf(newInvoiceAmount()));				
				uniqueItemsLbl.setText(String.valueOf(cartData.size()) + " Unique items");
				resetInputFields();
				SalesItemsTable.checkStockLevel(SalesItemsTable.getData());
			}else if (SalesItemsTable.getTable().getSelectionModel().getSelectedItem() != null){
				Message.showErrorAlert("You cannot remove an item from the stock list.");
			}else
			{
				return;
			}
		  }catch (SQLException e) {
			e.printStackTrace();
		  }
	}
	
	
	// Write buffer to db
	public static void writeCartBuffer(ObservableList<Cart> list) throws SQLException {
		String sql1 = "INSERT INTO buffer (itemId, quantity, dateCreated) VALUES(?,?,?)";
		String sql2 = "DELETE FROM buffer";
		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		try {
			conn = DBConnection.connect();
			ps1 = conn.prepareStatement(sql1);
			ps2 = conn.prepareStatement(sql2);
			
			ps2.execute();
			
			for (Cart item : list) {
					ps1.setString(1, item.getId());
					ps1.setInt(2, item.getQuantity());
					ps1.setString(3,CommonSource.getDateAndTime("yyMMddHHmm"));
					ps1.execute();
			}
			
			conn.commit();
			
		}catch(Exception e) {
			e.printStackTrace();
			Message.showErrorAlert("Could not save to buffer!");
		}finally {
			if(ps1 != null) {
				ps1.close();
			}
			if(ps2 != null) {
				ps2.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
	}
	
	// Check the if the buffer is empty or not
	public static boolean isCartBufferEmpty() throws SQLException {
		
		String qry = "SELECT itemId FROM buffer";
		
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			conn = DBConnection.connect();
			st = conn.createStatement();
			rs = st.executeQuery(qry);
			if(rs.next()) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			Message.showErrorAlert("Could not check the cart buffer!");
		}finally {
			if(st != null) {
				st.close();
			}
			if(rs != null) {
				rs.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
		return true;
	}
	
	// Roll back from buffer
	public static void rollBackCartBuffer() throws SQLException {
		
		String qry = "SELECT itemId, quantity FROM buffer";
		String sql = "UPDATE stock SET quantity=(quantity + ?) WHERE itemId=?";
		
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnection.connect();
			ps = conn.prepareStatement(sql);
			st = conn.createStatement();
			rs = st.executeQuery(qry);
			if(rs.next()) {
				do {
					ps.setInt(1, rs.getInt("quantity"));
					ps.setString(2, rs.getString("itemId"));
					ps.executeUpdate();
				}while(rs.next());
			}
			else {
				System.out.println("No records in cart buffer.");
			}
			
			conn.commit();
			// Clear the buffer after successful recovery
			clearCartBuffer();
		}catch(Exception e) {
			e.printStackTrace();
			Message.showErrorAlert("Could not clear the cart buffer!");
		}finally {
			if(st != null) {
				st.close();
			}
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
	
	// Clear cart buffer from db
	public static void clearCartBuffer() throws SQLException {

		String sql = "DELETE FROM buffer";
		Connection conn = null;

		PreparedStatement ps = null;
		try {
			conn = DBConnection.connect();

			ps = conn.prepareStatement(sql);
			
			ps.execute();
			
			conn.commit();
			
		}catch(Exception e) {
			e.printStackTrace();
			Message.showErrorAlert("Could not clear the cart buffer!");
		}finally {
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
	}
	
	// Read buffer
	public static void readCartBuffer() {
		getCartBuffer().stream().forEach(b -> System.out.println(b.getId() + ":" + b.getQuantity()));
		System.out.println("Buffer size: " + getCartBuffer().size());
	}
	
	// Complete sale
	public static void completeSales() throws SQLException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		String transDate = sdf1.format(new Date());
		SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmSS");
		setTransNo(Integer.valueOf(sdf2.format(new Date()).substring(0, 6)));
		String sql = "INSERT INTO sales (itemId, brandId, categoryId, totalStock, unitPrice, quantity, totalPrice, remainingStock, transDate, transNo) "
					+ "VALUES(?,(SELECT brand.id FROM brand JOIN item ON item.brand=brand.name WHERE item.id=?),"
							+ "(SELECT category.id FROM category JOIN item ON item.category=category.name WHERE item.id=?),"
							+ "(SELECT (stock.quantity + ?) FROM stock JOIN item ON item.id=stock.itemId WHERE stock.itemId=?),?,?,?,"
							+ "(SELECT stock.quantity FROM stock JOIN item ON item.id=stock.itemId WHERE stock.itemId=?),?,?);";
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBConnection.connect();
			ps = conn.prepareStatement(sql);
			for(Cart item : getCartItem()) {
						ps.setString(1, item.getId());
						ps.setString(2, item.getId());
						ps.setString(3, item.getId());
						ps.setInt	(4, item.getQuantity());
						ps.setString(5, item.getId());
						ps.setDouble(6, item.getUnitPrice());
						ps.setInt	(7, item.getQuantity());
						ps.setDouble(8, item.getTotalPrice());
						ps.setString(9, item.getId());
						ps.setString(10, transDate);
						ps.setInt	(11, getTransNo());
						// 
						ps.executeUpdate();
	
			}
			conn.commit();
			// Get the sales summary for the current date
			getCurrentTotalSales(getCurrentTotalSalesLbl(), getCurrentTotalItemsSoldLbl());
			HomePage.getCurrentTotalStock(SalesPage.getStockSummaryValueLbl());
//			DailySalesTable.refreshSalesData();
			// Make a copy of cart data for invoice generation
			copyCartData();
			// Reset the cart data
			resetCart();
			Message.showInfoAlert("Sales transation successful!");
		}catch(Exception e) {
			e.printStackTrace();
			conn.rollback();
			Message.showErrorAlert("Could not complete the transation! Something went wrong.");
		}finally {
			if(ps != null) {
				ps.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
	}
	
	// Generate invoice
	public static void generateInvoice() {
		try {
			System.out.println("Temp List size in gen. invoice: " + CartTable.getTempCartItems().size());
			if (CartTable.getTempCartItems().size() > 0) {
				new CreateFile("invoice").writeToFile(CartTable.getTempCartItems());
				CartTable.getTempCartItems().clear();
			}else {
				Message.showErrorAlert("Cart is empty. \nPlease make some sales and try again.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Get current day's total sales summary
	public static void getCurrentTotalSales(Label l1, Label l2) throws SQLException {
		String sql = "SELECT SUM(totalPrice) AS totalSales, SUM(quantity) AS totalQuantity FROM sales WHERE transDate LIKE ?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.connect();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+ CommonSource.getDateAndTime("yyMMdd") +"%");
			rs = ps.executeQuery();
			while(rs.next()) {
				l1.setText("$ " + String.valueOf(rs.getDouble("totalSales")));
				l2.setText(String.valueOf(rs.getInt("totalQuantity")));
			}
		}catch(Exception e) {
			e.printStackTrace();
			Message.showErrorAlert("Could not load the summary data!");
		}finally {
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
	
	// Reset cart
	public static void resetCart() {
		setCounter(0);
		setTotalQty(0);
		clearAmountField();
		getCartItem().clear();
		cartLabel.setText((String.valueOf(getCounter())));
	}
	
	// Clear amount field
	public static void clearAmountField() {
		amountTf.clear();
		changeAmountLbl.setText("0.0");
		invoiceTotalLbl.setText("0.0");
	}
	
	// Make a copy of cart data
	public static void copyCartData() {
		ObservableList<Cart> list = FXCollections.observableArrayList();
		list.addAll(getCartItem());
		CartTable.setTempCartItems(list);
	}
	
//	// Select the image file
//	public static String selectImageFile() {
//		ExtensionFilter ext1 = new ExtensionFilter("Image files", ".jpg");
//		ExtensionFilter ext2 = new ExtensionFilter("Image files", ".jpeg");
//		ExtensionFilter ext3 = new ExtensionFilter("Image files", ".png");
//		ExtensionFilter ext4 = new ExtensionFilter("All files", "*.*");	
//		try {
//			FileChooser fileChooser = new FileChooser();
//			fileChooser.getExtensionFilters().addAll(ext1, ext2, ext3, ext4);
//			fileChooser.setInitialDirectory(new File("C:/Users/Admin/Pictures"));
//			File file = fileChooser.showOpenDialog(Main.stage);
//			selectedImageFile = file.getAbsolutePath();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return selectedImageFile;
//	}
//	
//	// Read the image file
//	public static byte[] readImageFile(String filePath) {
//		File imageFile = null;
//		FileInputStream fis = null;
//		ByteArrayOutputStream bos = null;
//		try {
//			imageFile = new File(filePath);
//			fis = new FileInputStream(imageFile);			
//			bos = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			for(int length; (length=fis.read(buffer)) != -1;) {
//				bos.write(buffer, 0, length);
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return bos != null ? bos.toByteArray() : null;
//	}
	
//	// Get current day's total sales summary
//	public static void saveImage() throws SQLException, IOException {
//		String sql = "UPDATE stock SET image=? WHERE itemId=?;";
//		FileInputStream fs = null;
//		Connection conn = null;
//		PreparedStatement ps = null;
//		try {
//			conn = DBConnection.connect();
//			if (!selectImageFile().isEmpty()) {
//				ps = conn.prepareStatement(sql);
//				ps.setBytes(1, readImageFile(selectImageFile()));
//				ps.setString(2, "HW34");
//				ps.executeUpdate();
//				conn.commit();
////				System.out.println("Image successfully uploaded.");
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//			Message.showErrorAlert("Could not upload the image!");
//		}finally {
//			if(ps != null) {
//				ps.close();
//			}
//			if(conn != null) {
//				conn.close();
//			}
//			if(fs != null) {
//				fs.close();
//			}
//		}
//	}
}
