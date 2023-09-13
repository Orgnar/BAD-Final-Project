package management;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class StaffMainForm {
	Scene scene;
	public BorderPane bp;
	MenuBar menuBar;
	Menu userMenu,managementMenu;
	public MenuItem logoutMI;
	public MenuItem manageWatchMI;
	public MenuItem manageBrandMI;
	
	public BorderPane staffPage() {
		bp = new BorderPane();
		menuBar = new MenuBar();
		userMenu = new Menu("User");
		managementMenu = new Menu("Management");
		
		logoutMI = new MenuItem("Log Out");
		manageWatchMI = new MenuItem("Manage Watch");
		manageBrandMI = new MenuItem("Manage Brand");
		
		menuBar.getMenus().add(userMenu);
		menuBar.getMenus().add(managementMenu);
		
		//add userMenu's item;
		userMenu.getItems().add(logoutMI);
		// add transMenu's item;
		managementMenu.getItems().add(manageWatchMI);
		managementMenu.getItems().add(manageBrandMI);
		
		
		bp.setTop(menuBar);
		
		return bp;
	}

}
