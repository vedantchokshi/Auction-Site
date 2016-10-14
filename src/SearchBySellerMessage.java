/*
 * Client sends the sellerID to the server to find all items listed by a given seller
 */
public class SearchBySellerMessage implements Message {
	private String sellerID;

	public SearchBySellerMessage(String sellerID) {
		this.sellerID = sellerID;
	}

	public String getSellerID() {
		return sellerID;
	}
}
