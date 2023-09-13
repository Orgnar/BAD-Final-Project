package management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import jfxtras.labs.scene.control.window.Window;
import master.Cart;
import master.Watch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import jfxtras.labs.scene.control.window.Window;
import master.Brand;
import master.Watch;

public class manageWatch {
	TableView<Watch> tableWatch;
	BorderPane bPane;
	// Borderpane window;
	ArrayList<Watch> watchList;
	GridPane gPane;
	FlowPane bottomPane, row1, row2;
	Label nameLbl, priceLbl, stockLbl, brandLbl;
	TextField nameTF, priceTF;
	Spinner<Integer> stockSpinner;
	ComboBox<String> brandCMB;
	Button insertBtn, updateBtn, deleteBtn;
	Connect con = Connect.getConnection();
	Integer tempID = null;
	Integer brandID;
	String brandName;
	String tempName, tempPrice;
	String watchName, watchPrice;
	int tempStock, watchID, watchStock, watchPriceInteger;

	public BorderPane manageWatch() {
		bPane = new BorderPane();
		// window = new Window();
		gPane = new GridPane();
		watchList = new ArrayList();
		tableWatch = new TableView<Watch>();
		tableWatch.setMinWidth(700);
		TableColumn<Watch, Integer> col1 = new TableColumn<Watch, Integer>("Watch ID");
		TableColumn<Watch, String> col2 = new TableColumn<Watch, String>("Watch Name");
		TableColumn<Watch, String> col3 = new TableColumn<Watch, String>("Watch Brand");
		TableColumn<Watch, Integer> col4 = new TableColumn<Watch, Integer>("Watch Price");
		TableColumn<Watch, Integer> col5 = new TableColumn<Watch, Integer>("Watch Stock");

		col1.setCellValueFactory(new PropertyValueFactory<Watch, Integer>("WatchID"));
		col2.setCellValueFactory(new PropertyValueFactory<Watch, String>("WatchName"));
		col3.setCellValueFactory(new PropertyValueFactory<Watch, String>("watchBrand"));
		col4.setCellValueFactory(new PropertyValueFactory<Watch, Integer>("WatchPrice"));
		col5.setCellValueFactory(new PropertyValueFactory<Watch, Integer>("WatchStock"));

		tableWatch.getColumns().addAll(col1, col2, col3, col4, col5);
		tableWatch.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		nameLbl = new Label("Watch Name: ");
		nameTF = new TextField();
		nameTF.setPromptText("Name");
		priceTF = new TextField();
		priceTF.setPromptText("Price");
		priceLbl = new Label("Watch Price:");
		stockLbl = new Label("Watch Stock: ");
		stockSpinner = new Spinner<>(0, 1000, 0);
		brandLbl = new Label("Watch Brand: ");
		brandCMB = new ComboBox<String>();

		String queryy = "SELECT * FROM `brand`";
		ResultSet rs = con.executeQuery(queryy);

		try {
			while (rs.next()) {
				brandCMB.getItems().add(rs.getString("brandName"));
				System.out.println(rs.getString("brandName"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		insertBtn = new Button("Insert Watch");
		updateBtn = new Button("Update Watch");
		deleteBtn = new Button("Delete Watch");

		bottomPane = new FlowPane();
		gPane.add(nameLbl, 0, 0);
		gPane.add(nameTF, 1, 0);
		gPane.add(stockLbl, 0, 1);
		gPane.add(stockSpinner, 1, 1);
		gPane.add(priceLbl, 2, 0);
		gPane.add(priceTF, 3, 0);
		gPane.add(brandLbl, 2, 1);
		gPane.add(brandCMB, 3, 1);
		gPane.setHgap(20);
		gPane.setVgap(20);
		bottomPane.getChildren().add(insertBtn);
		bottomPane.getChildren().add(updateBtn);
		bottomPane.getChildren().add(deleteBtn);
		bottomPane.setHgap(20);

		bottomPane.setAlignment(Pos.CENTER);
		gPane.setAlignment(Pos.CENTER);

		bPane.setTop(tableWatch);
		bPane.setCenter(gPane);
		bPane.setBottom(bottomPane);
		bPane.setPadding(new Insets(10, 10, 10, 10));
		;
		// window.getContentPane().getChildren().add(bPane);
		// window.setResizableWindow(false);
		refreshTable();

		tableWatch.setOnMouseClicked((event) -> {
			TableSelectionModel<Watch> tableSelectionModel = tableWatch.getSelectionModel();
			tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			Watch watch = tableSelectionModel.getSelectedItem();
			String price = watch.getWatchPrice();
			watchID = watch.getWatchID();
			nameTF.setText(watch.getWatchName());
			priceTF.setText(price.substring(1));
			brandCMB.getSelectionModel().select(searchBrandName(watch.getBrandID()));
			stockSpinner.getValueFactory().setValue(watch.getWatchStock());

		});

		insertBtn.setOnAction((event) -> {
			String brandName = (String) brandCMB.getValue();
			watchName = nameTF.getText();
			watchPrice = priceTF.getText();
			watchPriceInteger = Integer.valueOf(watchPrice);
			watchStock = stockSpinner.getValue();
			brandID = searchBrandID(brandName);
			int price = Integer.valueOf(priceTF.getText());
			if (!nameTF.getText().endsWith(" Watch")) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error");
				error.setContentText("Watch Name must end with 'Watch'");
				error.show();
			} else if (price <= 0) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error");
				error.setContentText("Watch price must be higher than 0");
				error.show();
			} else if (watchStock <= 0) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error");
				error.setContentText("Stock must be higher than 0");
				error.show();
			} else if (brandID == 0) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error");
				error.setContentText("Choose a watch brand!");
				error.show();

			} else {
				insertWatch(watchID, brandID, watchName, watchPriceInteger, watchStock);
				Alert success = new Alert(AlertType.INFORMATION);
				success.setHeaderText("Message");
				success.setContentText("New watch successfully inserted!");
				refreshLayout();
			}
		});
		updateBtn.setOnAction((event) -> {
			String brandName = (String) brandCMB.getValue();
			watchName = nameTF.getText();
			watchPrice = priceTF.getText();
			watchPriceInteger = Integer.valueOf(watchPrice);
			watchStock = stockSpinner.getValue();
			brandID = searchBrandID(brandName);
			int price = Integer.valueOf(priceTF.getText());

			if(watchID == 0) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error");
				error.setContentText("You must select a watch!");
				error.show();
			}else if(!watchName.endsWith(" Watch")) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error");
				error.setContentText("Watch Name must end with ' Watch");
				error.show();
			}else if (price <= 0) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error");
				error.setContentText("Watch price must be higher than 0");
				error.show();
			} else if (watchStock <= 0) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error");
				error.setContentText("Stock must be higher than 0");
				error.show();
			} else if (brandID == 0) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error");
				error.setContentText("Choose a watch brand!");
				error.show();
			}else {
				updateWatch(watchID, brandID, watchName, price, watchStock);
				refreshLayout();
				Alert success = new Alert(AlertType.INFORMATION);
				success.setHeaderText("Message");
				success.setContentText("Watch Successfully updated!");
				success.show();
			}
		});
		deleteBtn.setOnAction((event) -> {
			if(watchID == 0) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error");
				error.setContentText("Choose a watch brand!");
				error.show();
			}else {
				deleteWatch();
				Alert success = new Alert(AlertType.INFORMATION);
				success.setHeaderText("Message");
				success.setContentText("Watch Successfully deleted!");
				success.show();
			}
			
		});

		return bPane;
	}
	
	public void refreshLayout() {
		watchID = 0;
		nameTF.setText("");
		priceTF.setText("");
		brandCMB.getSelectionModel().clearSelection();
		stockSpinner.getValueFactory().setValue(0);
	}

	public void refreshTable() {
		watchList.clear();
		getWatch();
		ObservableList<Watch> watchObs = FXCollections.observableArrayList(watchList);
		tableWatch.setItems(watchObs);
	}

	public int searchBrandID(String brandName) {
		Integer tempBrandID = 0;
		String query = String.format("Select `brandID` from `brand` Where `brandName` = '%s' ", brandName);
		ResultSet rs = con.executeQuery(query);
		try {
			while (rs.next()) {
				tempBrandID = rs.getInt("brandID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempBrandID;
	}

	public String searchBrandName(Integer brandID) {
		String tempBrandName = "";
		String query = String.format("Select `brandName` from `brand` Where `brandID` = '%d' ", brandID);
		ResultSet rs = con.executeQuery(query);
		try {
			while (rs.next()) {
				tempBrandName = rs.getString("brandName");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempBrandName;
	}

	public void getWatch() {
		// 2 TODO: get user from database button
		String query = "SELECT watchID, watchName,watchPrice,watchStock,watch.brandID, brand.BrandName as watchBrand FROM watch JOIN brand ON watch.brandID = brand.brandID";
		ResultSet rs = con.executeQuery(query);

		try {
			while (rs.next()) {
				Integer watchID = rs.getInt("WatchID");
				String watchBrand = rs.getString("watchBrand");
				Integer brandID = rs.getInt("watch.brandID");
				String watchName = rs.getString("WatchName");
				String watchPrice = "$" + rs.getString("WatchPrice");
				Integer watchStock = rs.getInt("WatchStock");

				Watch Watch = new Watch(watchID, watchBrand, watchName, watchPrice, watchStock, brandID);
				
				watchList.add(Watch);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertWatch(Integer watchID, Integer brandID, String watchName, Integer watchPrice,
			Integer watchStock) {

		String query = String.format("INSERT INTO `watch` VALUES ('%d','%d','%s','%d','%d')", 0, brandID, watchName,
				watchPrice, watchStock);
		con.executeUpdate(query);
		refreshTable();
	}

	public void updateWatch(Integer watchID, Integer brandID, String watchName, Integer watchPrice, Integer watchStock) {
		String query = String.format("UPDATE `watch` SET `BrandID`='%d',`WatchName`='%s',`WatchPrice`='%d',`WatchStock`='%d' WHERE `watchid` = '%d'", brandID,watchName,watchPrice,watchStock,watchID);
		con.executeUpdate(query);
		refreshTable();
	}

	public void deleteWatch() {
		String query = String.format("DELETE FROM watch WHERE watchid = %d", watchID);
		con.executeUpdate(query);
		refreshTable();
	}

}
