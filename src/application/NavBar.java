package application;

import java.awt.Cursor;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class NavBar {
	
	private String iconPath;
	private int page;
	private VBox navBar;
	
	NavBar() throws SQLException{
		
		int page = 0;
		this.iconPath = "C:\\Users\\Admin\\eclipse-workspace\\StockManager\\src\\application\\icons\\";
				
		navBar = new VBox(15);
		navBar.setPrefSize(170, 1000);
		navBar.setAlignment(Pos.TOP_CENTER);
		navBar.setPadding(new Insets(5,5,5,5));
		navBar.getStyleClass().add("navBar");
		
		VBox logoBox = new VBox(5);
		logoBox.setAlignment(Pos.CENTER);
		logoBox.setPadding(new Insets(10,10,10,10));
		logoBox.setId("logoBox");
		
		Image logo = new Image(iconPath + "hotel.png");
		ImageView logoView = new ImageView(logo);
		logoBox.getChildren().add(logoView);		
		logoView.setFitHeight(50); 
		logoView.setFitWidth(40);
		
		Label lbl = new Label("Inventory Manager");
		lbl.setTextFill(Color.BLACK);
		logoBox.getChildren().add(lbl);
				
		Image imageIcon1 = new Image(iconPath + "house-window.png");
		ImageView imageView1 = new ImageView(imageIcon1);		
		imageView1.setFitHeight(30); 
		imageView1.setFitWidth(30);
	    
		Image imageIcon2 = new Image(iconPath + "plus1.png");
		ImageView imageView2 = new ImageView(imageIcon2);		
		imageView2.setFitHeight(30); 
	    imageView2.setFitWidth(30);
	    
		Image imageIcon3 = new Image(iconPath + "dollar-symbol.png");
		ImageView imageView3 = new ImageView(imageIcon3);		
		imageView3.setFitHeight(30); 
	    imageView3.setFitWidth(30);
	    
	    Image imageIcon4 = new Image(iconPath + "sign-out-option.png");
		ImageView imageView4 = new ImageView(imageIcon4);		
		imageView4.setFitHeight(30); 
	    imageView4.setFitWidth(30);
	    
	    Image imageIcon5 = new Image(iconPath + "text-file.png");
		ImageView imageView5 = new ImageView(imageIcon5);		
		imageView5.setFitHeight(30); 
	    imageView5.setFitWidth(30);
	    
		Button dashBoardNavBtn = new Button(" Dashboard");
//		navBar.getChildren().add(dashBoardNavBtn);
		dashBoardNavBtn.getStyleClass().add("navBtn");
		dashBoardNavBtn.setGraphic(imageView1);
		dashBoardNavBtn.setOnAction(event -> {
			try {
				App.getBorderPane().setCenter(new HomePage().getPane());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	
		Button addItemNavBtn = new Button(" Add Item");
		addItemNavBtn.getStyleClass().add("navBtn");
		addItemNavBtn.setGraphic(imageView2);
		addItemNavBtn.setOnAction(event -> {
			try {
				App.getBorderPane().setCenter(new ItemPage().getPane());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		Button salesNavBtn = new Button(" Make Sales");
		salesNavBtn.getStyleClass().add("navBtn");
		salesNavBtn.setGraphic(imageView3);
		salesNavBtn.setOnAction(event -> {
			try {
				App.getBorderPane().setCenter(new SalesPage().getPane());
				SalesPage.getCurrentTotalSales(SalesPage.getCurrentTotalSalesLbl(), SalesPage.getCurrentTotalItemsSoldLbl());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Check and set the status stock level
			SalesItemsTable.checkStockLevel(SalesItemsTable.getData());
		});
		
		// Reports button
		Button reportNavBtn = new Button(" Report");
		reportNavBtn.getStyleClass().add("navBtn");
		reportNavBtn.setGraphic(imageView5);
		reportNavBtn.setOnAction(event -> {
			try {
				App.getBorderPane().setCenter(new ReportPage().getPane());
				DailySalesTable.setCurrentDayData();
				ReportPage.setReportTotals();
//				ReportPage.getAggregateData(DailySalesTable.getData());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		// Logout button panel
		VBox logOutBtnBox = new VBox(10);
		logOutBtnBox.setAlignment(Pos.BOTTOM_CENTER);
		logOutBtnBox.setPrefHeight(320);
		logOutBtnBox.setPadding(new Insets(10,10,10,10));
		logOutBtnBox.setId("logOutBtnBox");
		
		Button navLogOutBtn = new Button(" Logout");
		logOutBtnBox.getChildren().add(navLogOutBtn);
		navLogOutBtn.getStyleClass().add("navBtn");
		navLogOutBtn.setGraphic(imageView4);
		navLogOutBtn.setOnAction(event -> System.exit(0));
		
		navBar.getChildren().addAll(logoBox, dashBoardNavBtn, addItemNavBtn, salesNavBtn, reportNavBtn, logOutBtnBox);
		
	}
	
	public VBox getNavBar() {
		return navBar;
	}

	//
	public int getPage() {
		return page;
	}
	
	//
	public void setPage(int page) {
		this.page = page;
	}
}
