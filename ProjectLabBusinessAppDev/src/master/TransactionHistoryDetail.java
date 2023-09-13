package master;

public class TransactionHistoryDetail {
private Integer transactionID,watchID,quantity;
private String watchPrice,subTotal,watchName,watchBrandName;

public TransactionHistoryDetail(Integer transactionID, Integer watchID, Integer quantity, String watchPrice,
		String subTotal, String watchName, String watchBrandName) {
	this.transactionID = transactionID;
	this.watchID = watchID;
	this.quantity = quantity;
	this.watchPrice = watchPrice;
	this.subTotal = subTotal;
	this.watchName = watchName;
	this.watchBrandName = watchBrandName;
}
public Integer getTransactionID() {
	return transactionID;
}
public void setTransactionID(Integer transactionID) {
	this.transactionID = transactionID;
}
public Integer getWatchID() {
	return watchID;
}
public void setWatchID(Integer watchID) {
	this.watchID = watchID;
}
public Integer getQuantity() {
	return quantity;
}
public void setQuantity(Integer quantity) {
	this.quantity = quantity;
}
public String getWatchPrice() {
	return watchPrice;
}
public void setWatchPrice(String watchPrice) {
	this.watchPrice = watchPrice;
}
public String getSubTotal() {
	return subTotal;
}
public void setSubTotal(String subTotal) {
	this.subTotal = subTotal;
}
public String getWatchName() {
	return watchName;
}
public void setWatchName(String watchName) {
	this.watchName = watchName;
}
public String getWatchBrandName() {
	return watchBrandName;
}
public void setWatchBrandName(String watchBrandName) {
	this.watchBrandName = watchBrandName;
}

}
