package application;

import java.util.function.Predicate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginForm {
	
	private GridPane grid;
	
	LoginForm(){
		
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(25,25,25,25));
		
		Text welcomeTxt = new Text("Sign In");
		welcomeTxt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(welcomeTxt, 1, 0);
		
		Label userNameLbl = new Label("Username:");
		grid.add(userNameLbl, 0, 2);
		
		TextField userNameTf = new TextField();
		userNameTf.setPromptText("Username");
		grid.add(userNameTf, 1, 2);
		
		Label passwordLbl = new Label("Password:");
		grid.add(passwordLbl, 0, 3);
		
		PasswordField passwordTf = new PasswordField();
		passwordTf.setPromptText("Pasword");
		grid.add(passwordTf, 1, 3);
		
		TextField showPassTf = new TextField();
//		showPassTf.setText(passwordTf.getText());
		
		CheckBox showPass = new CheckBox("Show password");
		grid.add(showPass, 1, 4);
		
		CheckBox forgetPass = new CheckBox("Forget password");
		grid.add(forgetPass, 1, 5);
		
		Predicate<CheckBox> pw = p -> p.isSelected();
		
		showPass.setOnAction(event -> {
			String temp = passwordTf.getText();
			if (pw.test(showPass)) {
				//
			}
		});
		
		Button loginBtn = new Button("Login");
		loginBtn.setPrefWidth(150);
		loginBtn.setId("loginBtn");
		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.BOTTOM_RIGHT);
		hBox.getChildren().add(loginBtn);
		grid.add(hBox, 1, 8);
		
	}
	
	public GridPane getGrid() {
		return this.grid;
	}
}
