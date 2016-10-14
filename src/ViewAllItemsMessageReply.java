import java.util.ArrayList;
import java.util.List;
/*
 * Server replies with an ArrayList of Items which contins all the active items
 */
public class ViewAllItemsMessageReply implements Message {
	private ArrayList<Item> items;

	public ViewAllItemsMessageReply(List<Item> activeItems) {
		this.items = new ArrayList<>(activeItems);
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
}
