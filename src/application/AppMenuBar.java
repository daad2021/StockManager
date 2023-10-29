package application;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.Pane;

public class AppMenuBar {
	
//	private HBox menuPane;
	private Pane menuPane;
	
	AppMenuBar(){
		buildMenuBar();
	}
	
	public void buildMenuBar() {
		menuPane = new Pane();
		menuPane.setMaxSize(Double.MAX_VALUE, 120);
				
		// Create the menu bar.
	    MenuBar menuBar = new MenuBar();

	    // Create the File menu.
	    Menu fileMenu = new Menu("File");
	    MenuItem open = new MenuItem("Open");
	    MenuItem close = new MenuItem("Close");
	    MenuItem save = new MenuItem("Save");
	    MenuItem exit = new MenuItem("Exit");
	    fileMenu.getItems().addAll(open, close, save, new SeparatorMenuItem(), exit);

	    // Mount the File menu on the menu bar.
	    menuBar.getMenus().add(fileMenu);
	    
	    exit.setOnAction(event -> {
	    	if (Message.showConfirmationAlert("Are you sure you want to exit?")) {
	    		System.exit(0);
	    	}   	
	    });

	    // Create the Options menu.
	    Menu optionsMenu = new Menu("Options");
	    
	    MenuItem invoice = new MenuItem("Generate invoice");
		MenuItem export = new MenuItem("Export");
		MenuItem refresh = new MenuItem("Refresh");
	    
	    optionsMenu.getItems().addAll(invoice, export, new SeparatorMenuItem(), refresh);

	    // Add Options menu to the menu bar.
	    menuBar.getMenus().add(optionsMenu);

	    // Create the Help menu.
	    Menu helpMenu = new Menu("Help");
	    MenuItem about = new MenuItem("About");
	    helpMenu.getItems().add(about);

	    // Add Help menu to the menu bar.
	    menuBar.getMenus().add(helpMenu);

	    // the response label to the center position.
	    menuPane.getChildren().add(menuBar);
	}

	public Pane getMenuBox() {
		return menuPane;
	}
}
