package application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CommonSource {
	
	// Set the preferred size of the center pane of the App border pane
	public static void setCenterPaneSize(Node pane) {
//		((Region) pane).setPrefSize(1010, 800);
//		((Region) pane).setMaxWidth(1010);
//		((Region) pane).setPrefHeight(1000);
		((Region) pane).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	}
	
	// Get the item names
	public static ObservableList<String> getTableColumnData(String sqlQuery, String columnName) throws SQLException {		
		ObservableList<String> list = FXCollections.observableArrayList();			
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;	
		try {	
			conn = DBConnection.connect();
			st = conn.createStatement();			
			rs = st.executeQuery(sqlQuery);
			while(rs.next()) {
				list.add(rs.getString(columnName)); 
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			rs.close();
			st.close();
			conn.close();
		}
		return list;
	}
	
	// ComboBox filter
	public static void filterCombo(String searchText, ComboBox<String> combo, ObservableList<String> list) {
		ArrayList<String> filteredItems = new ArrayList<>();
		for (String i : list) {
			if (i.toString().toLowerCase().contains(searchText.toLowerCase())) {
				filteredItems.add(i);
			}
		}
		combo.setItems(FXCollections.observableArrayList(filteredItems));
	}
	
	// Icon path
	public static final String getIconPath() {
		String iconPath = "C:\\Users\\Admin\\eclipse-workspace\\StockManager\\src\\application\\icons\\";
		return iconPath;
	}
	
	// Date/time
	public static String getDateAndTime(String stringFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(stringFormat);
		String current = sdf.format(new Date());
		return current;
	}
	
	
	// Select the image file
	public static String selectImageFile() {
		ExtensionFilter ext1 = new ExtensionFilter("Image files", ".jpg");
		ExtensionFilter ext2 = new ExtensionFilter("Image files", ".jpeg");
		ExtensionFilter ext3 = new ExtensionFilter("Image files", ".png");
		ExtensionFilter ext4 = new ExtensionFilter("All files", "*.*");	
		File file = null;
		String selectedImageFile = null;
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(ext1, ext2, ext3, ext4);
			fileChooser.setInitialDirectory(new File("C:/Users/Admin/Pictures"));
			file = fileChooser.showOpenDialog(Main.stage);
			selectedImageFile = file.getAbsolutePath();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return file != null ? selectedImageFile : null;
	}
	
	// Save file dialog
	public static void saveFile() {
		FileChooser fileChooser = null;
		try {
			fileChooser = new FileChooser();
			fileChooser.setTitle("Save");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel files (*.xlsx)", "*.xlsx"));
	        //Opening a dialog box
	        File file = fileChooser.showSaveDialog(Main.stage);
	        Optional<File> optFile = Optional.ofNullable(file);
	        if ( optFile.isPresent()) {
		        // Calling the write file function
		        new ExcelService(file.getName()).writeToFile(DailySalesTable.getData());
	        }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Read the image file
	public static byte[] readImageFile(String filePath) {
		File imageFile = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			imageFile = new File(filePath);
			fis = new FileInputStream(imageFile);			
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			for(int length; (length=fis.read(buffer)) != -1;) {
				bos.write(buffer, 0, length);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return bos != null ? bos.toByteArray() : null;
	}
	
	// Draw a horizontal line
	public static Path horizontalRule(double length) {
		
		//Creating an object of the Path class 
	      Path path = new Path(); 
	       
	      //Moving to the starting point 
	      MoveTo moveTo = new MoveTo(); 
	      moveTo.setX(100.0); 
	      moveTo.setY(100.0); 
	       
	      //Instantiating the HLineTo class 
	      HLineTo hLineTo = new HLineTo();       
	         
	      //Setting the properties of the path element horizontal line 
	      hLineTo.setX(length);
	       
	      //Adding the path elements to Observable list of the Path class 
	      path.getElements().add(moveTo); 
	      path.getElements().add(hLineTo);
		
		return path;
	}
	
//	// ComboBox filter
//		public static <T> void filterCombo(String searchText, ComboBox<T> combo, ArrayList<T> list) {
//			ArrayList<T> filteredItems = new ArrayList<>();
//			for (T i : list) {
//				if (i.toString().toLowerCase().contains(searchText.toLowerCase())) {
//					filteredItems.add(i);
//				}
//			}
//			combo.setItems(FXCollections.observableArrayList(filteredItems));
//		}
}
