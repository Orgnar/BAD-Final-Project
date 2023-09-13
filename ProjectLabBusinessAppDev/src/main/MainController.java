package main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import login.LoginMenu;
import login.RegisterMenu;
import management.ManageBrand;
import management.StaffMainForm;
import management.manageWatch;
import user.BuyWatchForm;
import user.CustomerMainForm;
import user.CustomerTransactionHistory;
import database.Connect;
import login.*;
import management.*;
import master.*;

public class MainController extends Application {
	public Stage mainStage = new Stage();

	LoginMenu loginPage = new LoginMenu();
	RegisterMenu registerPage = new RegisterMenu();
	CustomerMainForm custMenu = new CustomerMainForm();
	StaffMainForm staffMenu = new StaffMainForm();
	Integer UserID;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginMenu();
	}

	public void LoginMenu() {
		mainStage.setHeight(500);
		mainStage.setWidth(500);
		mainStage.setTitle("Login");
		mainStage.setScene(scene(loginPage.loginPage()));
		mainStage.show();
		loginPage.loginBtn.setOnAction((event) -> {
			String userRole = "";
			try {
				userRole = loginPage.LoginButtonAction().getUserRole();
			} catch (Exception e) {
				Alert loginFail = new Alert(AlertType.ERROR);
				loginFail.setHeaderText("Failed Login");
				loginFail.setContentText("Incorrect email or password!");
				loginFail.show();
			}
			if (userRole.equals("Admin")) {
				UserID = loginPage.LoginButtonAction().getUserID();
				staffMenu();
			} else if (userRole.equals("Customer")) {
				UserID = loginPage.LoginButtonAction().getUserID();
				customerMenu();
			}
		});

		loginPage.registerInsteadBtn.setOnAction((event) -> {
			registerMenu();
		});

	}

	public void registerMenu() {
		mainStage.setScene(scene(registerPage.registerPage()));
		mainStage.setTitle("Register");
		mainStage.setHeight(700);
		mainStage.setWidth(600);

		registerPage.backToLoginBTN.setOnAction((event1) -> {
			LoginMenu();
		});

		registerPage.regBtn.setOnAction((event) -> {
			boolean isSuccessful = false;
			isSuccessful = registerPage.registerButtonAction();
			if(isSuccessful == true) {
				Alert loginFail = new Alert(AlertType.INFORMATION);
				loginFail.setHeaderText("Message");
				loginFail.setContentText("Register Successful!");
				loginFail.show();
				LoginMenu();
			}
		});
	}

	public void customerMenu() {
		mainStage.setHeight(800);
		mainStage.setWidth(1500);
		mainStage.setTitle("Main Page");
		mainStage.setScene(scene(custMenu.CustomerMainForm()));
		custMenu.buyWatchMI.setOnAction((event) -> {
			BuyWatchForm buyWatchMenu = new BuyWatchForm();
			custMenu.bp.setCenter(buyWatchMenu.BuyWatch(UserID));

		});
		custMenu.myTransHistoryMI.setOnAction((event) -> {
			CustomerTransactionHistory cusTransHist = new CustomerTransactionHistory();
			custMenu.bp.setCenter(cusTransHist.historyPage(UserID));
		});
		custMenu.logoutMI.setOnAction((event) -> {
			mainStage.close();
			LoginMenu();
		});
	}

	public void staffMenu() {
		mainStage.setHeight(800);
		mainStage.setWidth(1500);
		mainStage.setTitle("Main Page");
		mainStage.setScene(scene(staffMenu.staffPage()));
		staffMenu.manageWatchMI.setOnAction((event) -> {
			manageWatch manageWatch = new manageWatch();
			staffMenu.bp.setCenter(manageWatch.manageWatch());
		});

		staffMenu.manageBrandMI.setOnAction((event) -> {
			ManageBrand manageBrandMenu = new ManageBrand();
			staffMenu.bp.setCenter(manageBrandMenu.manageBrandPage());

		});
		staffMenu.logoutMI.setOnAction((event) -> {
			mainStage.close();
			LoginMenu();
		});

	}

	public Scene scene(Parent x) {
		Scene sc = new Scene(x);
		return sc;
	}

}
