package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HomePage {
	
	private GridPane homeRootPane, chartPane;
	private static Label currentTotalSalesLbl, currentTotalItemsSoldLbl, stockSummaryLbl;
	
	HomePage() throws SQLException{
		
		homeRootPane = new GridPane();
		homeRootPane.setHgap(10);
		homeRootPane.setVgap(10);
		homeRootPane.setPrefSize(940, 800);
		homeRootPane.setAlignment(Pos.TOP_LEFT);
		homeRootPane.setPadding(new Insets(5,5,5,5));
		homeRootPane.setId("homeRootPane");
		
		// Root summary HBox pane
		HBox summaryHbox = new HBox(20);
		summaryHbox.setPrefSize(940, 200);
		summaryHbox.setAlignment(Pos.CENTER_LEFT);
		summaryHbox.setPadding(new Insets(5,5,5,5));
		summaryHbox.getStyleClass().add("summary1");
		
		// 
		HBox summaryHbox1 = new HBox(10);
		summaryHbox1.setPrefSize(300, 150);
		summaryHbox1.setAlignment(Pos.CENTER_LEFT);
		summaryHbox1.setPadding(new Insets(10,10,10,10));
		summaryHbox1.getStyleClass().add("summary");
		
		// Current total sales panel
		GridPane summaryPane = new GridPane();
		summaryPane.setAlignment(Pos.TOP_LEFT);
		summaryPane.setPrefSize(290, 150);
		summaryPane.setVgap(5);
		summaryPane.setHgap(10);
		summaryPane.setPadding(new Insets(10,10,10,10));
		summaryPane.setId("summaryPane");
		summaryHbox1.getChildren().add(summaryPane);
		
		//
		VBox vb1 = new VBox();
		vb1.setPrefWidth(265);
		vb1.setAlignment(Pos.CENTER_LEFT);
		vb1.setPadding(new Insets(5,5,5,5));
		vb1.setId("vb1");
		// Current sales summary label
		Label summaryLbl = new Label("Day's total sales");
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
		Label itemSummaryLbl = new Label("Day's items sold");
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
		stockSummaryLbl = new Label("0");
		stockSummaryLbl.getStyleClass().add("salesSummary");
//		summaryPane.add(stockSummaryLbl, 1, 3);
		
		vb3.getChildren().addAll(stockLbl, stockSummaryLbl);
		summaryPane.add(vb3, 1, 1);
		
		
		// current stock parent panel
		HBox summaryHbox2 = new HBox(10);
		summaryHbox2.setPrefSize(300, 150);
		summaryHbox2.setAlignment(Pos.CENTER_LEFT);
		summaryHbox2.setPadding(new Insets(10,10,10,10));
		summaryHbox2.getStyleClass().add("summary");
		
		// Current stock panel
		GridPane stockSummaryPane = new GridPane();
		stockSummaryPane.setAlignment(Pos.TOP_LEFT);
		stockSummaryPane.setPrefSize(240, 150);
		stockSummaryPane.setVgap(5);
		stockSummaryPane.setHgap(10);
		stockSummaryPane.setPadding(new Insets(10,10,10,10));
		stockSummaryPane.setId("summaryPane");
		summaryHbox2.getChildren().add(stockSummaryPane);

		HBox summaryHbox3 = new HBox(10);
		summaryHbox3.setPrefSize(300, 150);
		summaryHbox3.setAlignment(Pos.CENTER_LEFT);
		summaryHbox3.setPadding(new Insets(10,10,10,10));
		summaryHbox3.getStyleClass().add("summary");
		
		HBox summaryHbox4 = new HBox(10);
		summaryHbox4.setPrefSize(300, 150);
		summaryHbox4.setAlignment(Pos.CENTER_LEFT);
		summaryHbox4.setPadding(new Insets(10,10,10,10));
		summaryHbox4.getStyleClass().add("summary");
//		homeRootPane.add(summaryHbox4, 3, 0);
		
		summaryHbox.getChildren().addAll(summaryHbox1, summaryHbox2, summaryHbox3);
		homeRootPane.add(summaryHbox, 0, 0);
		
		//
		chartPane = new GridPane();
		chartPane.setHgap(10);
		chartPane.setVgap(10);
		chartPane.setPrefSize(940, 450);
		chartPane.setAlignment(Pos.TOP_LEFT);
		chartPane.setPadding(new Insets(5,5,5,5));
		chartPane.setId("chartPane");
		
		homeRootPane.add(chartPane, 0, 1);
		
		/* Calling functions required at start-up */
		
		// Loading the current date/day's sales
		SalesPage.getCurrentTotalSales(HomePage.getCurrentTotalSalesLbl(), HomePage.getCurrentTotalItemsSoldLbl());
		
		// Getting the current total stock
		getCurrentTotalStock(HomePage.getStockSummaryLbl());
	}
	
	// Getting the current total stock summary
	public static void getCurrentTotalStock(Label label) throws SQLException {
		String qry = "SELECT SUM(quantity) AS currentStock FROM stock";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.connect();
			st = conn.createStatement();
			rs = st.executeQuery(qry);
			while(rs.next()) {
				label.setText(String.valueOf(rs.getInt("currentStock")));
			}
		}catch(Exception e) {
			e.printStackTrace();
			Message.showErrorAlert("Could not load the summary data!");
		}finally {
			if(rs != null) {
				rs.close();
			}
			if(st != null) {
				st.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
	}
	
	// Get current day's total sales summary
//	SalesPage.getCurrentTotalSalesLbl().setText("$ " + String.valueOf(rs.getDouble("totalSales")));
//	SalesPage.getCurrentTotalItemsSoldLbl().setText(String.valueOf(rs.getInt("totalQuantity")));

//	public static void getCurrentTotalSales(Label l1, String s1, Label l2, String s2) throws SQLException {
//		String sql = "SELECT SUM(totalPrice) AS totalSales, SUM(quantity) AS totalQuantity FROM sales WHERE transDate LIKE ?";
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try {
//			conn = DBConnection.connect();
//			ps = conn.prepareStatement(sql);
//			ps.setString(1, "%"+ CommonSource.getDateAndTime("yyMMdd") +"%");
//			rs = ps.executeQuery();
//			while(rs.next()) {
//				l1.setText("$ " + String.valueOf(rs.getDouble("totalSales")));
//				l2.setText(String.valueOf(rs.getInt("totalQuantity")));
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//			Message.showErrorAlert("Could not load the summary data!");
//		}finally {
//			if(rs != null) {
//				rs.close();
//			}
//			if(ps != null) {
//				ps.close();
//			}
//			if(conn != null) {
//				conn.close();
//			}
//		}
//	}
	
	// Get the homepage root pane
	public GridPane getPane() {
		return this.homeRootPane;
	}

	public static Label getCurrentTotalSalesLbl() {
		return currentTotalSalesLbl;
	}

	public static void setCurrentTotalSalesLbl(Label currentTotalSalesLbl) {
		HomePage.currentTotalSalesLbl = currentTotalSalesLbl;
	}

	public static Label getCurrentTotalItemsSoldLbl() {
		return currentTotalItemsSoldLbl;
	}

	public static void setCurrentTotalItemsSoldLbl(Label currentTotalItemsSoldLbl) {
		HomePage.currentTotalItemsSoldLbl = currentTotalItemsSoldLbl;
	}

	public static Label getStockSummaryLbl() {
		return stockSummaryLbl;
	}

	public static void setStockSummaryLbl(Label stockSummaryLbl) {
		HomePage.stockSummaryLbl = stockSummaryLbl;
	}
	
	
	
}
