import java.util.Date;
/*
 * Client sends message to server to search for all possibly sold items after a given date
 */
public class SearchByDateMessage implements Message {
	private Date date;
	
	public SearchByDateMessage(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
}
