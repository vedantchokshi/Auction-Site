import javax.swing.*;

/*
 * Sends message to the server with all the details to create a user
 */
public class RegisterUserMessage implements Message {
	private String name, surname, username, password;

	public RegisterUserMessage(JTextField name, JTextField surname, JTextField username, JPasswordField password) {
		this.name = name.getText();
		this.surname = surname.getText();
		this.username = username.getText();
		this.password = String.valueOf(password.getPassword());	
	}
	
	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
