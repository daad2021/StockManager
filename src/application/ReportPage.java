package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ReportPage {
	private TabPane rootPane;
	private static GridPane tab1_rootPane, topPanel;
	private GridPane formPane, summaryPane;
	private static VBox btnPane, mainPane, chartPanel;
	private static RadioButton allItemsRadioBtn, specificItemRadioBtn, categoryRadioBtn, brandRadioBtn, allCategoryRadioBtn, allBrandRadioBtn;
	private static ComboBox<String> nameCombo, idCombo, categoryCombo, brandCombo;
	private static Label totalItemsSoldLbl, totalAmountLbl, totalStockLbl, totalClosingStockLbl;
	private static String fromDateVar, toDateVar;
	private static DatePicker fromDatePicker = null;
	private static DatePicker toDatePicker = null;
	private static String commandText;
	private static PieChart pieChart;
	private static ToggleGroup toggleGroup;
	
	public ReportPage() throws SQLException {
		
		// Item id combobox
		idCombo = new ComboBox<String>(CommonSource.getTableColumnData("SELECT id FROM item;", "id"));
		// Item name combobox
		nameCombo = new ComboBox<String>(CommonSource.getTableColumnData("SELECT name FROM item;", "name"));
		// Item category combo box
		categoryCombo = new ComboBox<String>(CommonSource.getTableColumnData("SELECT name FROM category;", "name"));
		// Item brand combo box
		brandCombo = new ComboBox<String>(CommonSource.getTableColumnData("SELECT name FROM brand;", "name"));
		
		// Root pane
		rootPane = new TabPane();
		CommonSource.setCenterPaneSize((Node)rootPane);
		rootPane.setId("reportPage");
		Tab report1 = new Tab("Daily sales");
		Tab report2 = new Tab("Report page2");
		Tab report3 = new Tab("Report page3");
		
		report2.setContent(new Label("Report 2 page"));
		report3.setContent(new Label("Report 3 page"));
		
		rootPane.getTabs().addAll(report1, report2, report3);
		
		//
		tab1_rootPane = new GridPane();
		tab1_rootPane.setAlignment(Pos.TOP_LEFT);
		tab1_rootPane.setPrefSize(900, 425);
		tab1_rootPane.setVgap(5);
		tab1_rootPane.setHgap(5);
		tab1_rootPane.setPadding(new Insets(5,5,5,5));
		tab1_rootPane.setId("tab1_rootPane");
		
		topPanel = new GridPane();
		topPanel.setAlignment(Pos.TOP_LEFT);
		topPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		topPanel.setVgap(10);
		topPanel.setHgap(5);
		topPanel.setPadding(new Insets(5,5,5,5));
		topPanel.setId("topPanel");	
		
		report1.setContent(tab1_rootPane);
		
		tab1_rootPane.add(topPanel, 0, 0, 2, 1);
		
		//
		topPanel.add(new DailySalesTable().getTable(), 0, 0);
		
		//
		buildForm();
				
	}
	
	
	//********************** report form *************************
	
	public void buildForm() throws SQLException {
	
		// Refresh button HBox
		HBox hb = new HBox();
		hb.setPrefWidth(275);
		hb.setAlignment(Pos.CENTER_LEFT);
		hb.setPadding(new Insets(5,5,5,5));
		hb.setId("hb");
		
		Button refreshBtn = new Button("Refresh");
		refreshBtn.setId("refreshBtn");
		hb.getChildren().add(refreshBtn);
		refreshBtn.setOnAction(event -> {
			try {
				DailySalesTable.setCurrentDayData();
				allItemsRadioBtn.setSelected(true);
				setReportTotals();
				// Pass a collection to the pie chart
				pieChart.setData(getChartDataByName(DailySalesTable.getData()));
				setChartTitle();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		//
		topPanel.add(refreshBtn, 0, 1);
		
		//**************Query summary*****************
		
		// Summary pane
		summaryPane = new GridPane();
		summaryPane.setAlignment(Pos.TOP_LEFT);
		summaryPane.setPrefSize(370, 200);
		summaryPane.setVgap(5);
		summaryPane.setHgap(10);
		summaryPane.setPadding(new Insets(5,5,5,5));
		summaryPane.setId("summaryPane");
		
		//
		VBox vb1 = new VBox();
		vb1.setPrefWidth(350);
		vb1.setAlignment(Pos.CENTER_LEFT);
		vb1.setPadding(new Insets(5,5,5,5));
		vb1.setId("vb1");
		// Current sales summary label
		Label summaryLbl = new Label("Total sales amount");
		
		// Current daily total sales label
		totalAmountLbl = new Label("GHC 0.0");
		totalAmountLbl.getStyleClass().add("salesSummary");
		
		vb1.getChildren().addAll(summaryLbl, totalAmountLbl);
		summaryPane.add(vb1, 0, 0);
		
		//
		VBox vb2 = new VBox();
		vb2.setPrefWidth(350);
		vb2.setAlignment(Pos.CENTER_LEFT);
		vb2.setPadding(new Insets(5,5,5,5));
		vb2.setId("vb2");
		// Current total number of items sold label
		Label itemSummaryLbl = new Label("Total items sold");
		
		// Current daily total sales label
		totalItemsSoldLbl = new Label("0");
		totalItemsSoldLbl.getStyleClass().add("salesSummary");
		
		vb2.getChildren().addAll(itemSummaryLbl, totalItemsSoldLbl);
		summaryPane.add(vb2, 0, 1);
		
		//
		VBox vb3 = new VBox();
		vb3.setPrefWidth(350);
		vb3.setAlignment(Pos.CENTER_LEFT);
		vb3.setPadding(new Insets(5,5,5,5));
		vb3.setId("vb3");
		// Opening stock summary label
		Label stockLbl = new Label("Total opening stock(at the time)");
		
		// The total opening stock value label
		totalStockLbl = new Label("0");
		totalStockLbl.getStyleClass().add("salesSummary");
		
		vb3.getChildren().addAll(stockLbl, totalStockLbl);
		summaryPane.add(vb3, 0, 2);
		
		//
		VBox vb4 = new VBox();
		vb4.setPrefWidth(350);
		vb4.setAlignment(Pos.CENTER_LEFT);
		vb4.setPadding(new Insets(5,5,5,5));
		vb4.setId("vb4");
		// Closing stock summary label
		Label closingStockLbl = new Label("Total closing stock(at the time)");
		
		// Closing stock value label
		totalClosingStockLbl = new Label("0");
		totalClosingStockLbl.getStyleClass().add("salesSummary");
		
		vb4.getChildren().addAll(closingStockLbl, totalClosingStockLbl);
		summaryPane.add(vb4, 0, 3);
		
		//
		topPanel.add(summaryPane, 1, 0);
		
		//****************end of query summary***************
		
		// Initialising the radio buttons
		allItemsRadioBtn = new RadioButton("All items");
		allItemsRadioBtn.setUserData("all items");
		allItemsRadioBtn.setSelected(true);
		//
		specificItemRadioBtn = new RadioButton("Specific item");
		specificItemRadioBtn.setUserData("specific item");
		//
		allCategoryRadioBtn = new RadioButton("All category");
		allCategoryRadioBtn.setUserData("all category");
		//
		categoryRadioBtn = new RadioButton("Specific category");
		categoryRadioBtn.setUserData("specific category");
		//
		allBrandRadioBtn = new RadioButton("All brand");
		allBrandRadioBtn.setUserData("all brand");
		//
		brandRadioBtn = new RadioButton("Specific brand");
		brandRadioBtn.setUserData("specific brand");

		// Toggle group
		toggleGroup = new ToggleGroup();
		toggleGroup.selectedToggleProperty().addListener((ob, ov, nv) -> {
			if(toggleGroup.getSelectedToggle() != null) {
				if(nv.getUserData().toString().equals("all items")) {
					commandText = nv.getUserData().toString();					
					idCombo.getEditor().setText("--Select--");
					nameCombo.getEditor().setText("--Select--");
					categoryCombo.getEditor().setText("--Select--");
					brandCombo.getEditor().setText("--Select--");
					idCombo.setDisable(true);
					nameCombo.setDisable(true);
					categoryCombo.setDisable(true);
					brandCombo.setDisable(true);
				}else if(nv.getUserData().toString().equals("specific item")){
					commandText = nv.getUserData().toString();
					categoryCombo.getEditor().setText("--Select--");
					brandCombo.getEditor().setText("--Select--");
					idCombo.setDisable(false);
					nameCombo.setDisable(false);
					categoryCombo.setDisable(true);
					brandCombo.setDisable(true);
				}
				else if(nv.getUserData().toString().equals("all category")){
					commandText = nv.getUserData().toString();
					idCombo.getEditor().setText("--Select--");
					nameCombo.getEditor().setText("--Select--");
					brandCombo.getEditor().setText("--Select--");
					idCombo.setDisable(true);
					nameCombo.setDisable(true);
					categoryCombo.setDisable(true);
					brandCombo.setDisable(true);
				}
				else if(nv.getUserData().toString().equals("specific category")){
					commandText = nv.getUserData().toString();
					idCombo.getEditor().setText("--Select--");
					nameCombo.getEditor().setText("--Select--");
					brandCombo.getEditor().setText("--Select--");
					idCombo.setDisable(true);
					nameCombo.setDisable(true);
					categoryCombo.setDisable(false);
					brandCombo.setDisable(true);
				}
				else if(nv.getUserData().toString().equals("all brand")){
					commandText = nv.getUserData().toString();
					idCombo.getEditor().setText("--Select--");
					nameCombo.getEditor().setText("--Select--");
					categoryCombo.getEditor().setText("--Select--");
					idCombo.setDisable(true);
					nameCombo.setDisable(true);
					categoryCombo.setDisable(true);
					brandCombo.setDisable(true);
				}
				else if(nv.getUserData().toString().equals("specific brand")){
					commandText = nv.getUserData().toString();
					idCombo.getEditor().setText("--Select--");
					nameCombo.getEditor().setText("--Select--");
					categoryCombo.getEditor().setText("--Select--");
					idCombo.setDisable(true);
					nameCombo.setDisable(true);
					categoryCombo.setDisable(true);
					brandCombo.setDisable(false);
				}
			}
		});

		// adding to togglegroup
		allItemsRadioBtn.setToggleGroup(toggleGroup);
		categoryRadioBtn.setToggleGroup(toggleGroup);
		specificItemRadioBtn.setToggleGroup(toggleGroup);
		brandRadioBtn.setToggleGroup(toggleGroup);
		allCategoryRadioBtn.setToggleGroup(toggleGroup);
		allBrandRadioBtn.setToggleGroup(toggleGroup);
		
		// Radio button pane
		HBox radioRootPane = new HBox(10);
		radioRootPane.setAlignment(Pos.CENTER_LEFT);
		radioRootPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		radioRootPane.setPadding(new Insets(0,0,10,0));
		radioRootPane.setId("radioRootPane");
		
		VBox radioPane1 = new VBox(10);
		radioPane1.setAlignment(Pos.CENTER_LEFT);
		radioPane1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		radioPane1.setPadding(new Insets(10,10,10,10));
		radioPane1.setId("radioPane");
		radioPane1.getChildren().addAll(allItemsRadioBtn, specificItemRadioBtn);
		
		VBox radioPane2 = new VBox(10);
		radioPane2.setAlignment(Pos.CENTER_LEFT);
		radioPane2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		radioPane2.setPadding(new Insets(10,10,10,10));
		radioPane2.setId("radioPane");
		radioPane2.getChildren().addAll(allCategoryRadioBtn, categoryRadioBtn);
		
		VBox radioPane3 = new VBox(10);
		radioPane3.setAlignment(Pos.CENTER_LEFT);
		radioPane3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		radioPane3.setPadding(new Insets(10,10,10,10));
		radioPane3.setId("radioPane");
		radioPane3.getChildren().addAll(allBrandRadioBtn, brandRadioBtn);
				
		// Adding the radio buttons to the radio pane
		radioRootPane.getChildren().addAll(radioPane1, radioPane2, radioPane3);

		// Add stock form pane
		formPane = new GridPane();
		formPane.setAlignment(Pos.CENTER_LEFT);
		formPane.setHgap(5);
		formPane.setVgap(10);
		formPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		formPane.setPadding(new Insets(5,5,5,5));
		formPane.setId("formPane");
		
		// Name combo field
		Label nameLbl = new Label("Item Name");
		formPane.add(nameLbl, 0, 0);
//		nameCombo = new ComboBox<String>(CommonSource.getTableColumnData("SELECT name FROM item;", "name"));
		nameCombo.setPromptText("--Select--");
		nameCombo.setEditable(true);
		nameCombo.setVisibleRowCount(8);
		nameCombo.setMaxWidth(130);
		//adding the combo to the panel
		formPane.add(nameCombo, 1, 0);
		nameCombo.setOnAction(event -> {
			try {
				autoFillIdCombo();
//				autoFillCombo(idCombo, "id", nameCombo);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		
		// Category combo field
		Label categoryLbl = new Label("Category");
		formPane.add(categoryLbl, 2, 0);
//		categoryCombo = new ComboBox<String>(CommonSource.getTableColumnData("SELECT category FROM item;", "name"));
		categoryCombo.setPromptText("--Select--");
		categoryCombo.setEditable(true);
		categoryCombo.setVisibleRowCount(8);
		categoryCombo.setMaxWidth(130);
		//adding the combo to the panel
		formPane.add(categoryCombo, 3, 0);
		categoryCombo.setOnAction(event -> {
			try {
				autoFillIdCombo();
//				autoFillCombo(idCombo, "id", nameCombo);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		// Category combo field
		Label brandLbl = new Label("Brand");
		formPane.add(brandLbl, 2, 1);
//		brandCombo = new ComboBox<String>(CommonSource.getTableColumnData("SELECT name FROM brand;", "name"));
		brandCombo.setPromptText("--Select--");
		brandCombo.setEditable(true);
		brandCombo.setVisibleRowCount(8);
		brandCombo.setMaxWidth(130);
		//adding the combo to the panel
		formPane.add(brandCombo, 3, 1);
		brandCombo.setOnAction(event -> {
			try {
				autoFillIdCombo();
//						autoFillCombo(idCombo, "id", nameCombo);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		
		// Id text field
		Label idTfLbl = new Label("Id");
		formPane.add(idTfLbl, 0, 1);
//		idCombo = new ComboBox<String>(CommonSource.getTableColumnData("SELECT id FROM item;", "id"));
		idCombo.setPromptText("--Select--");
		idCombo.setEditable(true);
		idCombo.setVisibleRowCount(8);
		idCombo.setMaxWidth(80);
		formPane.add(idCombo, 1, 1);
		idCombo.setOnAction(event -> {
			try {
//				autoFillCombo(nameCombo, "name", idCombo);
				autoFillNameCombo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		// Date input field for the report select query
		Label fromDateTfLbl = new Label("From:");
		formPane.add(fromDateTfLbl, 0, 2);
		// Date picker
		 fromDatePicker = new DatePicker();
		 fromDatePicker.setPrefWidth(130);
		 fromDatePicker.setOnAction(event -> {
		        LocalDate date = fromDatePicker.getValue();
		        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		        fromDateVar = dateFormatter.format(date);
		 });
		 formPane.add(fromDatePicker, 1, 2);
		 
		 
		// Date input field for the report select query
		Label toDateTfLbl = new Label("To:");
		formPane.add(toDateTfLbl, 2, 2);
		// Date picker
		 toDatePicker = new DatePicker();
		 toDatePicker.setPrefWidth(130);
		 toDatePicker.setOnAction(event -> {
		        LocalDate date = toDatePicker.getValue();
		        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		        toDateVar = dateFormatter.format(date);
		 });
		 formPane.add(toDatePicker, 3, 2);


		// Button pane
		btnPane = new VBox(10);
		btnPane.setAlignment(Pos.CENTER);
		btnPane.setPrefSize(120, 100);
		btnPane.setPadding(new Insets(2,2,2,2));
		btnPane.setId("btnPane");
				
		// Update stock button
		Button exportBtn = new Button("Export");
		exportBtn.setId("fg-updateItemBtn");
		exportBtn.setOnAction(event -> {
			if (DailySalesTable.getData().size() > 0) {
				CommonSource.saveFile();
			}
		});
		
		// Executing the search query button
		Button queryBtn = new Button("Query");
		queryBtn.setId("fg-addItemBtn");
		formPane.add(queryBtn, 2, 1, 2, 1);
		queryBtn.setOnAction(event -> {
			queryReportData();
		});
		
		// Clear button
		Button clearBtn = new Button("Clear");
		clearBtn.setId("fg-clearBtn");
		clearBtn.setOnAction(event -> clearFormInputs());
		
		btnPane.getChildren().addAll(queryBtn, exportBtn, clearBtn);
		// Adding the button pane 
		formPane.add(btnPane, 4, 0, 1, 3);
		
		// Form root pane
		final VBox formRootPane = new VBox(10);
		formRootPane.setAlignment(Pos.CENTER);
		formRootPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		formRootPane.setPadding(new Insets(0,2,10,2));
		formRootPane.setId("formRootPane");;
		
		//
		formRootPane.getChildren().addAll(radioRootPane, formPane);
		
		tab1_rootPane.add(formRootPane, 0, 1);
		
		// Chart panel
		chartPanel = new VBox(5);
		chartPanel.setPrefSize(320, 280);
		chartPanel.setAlignment(Pos.TOP_LEFT);
		chartPanel.setPadding(new Insets(5,5,5,5));
		chartPanel.setId("chartPanel");
		
		// Pie chart
//		buildChart(getChartData(DailySalesTable.getData()));
		pieChart = new PieChart(getChartDataByName(DailySalesTable.getData()));
		if (DailySalesTable.getData().size() > 0) {
			pieChart.setTitle("Sales records by " + toggleGroup.getSelectedToggle().getUserData().toString());
		}else {
			pieChart.setTitle("Chart area");
		}
		pieChart.setLabelLineLength(6);
		pieChart.setLegendVisible(true);
		pieChart.setLabelsVisible(true);
		pieChart.setLegendSide(Side.RIGHT);
		pieChart.setMaxSize(400, 300);
        chartPanel.getChildren().add(pieChart);
        
        //
        pieChart.getData().forEach(data -> {
            String percentage = String.format("%.2f%%", (data.getPieValue() / 100));
            Tooltip toolTip = new Tooltip(percentage);
            Tooltip.install(data.getNode(), toolTip);
        });
        
        //
		tab1_rootPane.add(chartPanel, 1, 1);

	}
			
	//********************** end of report form ******************
	

	public TabPane getPane() {
		return rootPane;
	}

	public static String getFromDateVar() {
		return fromDateVar;
	}


	public static void setFromDateVar(String fromDateVar) {
		ReportPage.fromDateVar = fromDateVar;
	}


	public static String getToDateVar() {
		return toDateVar;
	}


	public static void setToDateVar(String toDateVar) {
		ReportPage.toDateVar = toDateVar;
	}

	

	public static DatePicker getFromDatePicker() {
		return fromDatePicker;
	}


	public static void setFromDatePicker(DatePicker fromDatePicker) {
		ReportPage.fromDatePicker = fromDatePicker;
	}


	public static DatePicker getToDatePicker() {
		return toDatePicker;
	}


	public static void setToDatePicker(DatePicker toDatePicker) {
		ReportPage.toDatePicker = toDatePicker;
	}


	public static ComboBox<String> getNameCombo() {
		return nameCombo;
	}


	public static void setNameCombo(ComboBox<String> nameCombo) {
		ReportPage.nameCombo = nameCombo;
	}


	public static ComboBox<String> getIdCombo() {
		return idCombo;
	}


	public static void setIdCombo(ComboBox<String> idCombo) {
		ReportPage.idCombo = idCombo;
	}


	public static ComboBox<String> getCategoryCombo() {
		return categoryCombo;
	}


	public static void setCategoryCombo(ComboBox<String> categoryCombo) {
		ReportPage.categoryCombo = categoryCombo;
	}


	public static ComboBox<String> getBrandCombo() {
		return brandCombo;
	}


	public static void setBrandCombo(ComboBox<String> brandCombo) {
		ReportPage.brandCombo = brandCombo;
	}


	public static ToggleGroup getToggleGroup() {
		return toggleGroup;
	}


	public static void setToggleGroup(ToggleGroup toggleGroup) {
		ReportPage.toggleGroup = toggleGroup;
	}


	// Prepare Pie chart data by item name
	public static  ObservableList<PieChart.Data> getChartDataByName(ObservableList<DailySales> data) {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (DailySales item : data) {
			pieChartData.add(new PieChart.Data(item.getTransDate(), item.getTotalSales()));
		}
		return pieChartData;
	}
	
	// Prepare Pie chart data by item brand
	public static  ObservableList<PieChart.Data> getChartDataByBrand(ObservableList<DailySales> data) {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (DailySales item : data) {
			pieChartData.add(new PieChart.Data(item.getTransDate(), item.getTotalSales()));
		}
		return pieChartData;
	}
	
	// Prepare Pie chart data by item category
	public static  ObservableList<PieChart.Data> getChartDataByCategory(ObservableList<DailySales> data) {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (DailySales item : data) {
			pieChartData.add(new PieChart.Data(item.getCategory(), item.getTotalSales()));
		}
		return pieChartData;
	}
	
	// Set pie chart title
	public static void setChartTitle() {
		pieChart.setTitle("Sales records by " + getToggleGroup().getSelectedToggle().getUserData().toString()); 
	}
	
	// Add event handler to the chart
	public static void showSlicePercentage() {
		 final Label caption = new Label("");
	        caption.setTextFill(Color.DARKORANGE);
	        caption.setStyle("-fx-font: 24 arial;");
	        chartPanel.getChildren().add(caption);

	        for (final PieChart.Data data : pieChart.getData()) {
	        	System.out.println("Chart have been clicked");
	            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
	                new EventHandler<MouseEvent>() {
	                    @Override public void handle(MouseEvent e) {
	                        caption.setTranslateX(e.getSceneX());
	                        caption.setTranslateY(e.getSceneY());
	                        caption.setText(String.valueOf(data.getPieValue()) + "%");
	                        System.out.println("Chart have been clicked 2");
	                     }
	                });
	        }
	}
	
	
//	// Build pie chart
//	public static void buildChart(ObservableList<PieChart.Data> chartData) {
//		PieChart pieChart = new PieChart(chartData);
//		pieChart.setTitle("Sales records");
//		pieChart.setMaxSize(300, 260);
//        chartPanel.getChildren().add(pieChart);		
//		tab1_rootPane.add(chartPanel, 1, 1);
//	}
	
	// Auto fill item id combo
	public static void autoFillIdCombo() throws SQLException {				
		String qry = "SELECT id FROM item WHERE name=?;";	
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {	
			conn = DBConnection.connect();
			if(nameCombo.getSelectionModel().getSelectedItem() != null) {
				ps = conn.prepareStatement(qry);
				ps.setString(1, nameCombo.getEditor().getText());
				rs = ps.executeQuery();
				while(rs.next()) {
					idCombo.getEditor().setText(rs.getString("id"));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
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
	
	// Auto fill item name combo
	public static void autoFillNameCombo() throws SQLException {				
		String qry = "SELECT name FROM item WHERE id=?;";	
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;	
		try {	
			conn = DBConnection.connect();
			if(idCombo.getSelectionModel().getSelectedItem() != null) {
				ps = conn.prepareStatement(qry);
				ps.setString(1, idCombo.getEditor().getText());
				rs = ps.executeQuery();
				while(rs.next()) {
					nameCombo.getEditor().setText(rs.getString("name"));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
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
	
	
	// Perform aggregation of the daily sales table data and set the summary values
	public static void getAggregateData(ObservableList<DailySales> list) {
		int quantitySold = 0;
		int remainingStock = 0;
		double totalSales = 0.0;
		for (DailySales d : list) {
			quantitySold += d.getQuantitySold();
//			remainingStock += d.getRemainingStock();
			totalSales += d.getTotalSales();
		}
		totalItemsSoldLbl.setText(String.valueOf(quantitySold));
		totalAmountLbl.setText("GHC " + String.valueOf(totalSales));
//		totalStockLbl.setText(String.valueOf(DailySalesTable.getClosingStock(DailySalesTable.getData())));
	}
	
//	// Auto fill item name combo and id combo
//	public static void autoFillCombo(ComboBox<String> cb1, String col, ComboBox<String> cb2) throws SQLException {				
//		String qry1 = "SELECT name FROM item WHERE id=?;";
//		String qry2 = "SELECT id FROM item WHERE name=?;";
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;	
//		try {	
//			conn = DBConnection.connect();
//			if(cb2.getSelectionModel().getSelectedItem() != null && col.equals("name")) {
//				ps = conn.prepareStatement(qry1);
//				ps.setString(1, cb2.getSelectionModel().getSelectedItem());
//				rs = ps.executeQuery();
//				while(rs.next()) {
//					cb1.getEditor().setText(rs.getString(col));
//				}
//			}
//			else if(cb1.getSelectionModel().getSelectedItem() != null && col.equals("id")) {
//				ps = conn.prepareStatement(qry2);
//				ps.setString(1, cb1.getSelectionModel().getSelectedItem());
//				rs = ps.executeQuery();
//				while(rs.next()) {
//					cb2.getEditor().setText(rs.getString(col));
//				}
//			}else {
//				return;
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		finally {
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

//	// Get the data from DB and add to the observable list
//	public static void setData() throws SQLException {
//		
//		String qry = "SELECT item.name AS name, SUM(sales.quantity) AS quantitySold, stock.quantity AS currentStock, SUM(sales.totalPrice) AS totalSales "
//				+ "FROM item AS item "
//				+ "JOIN sales AS sales "
//				+ "ON item.id=sales.itemId "
//				+ "JOIN stock AS stock "
//				+ "ON stock.itemId=item.id "
//				+ "WHERE sales.transDate like ? "
//				+ "GROUP BY sales.itemId "
//				+ "ORDER BY quantitySold DESC";		
//		Connection conn = null;
//		Statement st = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;		
//		try {			
//			conn = DBConnection.connect();
//			ps = conn.prepareStatement(qry);
//			if(ReportPage.getDateTf() == null) {
//				
//				ps.setString(1, "%" + CommonSource.getDateAndTime("yyyyMMdd") + "%");
//			}else {
//				ps.setString(1, "%" + ReportPage.getDateTf() + "%");
//			}
//			rs = ps.executeQuery();
//			data.clear();
//			while(rs.next()) {
//				data.add(new DailySales(
//						rs.getString("name"), 
//						rs.getInt("quantitySold"),
//						rs.getInt("currentStock"),
//						rs.getDouble("totalSales")
//						));
//			}	
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		finally {
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
	
//	// Check name combo box input fields
//	public static boolean isItemSelected() {
//		if(nameCombo.getEditor().getText().isEmpty() && idCombo.getEditor().getText().isEmpty()) {
//			return false;
//		}
//		return true;
//	}
	
//	// Check category combo box input fields
//	public static boolean isCategorySelected() {
//		if(categoryCombo.getEditor().getText().isEmpty()) {
//			return false;
//		}
//		return true;
//	}
//	
	// Check combo box input field
	public static boolean isComboSelected(ComboBox<String> combo) {
		if(combo.getEditor().getText().isEmpty() || combo.getEditor().getText().equals("--Select--")) {
			return false;
		}
		return true;
	}
	
	// Check date input fields
	public static boolean isDateSelected() {
		if(fromDatePicker.getEditor().getText().isEmpty() && toDatePicker.getEditor().getText().isEmpty()) {
			return false;
		}
		return true;
	}
	
	// Report summary
	public static void setReportTotals() {
		totalAmountLbl.setText("GHC " + String.valueOf(DailySalesTable.getTotalAmount()));
		totalItemsSoldLbl.setText(String.valueOf(DailySalesTable.getTotalItemsSold()));
		totalStockLbl.setText(String.valueOf(DailySalesTable.getOpeningStock(DailySalesTable.getData())));
		totalClosingStockLbl.setText(String.valueOf(DailySalesTable.getClosingStock(DailySalesTable.getData())));
	}

	// Clear form inputs
	public static void clearFormInputs() {
		if (!nameCombo.getEditor().getText().isEmpty() 
				|| !fromDatePicker.getEditor().getText().isEmpty()
				|| !toDatePicker.getEditor().getText().isEmpty()
				|| !idCombo.getEditor().getText().isEmpty()) {
			nameCombo.getEditor().setText("--Select--");
			idCombo.getEditor().setText("--Select--");
			brandCombo.getEditor().setText("--Select--");
			categoryCombo.getEditor().setText("--Select--");
			fromDatePicker.getEditor().clear();
			toDatePicker.getEditor().clear();
		}
		else {
			return;
		}
	}
	
	// Query report data
	public static void queryReportData() {
		try {
			if (commandText.equals("all items")) {
				if (!isDateSelected()) {
					Message.showErrorAlert("Some input field(s) are empty. Please check and fill them up.");
					return;
				}
				DailySalesTable.queryAllByDate();
				setReportTotals();
				pieChart.setData(getChartDataByName(DailySalesTable.getData()));
				setChartTitle();
			}
			else if (commandText.equals("specific item")) {
				if(!isComboSelected(idCombo) && !isComboSelected(nameCombo)) {
					Message.showErrorAlert("You have not selected an item for the query.");
					return;
				}else if (!isDateSelected()) {
					Message.showErrorAlert("Date input field is empty. Please select a date.");
					return;
				}
				DailySalesTable.queryByIdNameAndDate();
				pieChart.setData(getChartDataByName(DailySalesTable.getData()));
				setChartTitle();
				setReportTotals();
//				setStockSummary();
			}
			else if (commandText.equals("all category")) {
				if (!isDateSelected()) {
					Message.showErrorAlert("Date input field is empty. Please select a date.");
					return;
				}
				DailySalesTable.queryByAllCategoryAndDate();
				pieChart.setData(getChartDataByCategory(DailySalesTable.getData()));
				setChartTitle();
				setReportTotals();
			}
			else if (commandText.equals("specific category")) {
				if(!isComboSelected(categoryCombo)) {
					Message.showErrorAlert("You have not selected a category for the query.");
					return;
				}else if (!isDateSelected()) {
					Message.showErrorAlert("Date input field is empty. Please select a date.");
					return;
				}
				DailySalesTable.queryByCategoryAndDate();
				pieChart.setData(getChartDataByCategory(DailySalesTable.getData()));
				setChartTitle();
				setReportTotals();
			}
			else if (commandText.equals("all brand")) {
				if (!isDateSelected()) {
					Message.showErrorAlert("Date input field is empty. Please select a date.");
					return;
				}
				DailySalesTable.queryByAllBrandAndDate();
				    pieChart.setData(getChartDataByBrand(DailySalesTable.getData()));
				setChartTitle();
				setReportTotals();
			}
			else if (commandText.equals("specific brand")) {
				if(!isComboSelected(brandCombo)) {
					Message.showErrorAlert("You have not selected a brand for the query.");
					return;
				}else if (!isDateSelected()) {
					Message.showErrorAlert("Date input field is empty. Please select a date.");
					return;
				}
				DailySalesTable.queryByBrandAndDate();
				pieChart.setData(getChartDataByBrand(DailySalesTable.getData()));
				setChartTitle();
				setReportTotals();
			}
//			// Clear form inputs
//			clearFormInputs();
			// Prepare the aggregate data for the summary
			getAggregateData(DailySalesTable.getData());
		} catch (SQLException e) {
			Message.showErrorAlert("Could not fetch the records. " + "\nError: " + e.getMessage());
		}
	}
	
//	// Calculate the total number of the items in stock or the opening stock for the query from the list data
//	public static void setStockSummary() {
//		System.out.println("Opening: "+DailySalesTable.getOpeningStock(DailySalesTable.getData()));
//		totalStockLbl.setText(String.valueOf(DailySalesTable.getClosingStock(DailySalesTable.getData())));
//		System.out.println("Data: " +DailySalesTable.getData());
//		System.out.println("From funct " + totalStockLbl.getText());
//	}

}
