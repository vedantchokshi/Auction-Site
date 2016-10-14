
/*
 * Server sends a message back to the client saying the item has been created
 */
public class AddItemMessageReply implements Message {
	private String message = "Item has been added";
	
	public String getMessage(){
		return message;
	}
}
