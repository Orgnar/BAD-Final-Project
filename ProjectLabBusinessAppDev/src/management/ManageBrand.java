package management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import master.Brand;

public class ManageBrand{
	Scene scene;
	TableView<Brand> brandTable;
	BorderPane bPane;
	FlowPane fPane, fPane1;
	GridPane gPane;
	Label brandNameLbl;
	TextField brandNameTF;
	Button insertBtn, updateBtn, deleteBtn;
	ScrollPane sPane;
	Connect con = Connect.getConnection();
	ArrayList<Brand>brandList;
	
	boolean clicked = false;
	Integer tempID = null; 
	
	public BorderPane manageBrandPage() {
		bPane = new BorderPane();
		fPane = new FlowPane();
		fPane1 = new FlowPane();
		gPane = new GridPane();
		brandList = new ArrayList<Brand>();
		brandNameLbl = new Label("Brand Name: ");
		brandNameTF = new TextField();
		brandNameTF.setPromptText("Brand Name");
		insertBtn = new Button("Insert Brand");
		updateBtn = new Button("Update Brand");
		deleteBtn =  new Button("Delete Brand");
		sPane = new ScrollPane();
		
		brandTable = new TableView<Brand>();
		TableColumn<Brand, Integer> col1 = new TableColumn<Brand, Integer>("Brand ID");
		TableColumn<Brand, String> col2 = new TableColumn<Brand, String>("Brand Name");
		brandTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		col1.setCellValueFactory(new PropertyValueFactory<Brand, Integer>("brandID"));
		col2.setCellValueFactory(new PropertyValueFactory<Brand, String>("brandName"));
		
		brandTable.getColumns().addAll(col1,col2);
		
		fPane.getChildren().add(brandNameLbl);
		fPane.getChildren().add(brandNameTF);
		
		
		fPane1.getChildren().add(insertBtn);
		fPane1.getChildren().add(updateBtn);
		fPane1.getChildren().add(deleteBtn);
		fPane1.setAlignment(Pos.BOTTOM_CENTER);
		fPane1.setHgap(10);
		
		gPane.add(brandTable, 0, 0);
		gPane.add(fPane, 0, 1); 
		gPane.add(fPane1, 0, 2);
		gPane.setHgap(15);
		gPane.setVgap(15);
		
		brandTable.setMinWidth(700);
		
		bPane.setPadding(new Insets(10));
		
		bPane.setCenter(gPane);
		gPane.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(fPane, Pos.CENTER);
		BorderPane.setAlignment(gPane, Pos.CENTER);
		sPane.setContent(bPane);
		refreshTable();
		
		brandTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Brand>() {


			@Override
			public void changed(ObservableValue<? extends Brand> observable, Brand oldValue, Brand newValue) {
				if(newValue != null) {
					brandNameTF.setText(newValue.getBrandName());
				}
			}});
		
		handle();
		return bPane;
	}
	public void refreshTable() {
		brandList.clear();
		getBrand();
		ObservableList<Brand> brandObs = FXCollections.observableArrayList(brandList); 
		brandTable.setItems(brandObs);
	}
	
	public void addData(Integer brandID, String name) {
//		String query = "INSERT INTO `brand`"+ "(`BrandID`, `BrandName`)" + " VALUES ('" + brandID + "', '" + name + "')";
		String query = String.format("INSERT INTO `brand` VALUES (%d, '%s')", brandID, name);
		con.executeUpdate(query);
	}
	public void updateData(String name, Integer id) {
		String query = String.format("UPDATE `brand` SET BrandName = '%s' WHERE BrandID = %d", name, id);
		con.executeUpdate(query);
	}
	
	public void deleteData(Integer id) {
		String query = String.format("DELETE FROM `brand` WHERE BrandID = %d", id);
		con.executeUpdate(query);
	}
	
	public void getBrand() {
		String query = "SELECT * FROM `brand`";
		ResultSet rs = con.executeQuery(query);

		try {
			while (rs.next()) {
				Integer brandID = rs.getInt("BrandID"); 
				String brandName = rs.getString("BrandName");
				
				Brand brand = new Brand(brandID, brandName);
				brandList.add(brand);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insert() {
		Integer id = null;
		String name = brandNameTF.getText();
		
		addData(id,name);
		refreshTable();
		brandNameTF.setText("");
	}
	
	public void update() {
		String name = brandNameTF.getText();
		updateData(name, tempID);
		refreshTable();
		brandNameTF.setText("");
	}
	
	public void delete() {
		deleteData(tempID);
		refreshTable();
		brandNameTF.setText("");
	}
	
	public void handle() {
		brandTable.setOnMouseClicked((event) -> {
			TableSelectionModel<Brand> TSM = brandTable.getSelectionModel();
			TSM.setSelectionMode(SelectionMode.SINGLE);
			Brand brand = TSM.getSelectedItem();
			
			tempID = brand.getBrandID();
			brandNameTF.setText(brand.getBrandName());
			clicked = true;
		});
		insertBtn.setOnAction((event)-> {
			if(brandNameTF.getText().equals("")) {
				Alert brandNameAlert = new Alert(AlertType.ERROR);
				brandNameAlert.setHeaderText("Error");
				brandNameAlert.setContentText("Watch Brand must be filled!");
				brandNameAlert.show();
			}else {
				Alert brandAdded = new Alert(AlertType.INFORMATION);
				brandAdded.setHeaderText("Message");
				brandAdded.setContentText("New Brand Successfully added!"); 
				brandAdded.show();
				insert();
			}
		});
		updateBtn.setOnAction((event)->{ 
			if(brandNameTF.getText().equals("")) {
				Alert brandNameAlert = new Alert(AlertType.ERROR);
				brandNameAlert.setHeaderText("Error");
				brandNameAlert.setContentText("Watch Brand must be filled!");
				brandNameAlert.show();
			}else if (clicked == false){
				Alert clickAlert = new Alert(AlertType.ERROR);
				clickAlert.setHeaderText("Error");
				clickAlert.setContentText("A brand must be selected!");
				clickAlert.show();
			}else {
				Alert deleteSuccess = new Alert(AlertType.INFORMATION);
				deleteSuccess.setHeaderText("Message");
				deleteSuccess.setContentText("Brand Successfully updated!");
				deleteSuccess.show();
				update();
			}
		});
		deleteBtn.setOnAction((event)->{
			if(clicked == false) {
				Alert clickAlert = new Alert(AlertType.ERROR);
				clickAlert.setHeaderText("Error");
				clickAlert.setContentText("A brand must be selected!");
				clickAlert.show();
			}else {
				Alert deleteSuccess = new Alert(AlertType.INFORMATION);
				deleteSuccess.setHeaderText("Message");
				deleteSuccess.setContentText("Brand Successfully Deleted!");
				deleteSuccess.show();
				delete();
			}
		});
	}



}
