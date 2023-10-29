package application;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.layout.BorderPane;

public class App {
	private static BorderPane borderPane;
	private static boolean isRestoreComplete = false;
	public App() throws SQLException {
		buildApp();
		checkCartBuffer(5000);
//		restore();
	}
	
	public static void buildApp() throws SQLException {
		borderPane = new BorderPane();
//		borderPane.setMinSize(1120,650);
		CommonSource.setCenterPaneSize(borderPane);
//		borderPane.setTop(new AppMenuBar().getMenuBox());
		borderPane.setTop(new AppMenuBar().getMenuBox());
		borderPane.setCenter(new ScreenSaver().getPane());
		borderPane.setLeft(new NavBar().getNavBar());
		borderPane.setBottom(new StatusBar().getStatusBar());		
	}
	
	
	public static BorderPane getBorderPane() {
		return borderPane;
	}
	

	public static void checkCartBuffer(int countDown) {
		 Timer myTimer = new Timer();
	     myTimer.schedule(new TimerTask(){

	       @Override
	       public void run() {
	    	   try {
				if (!SalesPage.isCartBufferEmpty()) {
				   SalesPage.rollBackCartBuffer();
				   isRestoreComplete = true;
				   System.out.println("Restore successful!");
//				   Message.showInfoAlert("Stock table has been restored");
			   }
			} catch (SQLException e) {
				e.printStackTrace();
			}
	       }
	     }, countDown);
	}
	
//	//
//	public static void restore() {
//		int count = 0;
//		do {
//			checkCartBuffer(6000);
//			if (isRestoreComplete != true) {
//				checkCartBuffer(10000);
//				count++;
//			}
//			else {
//				break;
//			}
//		}while(count <= 3);
//	}
}
