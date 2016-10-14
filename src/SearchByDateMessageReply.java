import java.util.ArrayList;
/*
 * Server replies to client with a list of Items from the Date requested
 */
public class SearchByDateMessageReply implements Message {
	ArrayList<Item> searchResult;
	
	public SearchByDateMessageReply(ArrayList<Item> searchResult) {
		this.searchResult = searchResult;
	}

	public ArrayList<Item> getSearchResult() {
		return searchResult;
	}
}
