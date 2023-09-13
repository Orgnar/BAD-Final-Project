package user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import database.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import jfxtras.labs.scene.control.window.Window;
import master.Cart;
import master.TransactionDetail;
import master.Watch;

public class BuyWatchForm {
	TableView<Watch> tableWatch;
	ArrayList<Watch> watchList;
	ArrayList<Cart> cartList;
	ArrayList<TransactionDetail> detailTransaction;
	TableView<Cart> cartTable;
	BorderPane bPane;
	Label selWatchLbl, quantityLbl;
	Spinner<Integer> qtySpinner;
	Button addWatchBtn, clearBtn, checkoutBtn, noBtn, yesBtn;
	FlowPane fBottomPane, fCenterPane;
	GridPane gCenterPane;
	Window window;
	Connect con = Connect.getConnection();

	int selectedWatchID;
	int userID;
	int amount;
	int qty;
	int total;

	public BorderPane BuyWatch(Integer UserID) {
		userID = UserID;
		bPane = new BorderPane();
		window = new Window();
		fBottomPane = new FlowPane();
		gCenterPane = new GridPane();
		fCenterPane = new FlowPane();
		selWatchLbl = new Label("Selected Watch : None ");
		quantityLbl = new Label("Quantity: ");
		qtySpinner = new Spinner<>(0, 100, 0, 1);
		addWatchBtn = new Button("Add Watch to Cart");
		clearBtn = new Button("Clear Cart");
		checkoutBtn = new Button("Checkout");
		watchList = new ArrayList<Watch>();
		cartList = new ArrayList<Cart>();
		detailTransaction = new ArrayList<TransactionDetail>();
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

		cartTable = new TableView<Cart>();
		cartTable.setMinWidth(700);
		TableColumn<Cart, Integer> col6 = new TableColumn<Cart, Integer>("User ID");
		TableColumn<Cart, Integer> col7 = new TableColumn<Cart, Integer>("Watch ID");
		TableColumn<Cart, Integer> col8 = new TableColumn<Cart, Integer>("Quantity");

		col6.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("UserID"));
		col7.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("WatchID"));
		col8.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("Quantity"));

		cartTable.getColumns().addAll(col6, col7, col8);
		cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		fBottomPane.getChildren().add(clearBtn);
		fBottomPane.getChildren().add(checkoutBtn);
		fBottomPane.setAlignment(Pos.BOTTOM_CENTER);
		fBottomPane.setPadding(new Insets(10));
		fBottomPane.setHgap(20);
		fBottomPane.setVgap(20);

		fCenterPane.getChildren().add(quantityLbl);
		fCenterPane.getChildren().add(qtySpinner);
		fCenterPane.getChildren().add(addWatchBtn);
		fCenterPane.setHgap(10);
		fCenterPane.setVgap(10);

		gCenterPane.add(tableWatch, 2, 0);
		gCenterPane.add(selWatchLbl, 2, 1);
		gCenterPane.add(fCenterPane, 2, 2);
		gCenterPane.add(cartTable, 2, 3);
		gCenterPane.setHgap(15);
		gCenterPane.setVgap(15);

		bPane.setCenter(gCenterPane);
		gCenterPane.setAlignment(Pos.CENTER);
		bPane.setBottom(fBottomPane);

		refreshTable();
		refreshCartTable();
		addWatchBtn.setOnAction(e -> {
			qty = qtySpinner.getValue();
			addData(qty);
		});
		clearBtn.setOnAction(e -> {
			clearCartTable();
		});
		checkoutBtn.setOnAction(e -> {
			validateCheckOut();
		});
		tableWatch.setOnMouseClicked((event) -> {
			TableSelectionModel<Watch> tableSelectionModel = tableWatch.getSelectionModel();
			tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			Watch watch = tableSelectionModel.getSelectedItem();
			amount = watch.getWatchStock();
			selWatchLbl.setText("Selected: " + watch.getWatchName());
			selectedWatchID = watch.getWatchID();
		});

		return bPane;
	}

	public void refreshTable() {
		watchList.clear();
		getWatch();
		ObservableList<Watch> watchObs = FXCollections.observableArrayList(watchList);
		tableWatch.setItems(watchObs);
	}

	public void refreshCartTable() {
		cartList.clear();
		getCart();
		ObservableList<Cart> cartObs = FXCollections.observableArrayList(cartList);
		cartTable.setItems(cartObs);
	}

	public void clearCartTable() {
		Alert alertQuantity = new Alert(AlertType.CONFIRMATION);
		alertQuantity.setHeaderText("CONFIRM");
		alertQuantity.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alertQuantity.setContentText("Are you sure to clear cart?");
		alertQuantity.getButtonTypes();
		Optional<ButtonType> confirmation = alertQuantity.showAndWait();
		if (confirmation.get() == ButtonType.NO) {
			alertQuantity.close();
		} else if (confirmation.get() == ButtonType.YES) {
			deleteCartTable();
		}
	}

	public void deleteCartTable() {
		String query = "DELETE FROM `cart`";
		con.executeUpdate(query);
		refreshCartTable();
	}

	public void addData(int quantity) {

		if (selectedWatchID == 0) {
			Alert alertSelection = new Alert(AlertType.ERROR);
			alertSelection.setHeaderText("Error!");
			alertSelection.setContentText("You must select a watch first!");
			alertSelection.show();
		}

		else if (quantity > amount) {
			Alert alertQuantity = new Alert(AlertType.ERROR);
			alertQuantity.setHeaderText("Quantity error!");
			alertQuantity.setContentText("Quantity must not be higher than stock");
			alertQuantity.show();
		} else if (quantity == 0) {
			Alert alertQuantity = new Alert(AlertType.ERROR);
			alertQuantity.setHeaderText("Quantity error!");
			alertQuantity.setContentText("Quantity must more than 0");
			alertQuantity.show();
		}

		else {
			System.out.println(selectedWatchID);
			String query = String.format(
					"INSERT INTO `cart` " + "(`userId`,`watchID`,`quantity`)" + " VALUES ('%d' , '%d', '%d')", userID,
					selectedWatchID, quantity);

			con.executeUpdate(query);
			refreshCartTable();
		}

	}

	public void refreshBuyLayout() {
		refreshCartTable();
		amount = 0;
		selWatchLbl.setText("Selected: None");
		selectedWatchID = 0;
	}

	public void validateCheckOut() {
		total = 0;
		System.out.println(total);
		
		String query = "SELECT COUNT(UserID) AS `totalBrand` FROM `cart`";
		ResultSet rs = con.executeQuery(query);
		try {
			while (rs.next()) {
				total = rs.getInt("totalBrand");
				System.out.println(total);
			}
			if (total == 0) {
				Alert alertQuantity = new Alert(AlertType.ERROR);
				alertQuantity.setHeaderText("Cart error!");
				alertQuantity.setContentText("Cart must not be empty!");
				alertQuantity.show();
			} else {
				checkOut();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}

	

	public void checkOut() {
		Alert alertQuantity = new Alert(AlertType.CONFIRMATION);
		alertQuantity.setHeaderText("CONFIRM");
		alertQuantity.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alertQuantity.setContentText("Are you sure to checkout?");
		alertQuantity.getButtonTypes();
		Optional<ButtonType> confirmation = alertQuantity.showAndWait();
		if (confirmation.get() == ButtonType.NO) {
			alertQuantity.close();
		} else if (confirmation.get() == ButtonType.YES) {
			String dateQuery = "Select CURRENT_DATE";
			String date = "";
			int transID = 0;
			ResultSet rs = con.executeQuery(dateQuery);
			try {
				while (rs.next()) {
					date = rs.getString("CURRENT_DATE");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String headerTransactionQuery = String.format(
					"INSERT INTO `headertransaction`(`TransactionID`, `UserID`, `TransactionDate`) VALUES ('%d','%d','%s')",
					0, userID, date);
			con.executeUpdate(headerTransactionQuery);

			String getTransIDQuery = "Select MAX(TransactionID) AS CurrentTransactionID FROM `headerTransaction`";
			ResultSet rsTransID = con.executeQuery(getTransIDQuery);
			try {
				while (rsTransID.next()) {
					transID = rsTransID.getInt("CurrentTransactionID");
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String query = "Select * FROM `Cart`";
			ResultSet rs1 = con.executeQuery(query);
			try {
				while (rs1.next()) {
					System.out.println("test");
					Integer watchID = rs1.getInt("watchID");
					Integer qty = rs1.getInt("quantity");
					detailTransaction.add(new TransactionDetail(transID, watchID, qty));
				}
			} catch (SQLException e) {
				System.out.println();
				e.printStackTrace();
			}
			insertDetailTrans();
			deleteCartTable();
			Alert success = new Alert(AlertType.INFORMATION);
			success.setHeaderText("Successful checkout");
			success.show();

			refreshBuyLayout();
		}
	}

	public void insertDetailTrans() {
		int transID, watchID, qty;
		for (int i = 0; i < detailTransaction.size() ; i++) {
			transID = detailTransaction.get(i).gettransactionID();
			watchID = detailTransaction.get(i).getwatchID();
			qty = detailTransaction.get(i).getquantity();
			String detailTransactionQuery = String.format(
					"INSERT INTO `detailtransaction`(`TransactionID`, `WatchID`, `Quantity`) VALUES ('%d',%d,'%d')",
					transID, watchID, qty);
			con.executeUpdate(detailTransactionQuery);
		}
		detailTransaction.clear();
	}

	public void getWatch() {
		// 2 TODO: get watch from database button
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
			// TODO Auto-generated catch blockrs
			e.printStackTrace();
		}

	}

	public void getCart() {
		String query = "Select * FROM `Cart`";
		ResultSet rs = con.executeQuery(query);
		try {
			while (rs.next()) {
				Integer userID = rs.getInt("userID");
				Integer watchID = rs.getInt("watchID");
				Integer qty = rs.getInt("quantity");
				Cart cart = new Cart(userID, watchID, qty);
				cartList.add(cart);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
