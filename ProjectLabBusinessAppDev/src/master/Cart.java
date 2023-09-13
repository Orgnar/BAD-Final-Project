package master;

public class Cart {
	private Integer userID;
	private Integer watchID;
	private Integer Quantity;
	public Cart(Integer userID, Integer watchID, Integer Quantity) {
		super();
		this.userID = userID;
		this.watchID = watchID;
		this.Quantity = Quantity;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public Integer getWatchID() {
		return watchID;
	}
	public void setWatchID(Integer watchID) {
		this.watchID = watchID;
	}
	public Integer getQuantity() {
		return Quantity;
	}
	public void setQuantity(Integer Quantity) {
		this.Quantity = Quantity;
	}


	
}
