import java.util.ArrayList;

/*
 * Server replies to client with a list of Items from the category requested
 */
public class SearchByCategoryMessageReply implements Message {
	private ArrayList<Item> items;

	public SearchByCategoryMessageReply(ArrayList<Item> searchResult) {
		this.items = searchResult;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
}