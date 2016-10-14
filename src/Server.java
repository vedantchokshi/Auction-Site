import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.TitledBorder;
/*
 * Creates server GUI and runs the server
 */
public class Server implements Runnable {
	private static List<User> users = Collections.synchronizedList(new ArrayList<User>());
	private static List<Item> activeItems = Collections.synchronizedList(new ArrayList<Item>());
	private static List<Item> inactiveItems = Collections.synchronizedList(new ArrayList<Item>());
	private static ArrayList<Integer> usedItemID = new ArrayList<>();
	private static JTextArea log = new JTextArea();
	private static JFrame GUI;
	private Comms c;
	private User user;	
	
	public Server(Comms c) {
		this.c = c;
	}

	//Creates Server GUI
	public static void main(String[] args) throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//Sets theme
			    try {
			        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			    } catch(UnsupportedLookAndFeelException e) {
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			        e.printStackTrace();
			    }
			    //Creates server JFrame
				GUI = new JFrame("Server");
				GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				//Loads data if available
				DataPersistence data = new DataPersistence();
				data.load();
				
				JPanel logPanel = new JPanel(new BorderLayout());
				logPanel.setBorder(new TitledBorder("LOG"));
			
				log.setEditable(false);
				log.setLineWrap(true);
			    log.setWrapStyleWord(true);
			    
