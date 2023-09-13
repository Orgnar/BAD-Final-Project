package login;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Connect;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import master.User;

public class LoginMenu {
	Label loginTitleLBL, emailLBL, passwordLBL;
	TextField emailTF;
	PasswordField passwordPF;
	public Button loginBtn;
	public Button registerInsteadBtn;
	BorderPane bp, outerBP;
	GridPane gp;
	Scene scene;
	VBox loginMenuBTNs;
	Connect connect = Connect.getConnection();
	public BorderPane loginPage() {
		loginTitleLBL = new Label("Watches Dealer Login");
		loginTitleLBL.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		
		emailLBL = new Label("Email :");
		emailTF = new TextField();
		emailTF.setPromptText("Email Address");
		
		passwordLBL = new Label("Password: ");
		passwordPF = new PasswordField();
		passwordPF.setPromptText("Password");
		
		loginBtn = new Button("Login");
		loginBtn.setTextFill(Color.WHITE);
		loginBtn.setStyle("-fx-background-color: black;");
		
		registerInsteadBtn = new Button("Register Instead");
		registerInsteadBtn.setTextFill(Color.WHITE);
		registerInsteadBtn.setStyle("-fx-background-color: black;");
		
		emailLBL.setMinWidth(250);
		emailTF.setMinWidth(250);
		
		passwordLBL.setMinWidth(250);
		passwordPF.setMinWidth(250);
		loginBtn.setMinWidth(250);
		registerInsteadBtn.setMinWidth(250);
		
		gp = new GridPane();
		bp = new BorderPane();
		loginTitleLBL.setAlignment(Pos.CENTER);
		loginMenuBTNs = new VBox(10);
		loginMenuBTNs.getChildren().addAll(loginBtn, registerInsteadBtn);
		outerBP = new BorderPane();
		gp.setVgap(20);

		gp.add(loginTitleLBL, 1, 0);
		gp.add(emailLBL, 1, 1);
		gp.add(emailTF, 1, 2);
		gp.add(passwordLBL, 1, 3);
		gp.add(passwordPF, 1, 4);
		gp.add(loginMenuBTNs, 1, 5);
		bp.setTop(loginTitleLBL);
		bp.setCenter(gp);
		outerBP.setCenter(bp);

		BorderPane.setAlignment(bp, Pos.CENTER);
		BorderPane.setAlignment(outerBP, Pos.CENTER);
		BorderPane.setAlignment(loginTitleLBL, Pos.CENTER);
		loginTitleLBL.setMinWidth(150);
		bp.setPadding(new Insets(50, 50, 50, 50));
		outerBP.setPadding(new Insets(50, 50, 75, 75));
		
		loginMenuBTNs.setStyle("-fx-background-color: white;");
		bp.setStyle("-fx-background-color: white;");
		outerBP.setStyle("-fx-background-color: #c0fafa;");
		
		return outerBP;

	}
	public User LoginButtonAction() {
		User loginUser = null;
		// validate if email is empty or not
		if (emailTF.getText().equals("")) {
			Alert warningEmail = new Alert(AlertType.ERROR);
			warningEmail.setHeaderText("Error");
			warningEmail.setContentText("Email must be filled!");
			warningEmail.show();
			// validate if password is empty or not
		}
		else if (passwordPF.getText().equals("")) {
			Alert warningPassword = new Alert(AlertType.ERROR);
			warningPassword.setHeaderText("Error");
			warningPassword.setContentText("Password must be filled!");
			warningPassword.show();
		}else {
			String query = String.format("SELECT * FROM `user` WHERE `UserEmail` = '%s' AND `UserPassword` = '%s'", emailTF.getText(),passwordPF.getText());
			ResultSet rs = connect.executeQuery(query);
		try {
			while(rs.next()) {
			if(rs.getString("UserRole").equals(null)) {
				loginUser = new User(rs.getInt("UserID"),"");
			}else {
			loginUser = new User(rs.getInt("UserID"),rs.getString("UserRole"));
			}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
		return loginUser;
	}

}
