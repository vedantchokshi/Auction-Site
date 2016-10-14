/*
 * Client sends Message to server containing an ItemID that the Client wishes to see
 */
public class ViewItemMessage implements Message {
	private Integer itemID;
	
	public ViewItemMessage(Integer itemID) {
		this.itemID = itemID;
	}

	public Integer getItemID() {
		return itemID;
	}
}
