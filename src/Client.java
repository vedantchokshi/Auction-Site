import javax.swing.JFrame;
/*
 * Client class which
 */
public class Client {
	private Comms client;
	private DisplayElements GUI;
	private JFrame frame;

	public Client(JFrame frame) {
		client = new Comms();
		client.init();
		this.frame = frame;
		GUI = new DisplayElements(client);		
		receiveMessages();
	}
	
	//Makes a thread which is constantly receiving messages from the server while there is a connection between the server and client
	private void receiveMessages() {
		Thread receive = new Thread(new Runnable() {			
			@Override
			public void run() {
				while(client.isOpen()) {
					//Receives message from the client and handles the message
					Message m = client.receiveMessage();
					if(m instanceof LoginUserMessageReply) {
						GUI.displayMessage(((LoginUserMessageReply) m).getMessage());
						if(((LoginUserMessageReply) m).getStatus()) {
							GUI.setCurrentUser(((LoginUserMessageReply) m).getUser());
							client.sendMessage(new ViewAllItemsMessage());
						}				
					} else if(m instanceof RegisterUserMessageReply) {
						GUI.displayMessage(((RegisterUserMessageReply) m).message());
						if(((RegisterUserMessageReply) m).getStatus()) {
							GUI.loginPage();
						}				
					} else if(m instanceof SearchByCategoryMessageReply) {
						GUI.addItems(GUI.getItemList(), ((SearchByCategoryMessageReply) m).getItems());
					} else if(m instanceof SearchBySellerMessageReply) {
						GUI.addItems(GUI.getItemList(), ((SearchBySellerMessageReply) m).getItems());
					} else if(m instanceof SearchByItemMessageReply) {
						GUI.addItems(GUI.getItemList(), ((SearchByItemMessageReply) m).getItem());
					} else if(m instanceof SearchByDateMessageReply) {
						GUI.addItems(GUI.getItemList(), ((SearchByDateMessageReply) m).getSearchResult());
					} else if(m instanceof ViewAllItemsMessageReply) {
						GUI.itemsPage(((ViewAllItemsMessageReply) m).getItems());
					} else if(m instanceof AddItemMessageReply) {
						GUI.displayMessage(((AddItemMessageReply) m).getMessage());
						GUI.sellPage();
					} else if(m instanceof ViewAccountMessageReply) {
						GUI.setCurrentUser(((ViewAccountMessageReply) m).getUser());
						GUI.myAccountPage(GUI.getCurrentUser(), ((ViewAccountMessageReply) m).getSell());							
					} else if(m instanceof ViewItemMessageReply) {
						Item item = ((ViewItemMessageReply) m).getItem();
						if(item == null) {
							GUI.displayMessage("Item does not exist anymore. Please refresh page");
						} else {
							GUI.viewItemPage(item);
						}
					} else if(m instanceof BidOnTimeMessageReply) {
						GUI.displayMessage(((BidOnTimeMessageReply) m).getMessage());
						if(((BidOnTimeMessageReply) m).getStatus()) {
							GUI.viewItemPage(((BidOnTimeMessageReply) m).getItem());
						} else {
							client.sendMessage(new ViewAllItemsMessage());
						}
					} else if(m instanceof NotificationsMessage){
						for(String s : ((NotificationsMessage) m).getUser().getNotfications()) {
							GUI.displayMessage(s);
						}
					}
				}
				//When connection is lost, the window closes itself
				frame.dispose();
				System.exit(0);
			}
		});
		receive.start();
	}
	
	//Exit message which logs user out when the window is closed
	public void exit() {
		if(GUI.getCurrentUser() != null) {
			client.sendMessage(new SignOutMessage(GUI.getCurrentUser()));
		}		
	}

	public DisplayElements getGUI() {
		return GUI;
	}
}
