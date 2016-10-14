/*
 * Server replies to Client with an Item with the corresponding ItemID
 */
public class ViewItemMessageReply implements Message {
	private Item item;

	public ViewItemMessageReply(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}	
}
