package application;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Cart extends SalesModel{
	private String id;
	private String name;
	private String brand;
	private String category;
	private double unitPrice, totalPrice, invoiceTotal;
	private int quantity;
	
	Cart(String id, String name, String brand, String category, double unitPrice, int quantity, double totalPrice, double invoiceTotal){
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.category = category;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.invoiceTotal = invoiceTotal;
	}
	
	Cart(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double price) {
		this.unitPrice = price;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getInvoiceTotal() {
		return invoiceTotal;
	}

	public void setInvoiceTotal(double invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}
}
