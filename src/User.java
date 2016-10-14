import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
/*
 * Defines an Item
 * Serializable so item can be sent over sockets
 */
public class User implements Serializable {
	private String name, surname, username, password;
	private HashMap<Item, Double> bids = new HashMap<>();
	//Boolean which is set to active if the user is logged on
	private boolean active;
	//ArrayList which contains all the notifications for the User
	private ArrayList<String> notifications = new ArrayList<>();
	//ArrayList which contains the titles of all the items won by the User
	private ArrayList<String> itemsWon = new ArrayList<>();
	
	//Takes values to create the User
	public User(String name, String surname, String username, String password) {
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
		this.active = false;
	}

	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	//Gets all the notifications
	public ArrayList<String> getNotfications() {
		return notifications;
	}
	
	//Adds notifications to the ArrayList Notifications
	public void addNotification(String message) {
		notifications.add(message);
	}
	
	//Clears all the notifications
	public void clearNotifications() {
		notifications = new ArrayList<>();
	}
	
	//Gets all the Items won
	public ArrayList<String> getItemsWon() {
		return itemsWon;
	}
	
	//Adds won Item Titles to the ArrayList
	public void addWonItem(String itemTitle) {
		itemsWon.add(itemTitle);
	}
	
	//Gets if the user is active
	public boolean isActive() {
		return active;
	}
	
	//Sets if the user is active or inactive according to when the user signs in and out
	public void setActive(boolean b) {
		active = b;
	}
	
	//Contains the last bid the User placed on an Item
	public HashMap<Item, Double> getBids() {
		return bids;
	}
	
	//Adds bids
	public void addBids(Item item, Double bid) {
		bids.put(item, bid);
	}
	
	//Method now allows to compare items by Username which makes it easier to find if 2 Users are equal
    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;
        if (object != null && object instanceof User)
        {
            sameSame = this.username.equals(((User) object).username);
        }
        return sameSame;
    }
}
