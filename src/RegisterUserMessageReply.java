/*
 * Server replies to client with success/failure
 */
public class RegisterUserMessageReply implements Message {
	private boolean status;

	public RegisterUserMessageReply(boolean status) {
		this.status = status;
	}
	
	public String message() {
		if(status) {
			return "You have succesfully signed up";
		} else
			return "An account with the username already exists. Try again.";
	}
	
	public boolean getStatus() {
		return status;
	}
}
