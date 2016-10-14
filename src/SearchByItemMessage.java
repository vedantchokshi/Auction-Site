/*
 * Client sends the itemID to the server to find an Item with the given ID
 */
public class SearchByItemMessage implements Message {
	private String itemID;

	public SearchByItemMessage(String itemID) {
		this.itemID = itemID;
	}

	public String getItemID() {
		return itemID;
	}
}
