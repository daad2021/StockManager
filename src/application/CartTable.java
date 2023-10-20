package application;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CartTable extends HBox{
	private static TableView<Cart> table;
	private TableColumn<Cart, String> id;
	private TableColumn<Cart, String> name;
	private TableColumn<Cart, String> brand;
	private TableColumn<Cart, String> category;
	private TableColumn<Cart, Double> unitPrice, totalPrice;
	private TableColumn<Cart, Integer> quantity;
	private static ObservableList<Cart> cartItems;
	private static ObservableList<Cart> tempCartItems;
	private static double cartItemPrice;
	private static double removedItemPrice;
	
	
	CartTable(){
		table = new TableView<Cart>();
		id = new TableColumn<Cart, String>("ID");
		name = new TableColumn<Cart, String>("Name");
		brand = new TableColumn<Cart, String>("Brand");
		category = new TableColumn<Cart, String>("Category");
		unitPrice = new TableColumn<Cart, Double>("UnitPrice");
		quantity = new TableColumn<Cart, Integer>("Quantity");
		totalPrice = new TableColumn<Cart, Double>("Sub Total");
		
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
		category.setCellValueFactory(new PropertyValueFactory<>("category"));
		unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		
		table.getColumns().addAll(id,name,brand,unitPrice,quantity, totalPrice);
	
		// Adding a listener to the table selected row
		table.getSelectionModel()
			 .selectedItemProperty()
			 .addListener((obValue, oldValue, newValue) -> {
			if(newValue != null) {
				try {
					// Setting the form fields with the selected items
					SalesPage.getCategoryTf().setText(newValue.getCategory());
					SalesPage.getBrandTf().setText(newValue.getBrand());
					SalesPage.getNameTf().setText(newValue.getName());
					SalesPage.getIdTf().setText(newValue.getId());
					SalesPage.getUnitPriceTf().setText(String.valueOf(newValue.getUnitPrice()));
					SalesPage.getQuantitySp().getValueFactory().setValue(newValue.getQuantity());
					SalesPage.getTotalAmountLbl().setText(String.valueOf(newValue.getTotalPrice()));
					// Disable the spinner to prevent editing
//					SalesPage.getQuantitySp().setDisable(true);
					if (SalesItemsTable.getTable().getSelectionModel().getSelectedItem() != null) {
						SalesItemsTable.getTable().getSelectionModel().clearSelection();
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//
		table.setItems(SalesPage.getCartItem());	
//		table.setMinWidth(615);
//		table.setMaxHeight(320);
		table.setMaxSize(Double.MAX_VALUE, 400);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
//		String iconPath = "C:\\Users\\Admin\\eclipse-workspace\\StockManager\\src\\application\\icons\\";
		
		Image cartIcon = new Image(CommonSource.getIconPath() + "shopping-cart3.png");
		ImageView imageView = new ImageView(cartIcon);		
		imageView.setFitHeight(60); 
	    imageView.setFitWidth(60);
		Label iconLabel = new Label("Cart is empty");
		iconLabel.setGraphic(imageView);
		table.setPlaceholder(iconLabel);
	}
	
	public static TableView<Cart> getTable() {
		return table;
	}

	public void setTable(TableView<Cart> table) {
		CartTable.table = table;
	}

	public static ObservableList<Cart> getCartItems() {
		return cartItems;
	}

	public static void setCartItems(ObservableList<Cart> items) {
		CartTable.cartItems = items;
	}

	public static ObservableList<Cart> getTempCartItems() {
		return tempCartItems;
	}
	
	public static void setTempCartItems(ObservableList<Cart> tempCartItems) {
		CartTable.tempCartItems = tempCartItems;
	}

	public static double getCartItemPrice() {
		return cartItemPrice;
	}

	public static void setCartItemPrice(double itemPrice) {
		cartItemPrice = itemPrice;
	}
	

	public static double getRemovedItemPrice() {
		return removedItemPrice;
	}

	public static void setRemovedItemPrice(double removedItemPrice) {
		CartTable.removedItemPrice = removedItemPrice;
	}

	// Remove item from cart
	public static void removeItem() {
		 Cart item = table.getSelectionModel().getSelectedItem();
		 if (item != null) {
			 table.getItems().remove(item);
			 table.getSelectionModel().clearSelection();
		 }else {
			 Message.showErrorAlert("No item has been selected for removal. "
			 		+ "\nPlease select and item and try again!");
			 return;
		 }
	}
}
