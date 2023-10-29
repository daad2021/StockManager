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

public class DashBoard {
	
	private GridPane mainGridPane;
	private String iconPath;
	private int page;
	private HomePage homePage;
	private ItemPage itemPage;
	private SalesPage salesPage;
	private ReportPage reportPage;
	
	DashBoard() throws SQLException{
		
		int page = 0;
		this.iconPath = "C:\\Users\\Admin\\eclipse-workspace\\StockManager\\src\\application\\icons\\";
		mainGridPane = new GridPane();
		mainGridPane.setAlignment(Pos.TOP_LEFT);
		mainGridPane.setHgap(10);
		mainGridPane.setVgap(10);
		mainGridPane.setPadding(new Insets(2,2,2,2));
		mainGridPane.setId("mainGridPane");
		
		homePage = new HomePage();
		itemPage = new ItemPage();
		salesPage = new SalesPage();
		reportPage = new ReportPage();
		
		final HBox menuBar = new HBox(10);
		menuBar.setMaxSize(1165, 10);
		menuBar.setAlignment(Pos.CENTER_LEFT);
		menuBar.setPadding(new Insets(10,10,10,10));
		menuBar.setId("menuBar");
		
		Label menuLbl = new Label("Menu Label");
		menuBar.getChildren().add(menuLbl);
				
		final VBox naveBar = new VBox(10);
		naveBar.setPrefSize(160, 1000);
		naveBar.setAlignment(Pos.TOP_CENTER);
		naveBar.setPadding(new Insets(5,5,5,5));
		naveBar.getStyleClass().add("navBar");
		
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
		lbl.setTextFill(Color.WHITE);
		logoBox.getChildren().add(lbl);
		
		naveBar.getChildren().add(logoBox);
				
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
		naveBar.getChildren().add(dashBoardNavBtn);
		dashBoardNavBtn.getStyleClass().add("navBtn");
		dashBoardNavBtn.setGraphic(imageView1);
		dashBoardNavBtn.setOnAction(event -> {
			if (getPage() == 1) {
				mainGridPane.getChildren().remove(itemPage.getPane());
				mainGridPane.add(homePage.getPane(), 1, 1, 1, 2);
				setPage(0);
				try {
					DailySalesTable.setCurrentDayData();
					SalesPage.getCurrentTotalSales(HomePage.getCurrentTotalSalesLbl(), HomePage.getCurrentTotalItemsSoldLbl());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (getPage() == 2) {
				mainGridPane.getChildren().remove(salesPage.getPane());
				mainGridPane.add(homePage.getPane(), 1, 1, 1, 2);
				setPage(0);
				try {
					SalesPage.getCurrentTotalSales(HomePage.getCurrentTotalSalesLbl(), HomePage.getCurrentTotalItemsSoldLbl());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (getPage() == 3) {
				mainGridPane.getChildren().remove(reportPage.getPane());
				mainGridPane.add(homePage.getPane(), 1, 1, 1, 2);
				setPage(0);
			}
			else {
				return;
			}
		});
		
		Button addItemNavBtn = new Button(" Add Item");
		naveBar.getChildren().add(addItemNavBtn);
		addItemNavBtn.getStyleClass().add("navBtn");
		addItemNavBtn.setGraphic(imageView2);
		addItemNavBtn.setOnAction(event -> {
			if (getPage() == 0) {
				mainGridPane.getChildren().remove(homePage.getPane());
				mainGridPane.add(itemPage.getPane(), 1, 1, 1, 2);
				setPage(1);
			}
			else if (getPage() == 2) {
				mainGridPane.getChildren().remove(salesPage.getPane());
				mainGridPane.add(itemPage.getPane(), 1, 1, 1, 2);
				setPage(1);
			}
			else if (getPage() == 3) {
				mainGridPane.getChildren().remove(reportPage.getPane());
				mainGridPane.add(itemPage.getPane(), 1, 1, 1, 2);
				setPage(1);
			}
			else {
				return;
			}
		});
		
		Button salesNavBtn = new Button(" Make Sales");
		naveBar.getChildren().add(salesNavBtn);
		salesNavBtn.getStyleClass().add("navBtn");
		salesNavBtn.setGraphic(imageView3);
		salesNavBtn.setOnAction(event -> {
			if (getPage() == 0) {
				mainGridPane.getChildren().remove(homePage.getPane());
				mainGridPane.add(salesPage.getPane(), 1, 1, 1, 2);
				setPage(2);
//				try {
//					// Get the current day's total sales
//					SalesPage.getCurrentTotalSales(SalesPage.getCurrentTotalSalesLbl(), SalesPage.getCurrentTotalItemsSoldLbl());
//					// Check and set the status stock level
//					SalesItemsTable.checkStockLevel(SalesItemsTable.getData());
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			else if (getPage() == 1) {
				mainGridPane.getChildren().remove(itemPage.getPane());
				mainGridPane.add(salesPage.getPane(), 1, 1, 1, 2);
				setPage(2);
//				try {
//					// Get the current day's total sales
//					SalesPage.getCurrentTotalSales(SalesPage.getCurrentTotalSalesLbl(), SalesPage.getCurrentTotalItemsSoldLbl());
//					// Check and set the status stock level
//					SalesItemsTable.checkStockLevel(SalesItemsTable.getData());
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			else if (getPage() == 3) {
				mainGridPane.getChildren().remove(reportPage.getPane());
				mainGridPane.add(salesPage.getPane(), 1, 1, 1, 2);
				setPage(2);
			}
			else {
				return;
			}
			// Get the current day's total sales
			try {
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
		naveBar.getChildren().add(reportNavBtn);
		reportNavBtn.getStyleClass().add("navBtn");
		reportNavBtn.setGraphic(imageView5);
		reportNavBtn.setOnAction(event -> {
			if (getPage() == 0) {
				mainGridPane.getChildren().remove(homePage.getPane());
				mainGridPane.add(reportPage.getPane(), 1, 1, 1, 2);
				setPage(3);
			}
			else if (getPage() == 1) {
				mainGridPane.getChildren().remove(itemPage.getPane());
				mainGridPane.add(reportPage.getPane(), 1, 1, 1, 2);
				setPage(3);
			}
			else if (getPage() == 2) {
				mainGridPane.getChildren().remove(salesPage.getPane());
				mainGridPane.add(reportPage.getPane(), 1, 1, 1, 2);
				setPage(3);
			}
			else {
				return;
			}
			try {
				DailySalesTable.setCurrentDayData();
				ReportPage.setReportTotals();
//				ReportPage.getAggregateData(DailySalesTable.getData());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		// Logout button panel
		VBox hb = new VBox(10);
		hb.setAlignment(Pos.BOTTOM_CENTER);
		hb.setPadding(new Insets(10,10,10,10));
		hb.getStyleClass().add("navBar");
		
		Button navLogOutBtn = new Button(" Logout");
		hb.getChildren().add(navLogOutBtn);
		navLogOutBtn.getStyleClass().add("navBtn");
		navLogOutBtn.setGraphic(imageView4);
		navLogOutBtn.setOnAction(event -> System.exit(0));
		
		mainGridPane.add(menuBar, 0, 0, 2, 1);
		mainGridPane.add(naveBar, 0, 1);
		mainGridPane.add(hb, 0, 2);
		mainGridPane.add(homePage.getPane(), 1, 1, 1, 2);
	}
	
	//
	public GridPane getPane() {
		return this.mainGridPane;
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
