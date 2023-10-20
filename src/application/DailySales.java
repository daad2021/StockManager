package application;

import javafx.scene.layout.HBox;

public class DailySales extends HBox{
	private String itemName;
	private String brand;
	private String category;
	private int totalStock;
	private int quantitySold;
	private int remainingStock;
	private double totalSales;
	private String transDate;
	
	public DailySales(String itemName, String brand, String category, int totalStock, int quantitySold, int remainingStock, double totalSales, String transDate) {
		this.itemName = itemName;
		this.brand = brand;
		this.category = category;
		this.totalStock = totalStock;
		this.quantitySold = quantitySold;
		this.remainingStock = remainingStock;
		this.totalSales = totalSales;
		this.transDate = transDate;
	}
	
	public DailySales(String itemName, int totalStock, int quantitySold, int remainingStock, double totalSales, String transDate) {
		this.itemName = itemName;
		this.totalStock = totalStock;
		this.quantitySold = quantitySold;
		this.remainingStock = remainingStock;
		this.totalSales = totalSales;
		this.transDate = transDate;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public int getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(int totalStock) {
		this.totalStock = totalStock;
	}

	public int getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}

	public int getRemainingStock() {
		return remainingStock;
	}

	public void setRemainingStock(int remainingStock) {
		this.remainingStock = remainingStock;
	}

	public double getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(double totalSales) {
		this.totalSales = totalSales;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
}
