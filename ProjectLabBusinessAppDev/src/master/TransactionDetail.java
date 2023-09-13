package master;

public class TransactionDetail {
	private Integer transactionID,watchID,quantity;

	public TransactionDetail(Integer transactionID, Integer watchID, Integer quantity) {
		super();
		this.transactionID = transactionID;
		this.watchID = watchID;
		this.quantity = quantity;
	}

	public Integer gettransactionID() {
		return transactionID;
	}

	public void settransactionID(Integer transactionID) {
		this.transactionID = transactionID;
	}

	public Integer getwatchID() {
		return watchID;
	}

	public void setwatchID(Integer watchID) {
		this.watchID = watchID;
	}

	public Integer getquantity() {
		return quantity;
	}

	public void setquantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
	

}
