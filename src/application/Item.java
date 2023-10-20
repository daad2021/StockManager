package application;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item {
	private String id;
	private String name;
	private String brand;
	private String category;
//	private byte[] image;
	private double priceIn;
	private double priceOut;
	private double profitMargin;

	public Item(
			String id, 
			String name, 
			String brand, 
			String category,
//			byte[] image,
			double priceIn, 
			double priceOut, 
			double profitMargin){
		
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.category = category;
//		this.image = image;
		this.priceIn = priceIn;
		this.priceOut = priceOut;
		this.profitMargin = profitMargin;
	}
	
	Item(String name){
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
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


	public double getPriceIn() {
		return priceIn;
	}

	public double getPriceOut() {
		return priceOut;
	}
	
	public double getProfitMargin() {
		return profitMargin;
	}

//	public byte[] getImage() {
//		return image;
//	}
//
//	public void setImage(byte[] image) {
//		this.image = image;
//	}
	
}
