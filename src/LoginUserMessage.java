import javax.swing.*;

/*
 * Sends message to the server with all the details to log in a registered user
 */
public class LoginUserMessage implements Message {
	private String username, password;
	
	public LoginUserMessage(JTextField username, JPasswordField password) {
		this.username = username.getText();
		this.password = String.valueOf(password.getPassword());
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
