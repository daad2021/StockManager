package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Report {
	
	//
	public static void printInvoice() {
		//ObservableList<Cart> invoiceList = FXCollections.observableArrayList(SalesPage.getCartItem());
		for (Cart c : SalesPage.getCartItem()) {
			System.out.println("This is the brand list " +c.getBrand());
		}
	}
}
