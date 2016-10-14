import java.io.Serializable;
import java.util.*;
/*
 * Defines an Item
 * Serializable so item can be sent over sockets
 */
public class Item implements Serializable, Comparable<Item> {
	private String title, description, category, vendorUsername;
	private User highestBidder;
	private Date startTime, endTime;
	private double reservePrice, currentPrice;
	private HashMap<User, Double> bidList = new HashMap<>();
	private Integer itemID;
	
	//Take in values to create the Item
	public Item(String title, double reservePrice, String description, String category, String vendorUsername, Date startTime, Date endTime, Integer itemID) {
		this.title = title;
		this.reservePrice = reservePrice;
		this.currentPrice = reservePrice;
		this.description = description;
		this.category = category;
		this.vendorUsername = vendorUsername;		
		this.startTime = startTime;
		this.endTime = endTime;
		this.itemID = itemID;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getVendorUsername() {
		return vendorUsername;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}
	
	public double getReservePrice() {
		return reservePrice;
	}
	
	public Integer getItemID() {
		return itemID;
	}
	
	//Gets status of the item for a given user
	public String getStatus(User user) {
		if(!isActive() && reservePrice == currentPrice) {
			return "ENDED";
		} else if (!isActive() && reservePrice < currentPrice && user.getUsername().equals(vendorUsername)){
			return "SOLD";
		} else if(!isActive() && reservePrice < currentPrice) {
			if(user.getUsername().equals(highestBidder.getUsername())) {
				return "WON";
			} else {
				return "LOST";
			}			
		} else if(isActive() && user.getUsername().equals(vendorUsername)) {
			return "ACTIVE";
		} else if(isActive() && reservePrice < currentPrice) {
			if(user.getUsername().equals(highestBidder.getUsername())) {
				return "WINNING";
			} else {
				return "LOSING";
			}
		}
		return null;
	}
	
	public User getHighestBidder() {
		return highestBidder;
	}
	
	public double currentPrice() {
		return currentPrice;
	}
	
	public boolean isActive() {
		return endTime.after(Calendar.getInstance().getTime());
	}
	
	public boolean hasBids() {
		return !bidList.isEmpty();
	}
	
	//Adds bids to the Item
	public void addBid(User user, Double bid) {
		currentPrice = bid;
		highestBidder = user;
		bidList.put(user, bid);
	}
	
	//Method now allows to compare items by ItemID which makes it easier to find items
    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;
        if (object != null && object instanceof Item)
        {
            sameSame = this.itemID.equals(((Item) object).itemID);
        }
        return sameSame;
    }

    //Allows to arrange items from items ending first to last
	@Override
	public int compareTo(Item item) {
		return (int) (endTime.getTime() - item.endTime.getTime());
	}
}
