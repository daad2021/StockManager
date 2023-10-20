package application;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class SalesModel {
	
	private String id;
	private String name;
	private String brand;
	private String category;
	private Double price;
	private int quantity;
	private Button statusBtn;

	SalesModel(String id, String name, String brand, String category, double price, int quantity){
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.category = category;
		this.price = price;
		this.quantity = quantity;
		this.statusBtn = new Button();
		this.statusBtn.setId("statusBtn");
	}
	
	SalesModel(){}
	
	//
	public String getId() {
		return id;
	}

	public void setItemId(String itemId) {
		this.id = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Button getStatusBtn() {
		return statusBtn;
	}

	public void setStatusBtn(Button statusBtn) {
		this.statusBtn = statusBtn;
	}


}
