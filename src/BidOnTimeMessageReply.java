/*
 * Server sends a message back to the client so client can update details if bid was sucessful or not
 */
public class BidOnTimeMessageReply implements Message {
	private Item item;
	private String message;
	private boolean status;

	public BidOnTimeMessageReply(Item item, String message, boolean status) {
		this.message = message;
		this.status = status;
		this.item = item;
	}
	
	public Item getItem() {
		return item;
	}

	public String getMessage() {
		return message;
	}

	public boolean getStatus() {
		return status;
	}
}
