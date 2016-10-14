/*
 * Message sent by client when the cleint is closed or the signout button is clicked so the server knows
 * that the user is no longer online
 */
public class SignOutMessage implements Message {
	private User user;	
	
	public SignOutMessage(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
