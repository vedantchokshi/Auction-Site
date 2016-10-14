import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * Sends a message to the server with all the details to create a new item
 */
public class AddItemMessage implements Message {
	private String title, category,description, vendorUsername;
	private double reservePrice;
	private Date startTime, endTime;
	
	public AddItemMessage(JTextField title, JTextField reservePrice, Date startTime, Date endTime, JComboBox<String> categories, JTextArea description, String vendorUsername) {
		this.title = title.getText();
		this.reservePrice = Double.parseDouble(reservePrice.getText());
		this.startTime = startTime;
		this.endTime = endTime;
		this.category = categories.getSelectedItem().toString();
		this.description = description.getText();
		this.vendorUsername = vendorUsername;
	}

	public String getTitle() {
		return title;
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public String getVendorUsername() {
		return vendorUsername;
	}

	public double getReservePrice() {
		return reservePrice;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}
	
}
