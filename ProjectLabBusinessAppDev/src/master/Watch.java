package master;

public class Watch {

	private Integer watchID;
	private String watchBrand;
	private String watchName;
	private String watchPrice;
	private Integer watchStock;
	private Integer brandID;
	
	
	public Watch(Integer watchID, String watchBrand, String watchName, String watchPrice, Integer watchStock,
			Integer brandID) {
		super();
		this.watchID = watchID;
		this.watchBrand = watchBrand;
		this.watchName = watchName;
		this.watchPrice = watchPrice;
		this.watchStock = watchStock;
		this.brandID = brandID;
	}
	
	
	public String getWatchBrand() {
		return watchBrand;
	}


	public void setWatchBrand(String watchBrand) {
		this.watchBrand = watchBrand;
	}


	public void setBrandID(Integer brandID) {
		this.brandID = brandID;
	}


	public Integer getWatchID() {
		return watchID;
	}
	public void setWatchID(Integer watchID) {
		this.watchID = watchID;
	}
	public Integer getBrandID() {
		return brandID;
	}
	public void setBrandID(String watchBrand) {
		this.watchBrand = watchBrand;
	}
	public String getWatchName() {
		return watchName;
	}
	public void setWatchName(String watchName) {
		this.watchName = watchName;
	}
	public String getWatchPrice() {
		return watchPrice;
	}
	public void setWatchPrice(String watchPrice) {
		this.watchPrice = watchPrice;
	}
	public Integer getWatchStock() {
		return watchStock;
	}
	public void setWatchStock(Integer watchStock) {
		this.watchStock = watchStock;
	}
	
	
}
