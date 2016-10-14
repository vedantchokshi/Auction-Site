/*
 * Sends a message to the server with all the details for a user to bid on a time
 */
public class BidOnItemMessage implements Message {
	private User user;
	private Item item;
	private Double bid;
	
	public BidOnItemMessage(User user, Item item, Double bid) {
		this.user = user;
		this.item = item;
		this.bid = bid;
	}
	
	public User getUser() {
		return user;
	}

	public Item getItem() {
		return item;
	}

	public Double getBid() {
		return bid;
	}
}
