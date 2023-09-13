package user;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.window.Window;
import master.TransactionDetail;
import master.TransactionHeader;
import master.TransactionHistoryDetail;
import master.Watch;

public class CustomerTransactionHistory {
	BorderPane bPane;
	TableView<TransactionHeader> transHeaderTable;
	ArrayList<TransactionHeader> transHeaderArray;
	ArrayList<TransactionHistoryDetail> transHistoryDetailArray;
	TableView<TransactionHistoryDetail> transDetailTable;
	Label selectedTransaction;
	Label currentSelection;
	FlowPane selections, topPane, bottomPane;
	Window window;
	VBox vbox;
	GridPane gPane;
	Integer UserID, headerTransID;
	Connect con = Connect.getConnection();

	public BorderPane historyPage(int userID) {
		UserID = userID;
		System.out.println(UserID);
		bPane = new BorderPane();
		window = new Window();
		gPane = new GridPane();
		transHeaderArray = new ArrayList<TransactionHeader>();
	    transHistoryDetailArray = new ArrayList<TransactionHistoryDetail>();
		transHeaderTable = new TableView<TransactionHeader>();
		transHeaderTable.setMinWidth(1450);
		TableColumn<TransactionHeader, Integer> head1 = new TableColumn<TransactionHeader, Integer>("TransactionID");
		TableColumn<TransactionHeader, Integer> head2 = new TableColumn<TransactionHeader, Integer>("User ID");
		TableColumn<TransactionHeader, String> head3 = new TableColumn<TransactionHeader, String>("Transaction Date");
		
		head1.setCellValueFactory(new PropertyValueFactory<TransactionHeader, Integer>("transactionID"));
		head2.setCellValueFactory(new PropertyValueFactory<TransactionHeader, Integer>("userID"));
		head3.setCellValueFactory(new PropertyValueFactory<TransactionHeader, String>("transactionDate"));
		
		transHeaderTable.getColumns().addAll(head1, head2, head3);
		transHeaderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		selectedTransaction = new Label("Selected Transaction: ");
		currentSelection = new Label("None");
		selections = new FlowPane();
		selections.getChildren().add(selectedTransaction);
		selections.getChildren().add(currentSelection);

		transDetailTable = new TableView<TransactionHistoryDetail>();
		transDetailTable.setMinWidth(1450);
		TableColumn<TransactionHistoryDetail, Integer> detail1 = new TableColumn<TransactionHistoryDetail, Integer>(
				"TransactionID");
		TableColumn<TransactionHistoryDetail, Integer> detail2 = new TableColumn<TransactionHistoryDetail, Integer>(
				"Watch Id");
		TableColumn<TransactionHistoryDetail, String> detail3 = new TableColumn<TransactionHistoryDetail, String>(
				"Watch Name");
		TableColumn<TransactionHistoryDetail, String> detail4 = new TableColumn<TransactionHistoryDetail, String>(
				"Watch Brand");
		TableColumn<TransactionHistoryDetail, String> detail5 = new TableColumn<TransactionHistoryDetail, String>(
				"Watch Price");
		TableColumn<TransactionHistoryDetail, Integer> detail6 = new TableColumn<TransactionHistoryDetail, Integer>(
				"Quantity");
		TableColumn<TransactionHistoryDetail, String> detail7 = new TableColumn<TransactionHistoryDetail, String>(
				"Sub Total");
		
		detail1.setCellValueFactory(new PropertyValueFactory<TransactionHistoryDetail, Integer>("transactionID"));
		detail2.setCellValueFactory(new PropertyValueFactory<TransactionHistoryDetail, Integer>("watchID"));
		detail3.setCellValueFactory(new PropertyValueFactory<TransactionHistoryDetail, String>("watchName"));
		detail4.setCellValueFactory(new PropertyValueFactory<TransactionHistoryDetail, String>("watchBrandName"));
		detail5.setCellValueFactory(new PropertyValueFactory<TransactionHistoryDetail, String>("watchPrice"));
		detail6.setCellValueFactory(new PropertyValueFactory<TransactionHistoryDetail, Integer>("quantity"));
		detail7.setCellValueFactory(new PropertyValueFactory<TransactionHistoryDetail, String>("subTotal"));

		transDetailTable.getColumns().addAll(detail1, detail2, detail3, detail4, detail5, detail6, detail7);
		detail1.setMinWidth(100);
		detail2.setMinWidth(100);
		detail3.setMinWidth(300);
		detail4.setMinWidth(300);
		detail5.setMinWidth(300);
		detail6.setMinWidth(100);
		detail7.setMinWidth(250);

		topPane = new FlowPane();
		bottomPane = new FlowPane();
		vbox = new VBox(10);

//		topPane.getChildren().add(transHeaderTable);
//		bottomPane.getChildren().add(transDetailTable);

		gPane.add(transHeaderTable, 1, 0);
		gPane.add(selections, 1, 1);
		gPane.add(transDetailTable, 1, 3);

		gPane.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(gPane, Pos.CENTER);

		bPane.setCenter(gPane);
		bPane.setPadding(new Insets(10, 10, 10, 10));
		;
		transHeaderTableData();
		transHeaderTable.setOnMouseClicked((event) -> {
			TableSelectionModel<TransactionHeader> tableSelectionModel = transHeaderTable.getSelectionModel();
			tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
			TransactionHeader transHeader = tableSelectionModel.getSelectedItem();
			headerTransID = transHeader.getTransactionID();
			currentSelection.setText("Transaction " + headerTransID);
			refreshHistoryDetail();
		});

		return bPane;
	}
	
