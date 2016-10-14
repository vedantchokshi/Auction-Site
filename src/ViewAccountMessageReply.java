import java.util.ArrayList;
/*
 * Server replies with the User which contains the Users current bids/won items. The server also replies with a list of items that the user is selling(if any)
 */
public class ViewAccountMessageReply implements Message {
	private User user;
	private ArrayList<Item> sell;
	
	public ViewAccountMessageReply(User user, ArrayList<Item> sell) {
		this.user = user;
		this.sell = sell;
	}
	
	public User getUser() {
		return user;
	}
	
	public ArrayList<Item> getSell() {
		return sell;
	}
}
