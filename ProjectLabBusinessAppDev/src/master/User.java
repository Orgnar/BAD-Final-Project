package master;

public class User {
	private Integer UserID;
	private String UserRole;
	public User(Integer userID, String userRole) {
		super();
		UserID = userID;
		UserRole = userRole;
	}
	public Integer getUserID() {
		return UserID;
	}
	public void setUserID(Integer userID) {
		UserID = userID;
	}
	public String getUserRole() {
		return UserRole;
	}
	public void setUserRole(String userRole) {
		UserRole = userRole;
	}
	
	
	
	

}
