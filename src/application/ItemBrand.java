package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ItemBrand {
	private static String name;
	private static ObservableList<String> list;
	
	public ItemBrand(String categoryName) {
		name = categoryName;
	}
	
	ItemBrand(){}

	public static String getName() {
		return name;
	}

	public static void setName(String brandName) {
		name = brandName;
	}

	// Get the data from db
	public static ObservableList<String> getList() throws SQLException {
		list = FXCollections.observableArrayList();
		String qry = "SELECT * FROM brand;";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnection.connect();
			st = conn.createStatement();
			rs = st.executeQuery(qry);
			
			while (rs.next()) {
				list.add(rs.getString("name"));
			}
		}catch(Exception e) {
			System.out.println("Could not fetch the data: " + e.getMessage());
		}finally {
			rs.close();
			st.close();
			conn.close();
		}
		
		return list;
	}
	
//	// Check if the editable combobox value exists in db
//	public static boolean checkComboList(ComboBox<String> cb, ObservableList<String> list) {
//		for (String l : list) {
//			if (cb.getValue().equals(l)) {
//				break;
//			}
//		}
//		return true;
//	}	
}
