import java.util.ArrayList;
/*
 * Server replies to client with a the item if it exists
 */
public class SearchByItemMessageReply implements Message {
	private Item item;

	public SearchByItemMessageReply(Item searchResult) {
		this.item = searchResult;
	}
	
	public ArrayList<Item> getItem() {
		ArrayList<Item> item = new ArrayList<>();
		if(this.item != null) {
			item.add(this.item);
		}		
		return item;
	}	
}
