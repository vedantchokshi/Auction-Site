/*
 * Client sends message to server with category to search
 */
public class SearchByCategoryMessage implements Message {	
	private String category;

	public SearchByCategoryMessage(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}
}
