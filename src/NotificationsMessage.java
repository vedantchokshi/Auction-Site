/*
 * Server sends a notification message to the client which contains the User and the User contains all the notifications
 */
public class NotificationsMessage implements Message {
	private User user;
	
	public NotificationsMessage(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
