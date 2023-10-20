package application;
	

import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application{
	public static Stage stage;
		
	public static void main(String ...args) {
		launch(args);
	}
	
	
	public void start(Stage stage) throws SQLException {
		
//		String css = this.getClass().getResource("application.css").toExternalForm();
//		Scene scene = new Scene(new LoginForm().getGrid(), 300, 250);
//		scene.getStylesheets().add(css);
//		  stage.initStyle(StageStyle.TRANSPARENT);
//		  stage.initStyle(StageStyle.UNDECORATED);
		
//		  int width = (int) Screen.getPrimary().getBounds().getWidth();
//		  int height = (int) Screen.getPrimary().getBounds().getHeight();
		
		String css = this.getClass().getResource("application.css").toExternalForm();
		Scene scene = new Scene(new App().getBorderPane(), 1185, 650); //**
//		Scene scene = new Scene(new DashBoard().getPane(), 1200, 650);
//		Scene scene = new Scene(new App().getBorderPane(), width, height);
		scene.getStylesheets().add(css);
		
		Application.setUserAgentStylesheet(STYLESHEET_MODENA);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();
	}

}