			    JScrollPane scrollLog = new JScrollPane(log);
			    logPanel.add(scrollLog, BorderLayout.CENTER);
			    JButton produceLog = new JButton("Produce Log");
			    produceLog.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							//Creates the log.txt file when the button is clicked
							FileWriter writer = new FileWriter("LOG.txt");
							for(User u : users) {
								writer.write("USER: " + u.getName() + " " + u.getSurname() + "\n");
								writer.write("\tITEMS BOUGHT: \n");
								for(String s : u.getItemsWon()) {
									writer.write("\t" + s + "\n");
								}
							}
							JOptionPane.showMessageDialog(GUI, "LOG.txt created");
							writer.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}					
					}
				});
			    logPanel.add(produceLog, BorderLayout.SOUTH);
			    
			    GUI.add(logPanel);
				GUI.setSize(600, 500);
				GUI.setVisible(true);
				GUI.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						for(User u : users) {
							//Sets all users to inactive when the server is closed
							if(u.isActive()) {
								u.setActive(false);
							}
						}
						//Saves the data
						data.save();
					}
				});
			}
		});
		//Starts the server
		Server.init();
	}
	
	/*
	 * Establishes a connection with a client
	 * Each client has its own server and comms but they all access static variables and hence the information they access is the same
	 */
	public static void init() {
		while(true) {
			Comms sc = Comms.connect();
			addToLog("[SERVER]: " + sc.name() + " connected");
			sc.init();
			if (sc != null) {
				Thread x = new Thread(new Server(sc));
				x.start();
			}
		}
	}
	
	/*
	 * Ends items which have passed their end date
	 * Runs in the main thread
	 */
	private void timeTicker() {
		synchronized(activeItems) {
			if(activeItems.size() > 0) {
				ArrayList<Item> toRemove = new ArrayList<>();						
				for(Item i : activeItems) {
					if(!i.isActive()) {
						inactiveItems.add(i);
						createNotification(i);
						toRemove.add(i);
						addToLog("[SERVER]: " + i.getTitle() + " ended");								
					} else {
						break;
					}
				}
				if(toRemove.size() > 0) {
					activeItems.removeAll(toRemove);							
				}
			}
		}		
	}
	
	//Receives a log in message from the client and logs the user in if the user isn't already logged in
	private void login(LoginUserMessage m) {
		user = null;
		boolean status = false;
		String message = "Invalid username or password";
		if(!users.isEmpty()){
			for(User u : users) {
				if(u.getUsername().equals(m.getUsername())) {
					if(u.isActive()) {
						message = "User is already logged in";
					}					
					if(!u.isActive() && u.getPassword().equals(m.getPassword())) {
						user = u;
						status = true;
						message = "You have succesfully logged in";
						user.setActive(true);
						addToLog("[SERVER]: " + m.getUsername() + " logged in");
						break;
					}
				}
			}
		}
		c.sendMessage(new LoginUserMessageReply(user, status, message));
	}
	
	//Receives a register message from the client and makes a new user if the user doesnt already exist
	private void register(RegisterUserMessage m) {
		boolean status = false;
		checkUserLoop: {
			for (User u : users) {
				if (u.getUsername().equals(m.getUsername())) {
					break checkUserLoop;
				}
			}
			status = true;
			users.add(new User(m.getName(), m.getSurname(), m.getUsername(), m.getPassword()));
			addToLog("[SERVER]: " + m.getUsername() + " registered");
		}
		c.sendMessage(new RegisterUserMessageReply(status));
	}
	
	//Receives a message from the client and creates an item
	private void addItem(AddItemMessage m) {
		Random rand = new Random();
		Integer itemID = rand.nextInt(100001);
		while(usedItemID.contains(itemID)) {
			itemID = rand.nextInt(100001);
		}
		usedItemID.add(itemID);
		activeItems.add(new Item(m.getTitle(), m.getReservePrice(), m.getDescription(), m.getCategory(), m.getVendorUsername(), m.getStartTime(), m.getEndTime(), itemID));
		Collections.sort(activeItems);
		addToLog("[SERVER]: Item - " + m.getTitle() + " added by " + m.getVendorUsername());
		c.sendMessage(new AddItemMessageReply());
	}
	
	//Recives a message from the client to search by category
	private void searchByCategory(SearchByCategoryMessage m) {
		ArrayList<Item> items = new ArrayList<>();
		//Loops through all the items and returns an ArrayList of items with the category that is searched for
		for(Item i : activeItems) {
			if(i.getCategory().equals(m.getCategory())) {
				items.add(i);
			}
		}
		addToLog("[SERVER]: Client requested search by category - Returns: " + items.size() + " items");
		c.sendMessage(new SearchByCategoryMessageReply(items));
	}
	
	//Recives a message from the client to search by SellerID
	private void searchBySeller(SearchBySellerMessage m) {
		ArrayList<Item> items = new ArrayList<>();
		//Loops through all the items and returns an ArrayList of items with the seller that is searched for
		for(Item i : activeItems) {
			if(i.getVendorUsername().equals(m.getSellerID())) {
				items.add(i);
			}
		}
		addToLog("[SERVER]: Client requested search by SellerID - Returns: " + items.size() + " items");
		c.sendMessage(new SearchBySellerMessageReply(items));
	}
	
	//Recives a message from the client to search by ItemID
	private void searchByItem(SearchByItemMessage m) {
		Item item = null;
		//Loops through all the items and returns an the Item which is searched for
		for(Item i : activeItems) {
			if(i.getItemID().toString().equals(m.getItemID())) {
				item = i;
				break;
			}
		}
		addToLog("[SERVER]: Client requested search by ItemID");
		c.sendMessage(new SearchByItemMessageReply(item));
	}
	
	//Receives a message from the client to search by date
	private void searchByDate(SearchByDateMessage m) {
		ArrayList<Item> items = new ArrayList<>();
		//Loops through all the items and returns an ArrayList of all possibly sold items since the specified date
		for(Item i : activeItems) {
			if(i.getStartTime().after(m.getDate())) {
				items.add(i);
			}
		}
		for(Item i : inactiveItems) {
			if(i.getStartTime().after(m.getDate())) {
				items.add(i);
			}
		}
		addToLog("[SERVER]: Client requested search by Date");
		c.sendMessage(new SearchByDateMessageReply(items));
	}
	
	//Receives a message from the client to view an Item
	private Item viewItem(ViewItemMessage m) {
		Item item = null;
		//Loops through all the items and returns an the Item which is searched for
		for(Item i : activeItems) {
			if(i.getItemID().equals(m.getItemID())) {
				item = i;
				break;
			}
		}
		if(item == null) {
			for(Item i : inactiveItems) {
				if(i.getItemID().equals(m.getItemID())) {
					item = i;
					break;
				}
			}
		}
		addToLog("[SERVER]: Client requested to view " + item.getTitle());
		return item;		
	}
	
	//Receives a message to bid on an Item
	private void bidItem(BidOnItemMessage m) {
		Item item;
		try {
			item = activeItems.get(activeItems.indexOf(m.getItem()));
		} catch(Exception e) {
			item = inactiveItems.get(inactiveItems.indexOf(m.getItem()));
		}		
		String s = null;
		//Processes bid and returns if the bid was successful or not
		if(activeItems.contains(item)) {
			if(m.getUser().getUsername().equals(item.getVendorUsername())) {
				s = "You cannot bid on your item. Cannot bid";
			} else {
				if(m.getBid() <= item.currentPrice()) {
					s = "The bid should be higher than the current price";
				} else {
					User user = users.get(users.indexOf(m.getUser()));
					item.addBid(user, m.getBid());
					user.addBids(item, m.getBid());
					s = "Bid has succesfully been placed";
					addToLog("[SERVER]: Bid of " + m.getBid() + " placed on " + item.getTitle());
				}
			}
			c.sendMessage(new BidOnTimeMessageReply(item, s, true));
		} else {
			s = "Item listing has ended. Cannot bid";
			c.sendMessage(new BidOnTimeMessageReply(item, s, false));
		}		
	}
	
	//Receives a mesasage to view account details
	private void myAccount(ViewAccountMessage m) {
		//Returns user's bid/won items and the items that the user is selling/has sold
		User user = users.get(users.indexOf(((ViewAccountMessage) m).getUser()));
		ArrayList<Item> sell = new ArrayList<>();
		for(Item i : activeItems) {
			if(i.getVendorUsername().equals(user.getUsername())) {
				sell.add(i);
			}
		}
		for(Item i : inactiveItems) {
			if(i.getVendorUsername().equals(user.getUsername())) {
				sell.add(i);
			}	
		}
		c.sendMessage(new ViewAccountMessageReply(user, sell));
	}
	
	//Receives message to sign out the current user
	private void signOut(SignOutMessage m) {
		user = users.get(users.indexOf(((SignOutMessage) m).getUser()));
		user.setActive(false);
		addToLog("[SERVER]: " + user.getUsername() + " has signed out");
		user = null;
	}
	
	//Creates notification for an item which has ended and adds the notification to the buyer and seller
	private void createNotification(Item item) {
		User seller = findUser(item.getVendorUsername());
		User buyer = null;
		if(item.currentPrice() == item.getReservePrice()) {
			seller.addNotification("Your item listing(" + item.getTitle() + ") has ended! The item has not been sold");
		} else if(item.currentPrice() > item.getReservePrice()) {
			buyer = users.get(users.indexOf(item.getHighestBidder()));
			buyer.addNotification("You have won the item " + item.getTitle());
			buyer.addWonItem(item.getTitle());
			seller.addNotification("Your item listing(" + item.getTitle() + ") has ended! The item has been sold to " + buyer.getUsername());
		}
	}
	
	//Finds user from username
	private User findUser(String username) {
		User user = null;
		for(User u : users) {
			if(u.getUsername().equals(username)) {
				user = u;
				break;
			}
		}
		return user;
	}
	
	//Sends notification to a user when online
	private void sendNotifications() {
		if(user != null && user.getNotfications().size() > 0) {	
			c.sendMessage(new NotificationsMessage(user));
			user.clearNotifications();
		}
	}
	
	//Main thread which receives messages from the client and handles them
	@Override
	public void run() {
		try {
			//Runs while there is a connection between server and client
			while(c.isOpen()) {
				Message m = c.receiveMessage();
				if(m != null) {
					if(m instanceof RegisterUserMessage) {
						register((RegisterUserMessage) m);
					} else if(m instanceof LoginUserMessage) {
						login((LoginUserMessage) m);
					} else if(m instanceof AddItemMessage) {
						addItem((AddItemMessage) m);
					} else if(m instanceof ViewAllItemsMessage) {
						c.sendMessage(new ViewAllItemsMessageReply(activeItems));
					} else if(m instanceof SearchByCategoryMessage) {
						searchByCategory((SearchByCategoryMessage) m);
					} else if(m instanceof SearchBySellerMessage) {
						searchBySeller((SearchBySellerMessage) m);
					} else if(m instanceof SearchByItemMessage) {
						searchByItem((SearchByItemMessage) m);
					} else if(m instanceof SearchByDateMessage) {
						searchByDate((SearchByDateMessage) m);
					} else if(m instanceof ViewItemMessage) {
						c.sendMessage(new ViewItemMessageReply(viewItem((ViewItemMessage) m)));
					} else if(m instanceof BidOnItemMessage) {
						bidItem((BidOnItemMessage) m);
					} else if(m instanceof ViewAccountMessage) {
						myAccount((ViewAccountMessage) m);
					} else if(m instanceof SignOutMessage) {
						signOut((SignOutMessage) m);
					}
				}
				//Runs the time ticker in the while loop so items are ended and notifications are sent
				timeTicker();
				sendNotifications();
			}
			addToLog("[SERVER]: " + c.name() + " disconnected");
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	//Adds messages to the log
	public static void addToLog(String message) {
		log.append(message + "\n");
	}

	//Get the list of users
	public static List<User> getUsers() {
		return users;
	}

	//Set the list of users
	public static void setUsers(ArrayList<User> users) {
		Server.users = Collections.synchronizedList(users);
	}

	//Get the list of inactive items
	public static List<Item> getInactiveItems() {
		return inactiveItems;
	}

	//Set the list of inactive items
	public static void setInactiveItems(ArrayList<Item> inactiveItems) {
		Server.inactiveItems = Collections.synchronizedList(inactiveItems);
	}
	
	//Get the list of used ItemIDs
	public static ArrayList<Integer> getUsedItemID() {
		return usedItemID;
	}

	//Set the list of used ItemIDs
	public static void setUsedItemID(ArrayList<Integer> usedItemID) {
		Server.usedItemID = usedItemID;
	}

	//Get the list of active items
	public static List<Item> getActiveItems() {
		return activeItems;
	}
	
	//Set the list of active items
	public static void setActiveItems(ArrayList<Item> activeItems) {
		Server.activeItems = Collections.synchronizedList(activeItems);
	}
	
	
}