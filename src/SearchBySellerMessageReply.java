import java.util.ArrayList;
/*
 * Server replies to client with all the items listed by the seller(if any)
 */
public class SearchBySellerMessageReply implements Message {
	private ArrayList<Item> items;

	public SearchBySellerMessageReply(ArrayList<Item> searchResult) {
		this.items = searchResult;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}	
}
