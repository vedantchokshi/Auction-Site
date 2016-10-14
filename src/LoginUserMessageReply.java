/*
 * Server replies to client with success/failure
 */
public class LoginUserMessageReply implements Message {
	private User user;
	private boolean status;	
	private String message;

	public LoginUserMessageReply(User user, boolean status, String message) {
		this.user = user;
		this.status = status;
		this.message = message;	
	}
	
	public User getUser() {
		return user;
	}	
	
	public boolean getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}	
}
