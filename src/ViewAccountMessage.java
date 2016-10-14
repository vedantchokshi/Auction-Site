/*
 * Sends the server the User for which the Client wants to see the My Account page for
 */
public class ViewAccountMessage implements Message {
	private User user;
	
	public ViewAccountMessage(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
}
