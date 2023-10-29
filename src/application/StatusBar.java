package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StatusBar {
	
	private HBox statusBar;
	
	StatusBar(){
		buildStatusBar();
	}
	
	public void buildStatusBar() {
		statusBar = new HBox(15);
		statusBar.setPrefSize(1000, 20);
		statusBar.setAlignment(Pos.TOP_CENTER);
		statusBar.setPadding(new Insets(5,5,2,5));
		statusBar.getStyleClass().add("statusBar");
		
		Label status = new Label("Status: App running...");
		statusBar.getChildren().add(status);
	}

	public HBox getStatusBar() {
		return statusBar;
	}
}