	public void transHeaderTableData() {
		transHeaderArray.clear();
		getTransaction();
		ObservableList<TransactionHeader> transHeadObs = FXCollections.observableArrayList(transHeaderArray);
		transHeaderTable.setItems(transHeadObs);
	}
	
	
	public void refreshHistoryDetail() {
		transHistoryDetailArray.clear();
		getTransHistoryDetail();
		ObservableList<TransactionHistoryDetail> hisDetailObs = FXCollections.observableArrayList(transHistoryDetailArray);
		transDetailTable.setItems(hisDetailObs);
	}

	public void getTransHistoryDetail() {
		String query = String.format(
				"SELECT\r\n" + 
				"    transactionID,\r\n" + 
				"    detailtransaction.watchID AS watchID,\r\n" + 
				"    watch.watchName AS watchName,\r\n" + 
				"    brand.brandName AS BrandName,\r\n" + 
				"    watch.watchPrice AS watchPrice,\r\n" + 
				"    SUM(quantity) AS TotalQuantity,\r\n" + 
				"    (\r\n" + 
				"        watch.watchPrice * SUM(quantity)\r\n" + 
				"    )AS subTotal\r\n" + 
				"FROM\r\n" + 
				"    `detailtransaction`\r\n" + 
				"JOIN Watch ON detailtransaction.WatchID = watch.WatchID\r\n" + 
				"JOIN brand ON watch.brandID = brand.brandID\r\n" + 
				"WHERE\r\n" + 
				"    detailtransaction.TransactionID = %d\r\n" + 
				"GROUP BY\r\n" + 
				"    transactionID,watchID",
				headerTransID);
		ResultSet rs = con.executeQuery(query);
		try {
			while (rs.next()) {
				Integer transactionID = rs.getInt("transactionID");
				Integer watchID = rs.getInt("watchID");
				String watchName = rs.getString("watchID");
				String watchBrandName = rs.getString("brandName");
				String watchPrice = "$" + rs.getInt("watchPrice");
				Integer quantity = rs.getInt("totalQuantity");
				String subTotal = "$" + rs.getString("subTotal");
				TransactionHistoryDetail transHisDetail = new TransactionHistoryDetail(transactionID, watchID, quantity,
						watchPrice, subTotal, watchName, watchBrandName);
				transHistoryDetailArray.add(transHisDetail);
				System.out.println("test");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void getTransaction() {
		String query = String.format("SELECT * FROM headertransaction WHERE UserID = %d ",UserID);
		ResultSet rs = con.executeQuery(query);
			try {
				while(rs.next()) {
					Integer transactionID = rs.getInt("TransactionID");
					Integer userID = rs.getInt("userID");
					String transactionDate = rs.getString("transactionDate");
					TransactionHeader transHead = new TransactionHeader(transactionID, userID, transactionDate) ;
					transHeaderArray.add(transHead);
					System.out.println("test");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
		
}
