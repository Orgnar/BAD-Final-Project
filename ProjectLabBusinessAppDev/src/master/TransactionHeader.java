package master;

import java.sql.Date;

public class TransactionHeader {
	private Integer transactionID,userID;
	private String transactionDate;
	
	public TransactionHeader(Integer transactionID, Integer userID, String transactionDate) {
		this.transactionID = transactionID;
		this.userID = userID;
		this.transactionDate = transactionDate;
	}
	public Integer getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(Integer transactionID) {
		this.transactionID = transactionID;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	
	
	
}
