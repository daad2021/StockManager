package application;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Message {
	
	// Information alert dialog
	public static void showInfoAlert(String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(msg);
		alert.setContentText("");
		alert.show();
//		alert.showAndWait();
	}
	
	// Error alert dialog
	public static void showErrorAlert(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(msg);
		alert.setContentText("");
		alert.show();
	}
	
	// Warning alert dialog
	public static void showWarningAlert(String msg) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(msg);
		alert.setContentText("");
		alert.show();
	}
	
	// Confirmation alert dialog
	public static boolean showConfirmationAlert(String msg) {
		Alert alert = new Alert(AlertType.CONFIRMATION);		
		alert.setTitle("Warning");
		alert.setHeaderText(msg);
		alert.setContentText("");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return false;
		}
		return true;
	}
}
