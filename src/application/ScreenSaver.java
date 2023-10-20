package application;

import java.util.TimerTask;

import javax.management.timer.Timer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class ScreenSaver {
	private GridPane pane;
	
	ScreenSaver(){
		buildScreenSaver();
	}
	
	public void buildScreenSaver() {
		pane = new GridPane();
//		pane.setPrefSize(1010, 800);
		CommonSource.setCenterPaneSize(pane);
		pane.setVgap(5);
		pane.setVgap(5);
		pane.setHgap(5);
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(3,3,3,3));
		pane.setId("screenSaverPane");
		
		// Add item to screen aver
		Image screenIcon = new Image(CommonSource.getIconPath() + "add-to-cart2.png");
		ImageView imageView = new ImageView(screenIcon);		
		imageView.setFitHeight(500); 
	    imageView.setFitWidth(550);
		
		Label label = new Label();
		label.setGraphic(imageView);
		label.setId("screenLabel");
		
		pane.add(label, 0, 0);
		
	}

	public GridPane getPane() {
		return pane;
	}

}
