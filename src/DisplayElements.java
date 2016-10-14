import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/*
 * Creates entire GUI
 */
public class DisplayElements extends JPanel {
	private GridBagConstraints gbc;
	private User currentUser;
	private Comms client;
	private String[] categories = new String[] {"Antiques", "Art", "Baby", "Books, Comics & Magazines", "Business, Office & Industrial", "Cameras & Photography", "Cars, Motorcyles & Accessories",
			"Clothes, Shoes & Accessories", "Coins", "Collectables", "Computers/Tablets & Networking", "Crafts", "Dolls & Bears", "DVDs, Films & TV", "Events Tickets", "Garden & Patio",
			"Health & Beauty", "Holidays & Travel", "Home, Furniture & DIY", "Jewellery & Watches", "Mobile Phones & Communication", "Music", "Musical Instruments", "Pet Supplies", 
			"Pottery, Porcelain & Glass", "Property", "Sound & Vision", "Sporting Goods", "Stamps", "Toys & Games", "Vehicle Parts & Accessories", "Video Games & Consoles", "Wholesale & Job Lots"};
	private JPanel itemList;
	
	//Passes client in from Client class so GUI can send messages to the server
	public DisplayElements(Comms client) {
		this.client = client;
		loginPage();
	}
	
	//Creates login page GUI
	public void loginPage() {
		this.removeAll();
		
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder("Login"));
		gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        //Creates GUI using swing componenets
        setGridBagConstraints(0, 0);
        add(new JLabel("Username:"), gbc);
        
        setGridBagConstraints(1, 0);
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        JTextField username = new JTextField(10);
        add(username, gbc);
        
        setGridBagConstraints(0, 1);
        add(new JLabel("Password:"), gbc);        
        
        setGridBagConstraints(1, 1);
        JPasswordField password = new JPasswordField(10);
        add(password, gbc);
		
        setGridBagConstraints(1, 2);
        gbc.gridwidth = 1;
        JButton login = new JButton("Login");		
		login.setEnabled(false);
		
		//FieldHandler which activates the login button when the specified JTextFields are filled
		FieldValidationHandler handleFields = new FieldValidationHandler(login, username, password);
		
		//Sends a Login Message to the server with the username and password
        login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendMessage(new LoginUserMessage(username, password));				
			}
		});        
        add(login, gbc);       
        
        //Navigates user to register a new user page if the user wants to make a new account
        setGridBagConstraints(2, 2);
        JButton register = new JButton("New User? Register");
        register.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				registerPage();			
			}
		});
        add(register, gbc); 
        
		this.revalidate();
		this.repaint();
	}
	
	//Creates GUI to register a new user
	public void registerPage() {
		this.removeAll();
		
        setBorder(new TitledBorder("Register"));        
        gbc.insets = new Insets(5, 5, 5, 5);
        
        //Creates GUI using swing componenets
        setGridBagConstraints(0, 0);
        add(new JLabel("First Name:"), gbc);
        
        setGridBagConstraints(1, 0);
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        JTextField name = new JTextField(10);
        add(name, gbc);
        
        setGridBagConstraints(0, 1);
        add(new JLabel("Surname:"), gbc);
        
        setGridBagConstraints(1, 1);
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        JTextField surname = new JTextField(10);
        add(surname, gbc);
        
        setGridBagConstraints(0, 2);
        add(new JLabel("Username:"), gbc);
        setGridBagConstraints(1, 2);
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        JTextField username = new JTextField(10);
        add(username, gbc);
        
        setGridBagConstraints(0, 3);
        add(new JLabel("Password:"), gbc);
        
        setGridBagConstraints(1, 3);
        JPasswordField password = new JPasswordField(10);
        add(password, gbc);

        //Cancel button sends user back to login page
        setGridBagConstraints(1, 4);
        gbc.gridwidth = 1;
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				loginPage();
			}
		});
        add(cancel, gbc);
        
        setGridBagConstraints(2, 4);
        JButton register = new JButton("Register");
        register.setEnabled(false);
        
        //FieldHandler which activates the register button when the specified JTextFields are filled
        FieldValidationHandler handleFields = new FieldValidationHandler(register, name, surname, username, password);
        
        register.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendMessage(new RegisterUserMessage(name, surname, username, password));	
			}
		});
        add(register, gbc); 
        
		this.revalidate();
		this.repaint();
	}
	
	//Creates GUI for Items Page
	public void itemsPage(ArrayList<Item> items) {
		this.removeAll();

		gbc.insets = new Insets(0, 0, 0, 0);		
		setBorder(new TitledBorder("Items"));
		
		//Creates GUI using swing components
        gbc.gridheight = 2;
		navigationBar(0.13);
		
		gbc.gridheight = 1;        
        setGridBagConstraints(1, 1);
        gbc.weighty = 0.995;
        itemList = new JPanel();
        addItems(itemList, items);        
		
        JScrollPane scrollItems = new JScrollPane(itemList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollItems, gbc);
		
        setGridBagConstraints(1, 0);
        gbc.weightx= 0.85;
        gbc.weighty = 0.005;
        
        //Different searches made in a JTabbedPane
        JTabbedPane filterSection = new JTabbedPane();
        
        //Search by category
        JPanel searchByCategory = new JPanel();
        searchByCategory.setLayout(new GridBagLayout());
        GridBagConstraints localGBC = new GridBagConstraints();
        localGBC.gridx = localGBC.gridy = 0;
        localGBC.gridwidth = 3;
        JComboBox<String> categories = new JComboBox<>(this.categories);
        categories.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					//Sends message to view items by category
					client.sendMessage(new SearchByCategoryMessage((String) e.getItem()));
				}				
			}
		});
        searchByCategory.add(categories, localGBC);
        
        //Reset
        localGBC.gridx = 3;
        localGBC.gridwidth = 1;
        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Resets by sending message to view all active items
				client.sendMessage(new ViewAllItemsMessage());					
			}
		});
        searchByCategory.add(reset, localGBC);        
        
        //Search by sellerID
        JPanel searchBySeller = new JPanel();
        localGBC = new GridBagConstraints();        
        localGBC.gridx = 0;
        localGBC.gridy = 1;
        localGBC.gridwidth = 3;
        JTextField sellerID = new JTextField(15);
        searchBySeller.add(sellerID, localGBC);
        localGBC.gridx = 3;
        localGBC.gridwidth = 1;
        JButton searchSeller = new JButton("Search");
        searchSeller.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Sends message to view items by sellerID
				client.sendMessage(new SearchBySellerMessage(sellerID.getText()));
			}
		});
        searchBySeller.add(searchSeller, localGBC); 
        
        //Reset
        localGBC.gridx = 4;
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Resets by sending message to view all active items
				client.sendMessage(new ViewAllItemsMessage());	
			}
		});
        searchBySeller.add(reset, localGBC); 
        
        //Search by ItemID
        JPanel searchByItemID = new JPanel();
        localGBC = new GridBagConstraints();        
        localGBC.gridx = 0;
        localGBC.gridy = 1;
        localGBC.gridwidth = 3;
        JTextField itemID = new JTextField(15);
        searchByItemID.add(itemID, localGBC);
        
        localGBC.gridx = 3;
        localGBC.gridwidth = 1;
        JButton searchItem = new JButton("Search");
        searchItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Sends message to view item by ItemID
				client.sendMessage(new SearchByItemMessage(itemID.getText()));
			}
		});
        searchByItemID.add(searchItem, localGBC); 
        
        //Reset
        localGBC.gridx = 4;
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Resets by sending message to view all active items
				client.sendMessage(new ViewAllItemsMessage());	
			}
		});
        searchByItemID.add(reset, localGBC); 
        
        //Search by Date
        JPanel searchByDate = new JPanel();
        localGBC = new GridBagConstraints();        
        localGBC.gridx = 0;
        localGBC.gridy = 1;

        Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
        JComboBox<Integer> day = new JComboBox<>(); 
        populateComboBox(day, 1, 32);
        day.setSelectedItem(cal.get(Calendar.DATE));
		searchByDate.add(day, localGBC);
		
		JComboBox<String> month = new JComboBox<>(new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}); 
		month.setSelectedIndex(cal.get(Calendar.MONTH));
		searchByDate.add(month, localGBC);
		
		JComboBox<Integer> year = new JComboBox<>(); 
		populateComboBox(year, cal.get(Calendar.YEAR), cal.get(Calendar.YEAR)+20);
		searchByDate.add(year, localGBC);	
		
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MMM-yyyy");				
				dateFormat.setLenient(false);
				String dateAsString = new String("00:00:00 " + day.getSelectedItem() + "-" + month.getSelectedItem() + "-" + year.getSelectedItem());
				try {
					//Checks if a valid date is entered by the user
					Date searchDate = dateFormat.parse(dateAsString);
					if(cal.getTime().before(searchDate)) {
						throw new Exception("Cannot show auctions in the future. Please adjust search");
					}
					//Sends message to view items by Date
					client.sendMessage(new SearchByDateMessage(searchDate));
				} catch(ParseException invalidDateException) {
					displayMessage("Invalid date entered");
				} catch(Exception invalidDateExceptions) {
					displayMessage(invalidDateExceptions.getMessage());
				}
			}
		});
        searchByDate.add(searchButton, localGBC);
        
        //Reset
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Resets by sending message to view all active items
				client.sendMessage(new ViewAllItemsMessage());
			}
		});
        searchByDate.add(reset, localGBC);
        
        filterSection.add("Search by Seller ID", searchBySeller);
        filterSection.add("Search by Item ID", searchByItemID);
        filterSection.add("Search by Category", searchByCategory);
        filterSection.add("Search by Date", searchByDate);
        add(filterSection, this.gbc);
     
		this.revalidate();  
		this.repaint();
	}
	
	//Creates GUI for Sell Page 
	public void sellPage() {
		this.removeAll();
		
		setBorder(new TitledBorder("Sell"));
		navigationBar(0.11);
		
		//Creates GUI from swing components
		setGridBagConstraints(1, 0);
        gbc.weightx= 0.89;
        gbc.weighty = 1;
        JPanel sell = new JPanel();
        sell.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.gridx = gbc.gridy =  0;
        sell.add(new JLabel("Title: "), gbc);
        
        gbc.gridx++;
        JTextField itemName = new JTextField(20);
		sell.add(itemName, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        sell.add(new JLabel("Reserve Price: £"), gbc);
        
        gbc.gridx++;
        JTextField reservePrice = new JTextField(5);
		sell.add(reservePrice, gbc);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		//Creates combo boxes to pick end time
		gbc.gridy++;
        gbc.gridx = 0;
        sell.add(new JLabel("End Time: "), gbc);
        
        gbc.gridx++;
        JPanel time = new JPanel();
        time.setLayout(new GridLayout(1, 0, 5, 0));
		
        time.add(new JLabel());
        
        JComboBox<Integer> hour = new JComboBox<>(); 
        populateComboBox(hour, 0, 24);
        hour.setSelectedItem(cal.get(Calendar.HOUR_OF_DAY));
		time.add(hour);		
		
		JComboBox<Integer> mins = new JComboBox<>(); 
        populateComboBox(mins, 0, 60);
        mins.setSelectedItem(cal.get(Calendar.MINUTE));
		time.add(mins);
		
		sell.add(time, gbc);
		
		//Creates combo boxes to pick end date
		gbc.gridy++;
        gbc.gridx = 0;
        sell.add(new JLabel("End Date: "), gbc);
        
        gbc.gridx++;
        JPanel date = new JPanel();
        date.setLayout(new GridLayout(1, 0, 5, 0));
		
        JComboBox<Integer> day = new JComboBox<>(); 
        populateComboBox(day, 1, 32);
        day.setSelectedItem(cal.get(Calendar.DATE)+1);
		date.add(day);
		
		JComboBox<String> month = new JComboBox<>(new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}); 
		month.setSelectedIndex(cal.get(Calendar.MONTH));
		date.add(month);
		
		JComboBox<Integer> year = new JComboBox<>(); 
		populateComboBox(year, cal.get(Calendar.YEAR), cal.get(Calendar.YEAR)+20);
		date.add(year);		
		sell.add(date, gbc);
		
        gbc.gridheight = gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        sell.add(new JLabel("Category: "), gbc);
        
        gbc.gridx++;;
        JComboBox<String> categories = new JComboBox<>(this.categories);
        
        sell.add(categories, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        sell.add(new JLabel("Description: "), gbc);
        
        gbc.gridx++;
        JTextArea description = new JTextArea(5, 20);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        JScrollPane scrollableDescription = new JScrollPane(description);
        sell.add(scrollableDescription, gbc);
        
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        JButton createListing = new JButton("Create Listing");
        createListing.setEnabled(false);
        
        //FieldHandler which activates the createListing button when the specified JTextFields are filled
        FieldValidationHandler handleFields = new FieldValidationHandler(createListing, itemName, reservePrice);
        createListing.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				cal.setTime(new Date());
				Date startDate = cal.getTime();
				
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MMM-yyyy");				
				dateFormat.setLenient(false);
				String dateAsString = new String(hour.getSelectedItem() + ":" + mins.getSelectedItem() + ":" + cal.get(Calendar.SECOND) + " " + day.getSelectedItem() + "-" + month.getSelectedItem() + "-" + year.getSelectedItem());
				try {
					//Checks for valid reserve price input
					Double val = Double.parseDouble(reservePrice.getText());
					if(!(val > 0)) {
						throw new Exception("Reserve price has to be greater than 0");
					}
					//Checks for valid date input
					Date endDate = dateFormat.parse(dateAsString);
					if(endDate.before(startDate)) {
						throw new Exception("Auction cannot end before beginning. Please enter a valid end date and time");
					}
					//Sends message to server to create a new item listing
					client.sendMessage(new AddItemMessage(itemName, reservePrice, startDate, endDate, categories, description, currentUser.getUsername()));
				} catch (NumberFormatException e1) {
					displayMessage("Please enter valid input for reserve price");
				} catch (Exception e2) {
					displayMessage(e2.getMessage());
				}
			}
		});
        sell.add(createListing, gbc);       
        add(sell, this.gbc);
        
        this.revalidate();  
		this.repaint();
	}
	
	//Creates GUI to view a specific item
	public void viewItemPage(Item item) {
		this.removeAll();
		
		setBorder(new TitledBorder(item.getTitle()));
		navigationBar(0.11);
		
		//Creates GUI from swing components by getting details of the item such as title, start time, end time, etc.
		setGridBagConstraints(1, 0);
        gbc.weightx= 0.89;
        gbc.weighty = 1;
        JPanel view = new JPanel();
        view.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		
		JLabel title = new JLabel(item.getTitle());
		title.setFont(new Font(title.getName(), Font.BOLD, 50));
		view.add(title, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.9;
		JPanel itemDetails = new JPanel();
		itemDetails.setLayout(new GridBagLayout());
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.insets = new Insets(10, 5, 10, 5);	
		gbc2.fill = GridBagConstraints.BOTH;
		
		gbc2.gridx = gbc2.gridy = 0;
		itemDetails.add(new JLabel("Current Price: "), gbc2);
		
		gbc2.gridx++;
		gbc2.gridwidth = 2;
		itemDetails.add(new JLabel("£ " + item.currentPrice()), gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy++;
		gbc2.gridwidth = 1;
		itemDetails.add(new JLabel("Bid: "), gbc2);
		
		gbc2.gridx++;
		gbc2.gridwidth = 1;
		JTextField bid = new JTextField(5);
		itemDetails.add(bid, gbc2);
		
		gbc2.gridx++;
		gbc2.gridwidth = 1;
		JButton bidButton = new JButton("Bid");
		bidButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//Checks for valid bid input
					double bidValue = Double.parseDouble(bid.getText());
					//Sends message to server with the User thats bid on the item and the value the user has attempted to bid
					client.sendMessage(new BidOnItemMessage(currentUser, item, bidValue));
				} catch(NumberFormatException e1) {
					displayMessage("Please enter a valid bid value");
				}
				
			}
		});
		itemDetails.add(bidButton, gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy++;
		gbc2.gridwidth = 1;
		itemDetails.add(new JLabel("Start Time: "), gbc2);
		
		gbc2.gridx++;
		gbc2.gridwidth = 2;
		itemDetails.add(new JLabel(item.getStartTime().toString()), gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy++;
		gbc2.gridwidth = 1;
		itemDetails.add(new JLabel("End Time: "), gbc2);
		
		gbc2.gridx++;
		gbc2.gridwidth = 2;
		itemDetails.add(new JLabel(item.getEndTime().toString()), gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy++;
		gbc2.gridwidth = 1;
		itemDetails.add(new JLabel("Category: "), gbc2);
		
		gbc2.gridx++;
		gbc2.gridwidth = 2;
		itemDetails.add(new JLabel(item.getCategory()), gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy++;
		gbc2.gridwidth = 1;
		itemDetails.add(new JLabel("Seller ID: "), gbc2);
		
		gbc2.gridx++;
		gbc2.gridwidth = 2;
		itemDetails.add(new JLabel(item.getVendorUsername()), gbc2);
		
		gbc2.gridx = 0;
		gbc2.gridy++;
		gbc2.gridwidth = 1;
		itemDetails.add(new JLabel("Item ID: "), gbc2);
		
		gbc2.gridx++;
		gbc2.gridwidth = 2;
		itemDetails.add(new JLabel("" + item.getItemID()), gbc2);		
		
		view.add(itemDetails, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.9;
		JTextArea description = new JTextArea(20, 8);
		description.setText("DESCRIPTION \n \n");
		description.append(item.getDescription());
		description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        JScrollPane scrollableDescription = new JScrollPane(description);
		view.add(scrollableDescription, gbc);
		
		add(view, this.gbc);
		
		this.revalidate();  
		this.repaint();
	}
	
	//Creates GUI to view Accounts Page for a specific user
	public void myAccountPage(User user, ArrayList<Item> userSell) {
		this.removeAll();
		
		setBorder(new TitledBorder("My Account"));
		navigationBar(0.16);
		
		setGridBagConstraints(1, 0);
        gbc.weightx= 0.84;
        gbc.weighty = 1;
        
        JPanel accountPage = new JPanel();		
		accountPage.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0.05;
		accountPage.add(new JLabel("BIDS"), gbc);
		
		//Creates table for bids the user has placed/items the user has won
		gbc.gridx = 0; 
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 0.45;
		JPanel bids = new JPanel();
		bids.setLayout(new BorderLayout());
		String[] header = new String[] {"Title", "Price(£)", "Item ID", "End Time", "Your Bid", "Status"};
	    JTable table = new JTable();
		DefaultTableModel dtm = new DefaultTableModel(0, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		dtm.setColumnIdentifiers(header);
		table.setModel(dtm);
		
		for(Item item : currentUser.getBids().keySet()) {
			dtm.addRow(new Object[] {item.getTitle(), item.currentPrice(), item.getItemID(), item.getEndTime().toString(), currentUser.getBids().get(item), item.getStatus(currentUser)});
		}
		
		bids.add(table.getTableHeader(), BorderLayout.NORTH);
		bids.add(table, BorderLayout.CENTER);
		JScrollPane scrollBids = new JScrollPane(bids, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
		accountPage.add(scrollBids, gbc);
		
		//Creates table for the items the user is selling/sold
		gbc.gridx = 0; 
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 0.05;
		accountPage.add(new JLabel("SELL"), gbc);
		
		gbc.gridx = 0; 
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.weighty = 0.45;
		JPanel sell = new JPanel();
		sell.setLayout(new BorderLayout());
		String[] header2 = new String[] {"Title", "Price(£)", "Item ID", "End Time", "Status"};
	    JTable table2 = new JTable();
		DefaultTableModel dtm2 = new DefaultTableModel(0, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		dtm2.setColumnIdentifiers(header2);
		table2.setModel(dtm2);
		
		for(Item i : userSell) {
			dtm2.addRow(new Object[] {i.getTitle(), i.currentPrice(), i.getItemID(), i.getEndTime().toString(), i.getStatus(currentUser)});
		}
		
		sell.add(table2.getTableHeader(), BorderLayout.NORTH);
		sell.add(table2, BorderLayout.CENTER);
		JScrollPane scrollSell = new JScrollPane(sell, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
		accountPage.add(scrollSell, gbc);
		
		add(accountPage, this.gbc);
		
		this.revalidate();  
		this.repaint();
	}
	
	//Creates GUI for the navigation bar on the side
	public void navigationBar(double weightx) {
		setGridBagConstraints(0, 0);
        gbc.weightx= weightx;
        gbc.weighty = 1;
        
        JPanel navigation = new JPanel();
        navigation.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.gridx = gbc.gridy = 0;
        JButton items = new JButton("Items");
        navigation.add(items, gbc);
        items.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Sends message to view all items
				client.sendMessage(new ViewAllItemsMessage());
			}
		});
        
        gbc.gridy++;
        JButton sell = new JButton("Sell an Item");
        navigation.add(sell, gbc);
        sell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Opens up the sell page
				sellPage();
			}
		});  
        
        gbc.gridy++;
        JButton account = new JButton("My Account");
        account.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Sends message to Server to see My Account details for current user
				client.sendMessage(new ViewAccountMessage(currentUser));			
			}
		});
        navigation.add(account, gbc);
        
        gbc.gridy++;
        JButton signOut = new JButton("Sign Out");
        signOut.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Sends sign out message and signs out current user
				client.sendMessage(new SignOutMessage(currentUser));
				loginPage();
			}
		});
        navigation.add(signOut, gbc);
        
        gbc.gridy++;
        //Shows signed in user
        JLabel user = new JLabel("Signed in as: " + currentUser.getUsername());
        navigation.add(user, gbc);
        
        add(navigation, this.gbc);
	}
	
	//Adds items to a given JPanel
	public void addItems(JPanel panelToAddTo, ArrayList<Item> items) {
		panelToAddTo.removeAll();
		
	    panelToAddTo.setLayout(new BorderLayout());
	    
	    //Creates a new table
	    String[] header = new String[] {"Title", "Price(£)", "Category", "SellerID", "End Time", "Item ID"};
	    JTable table = new JTable();
		DefaultTableModel dtm = new DefaultTableModel(0, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		dtm.setColumnIdentifiers(header);
		table.setModel(dtm);
		
		//Adds items to table
		for(Item i : items) {
			dtm.addRow(new Object[] {i.getTitle(), i.currentPrice(), i.getCategory(), i.getVendorUsername(), i.getEndTime(), i.getItemID()});
		}
		
		//Adds selection listener to the table
		ListSelectionModel model = table.getSelectionModel();
		model.addListSelectionListener(new ListSelectionListener() {			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!model.isSelectionEmpty() && !e.getValueIsAdjusting() ) {
					//Sends a message to view item for the row clicked
					int selectedRow = model.getMinSelectionIndex();
					client.sendMessage(new ViewItemMessage((Integer) table.getValueAt(selectedRow, table.getColumnCount() - 1)));
				}
			}
		});
		
		//Adjusts width of columns
		TableColumnModel columnModel = table.getColumnModel();
	    for (int column = 0; column < table.getColumnCount(); column++) {
	        int width = 50;
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, column);
	            Component comp = table.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width +1 , width);
	        }
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
		
		panelToAddTo.add(table.getTableHeader() , BorderLayout.NORTH);
		panelToAddTo.add(table, BorderLayout.CENTER);
		
		panelToAddTo.revalidate();  
		panelToAddTo.repaint();
	}

	//Sets GridBagConstraints for main gbc
	private void setGridBagConstraints(int gridx, int gridy) {
		gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.BOTH;
	}
	
	//Displays message
	public void displayMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
	//Adds numbers to combobox
	private void populateComboBox(JComboBox<Integer> boxToPopulate, int start, int end) {
		boxToPopulate.removeAllItems();
		for(int i = start; i < end; i++) {
			boxToPopulate.addItem(i);
		}
	}
	
	//Gets current user
	public User getCurrentUser() {
		return currentUser;
	}

	//Sets current user
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	//Gets itemList
	public JPanel getItemList() {
		return itemList;
	}

	//FieldValidationHandler which only activates the given JButton when all the given JTextFields are filled
	private class FieldValidationHandler implements DocumentListener {
		private List<JTextField> monitorFields;
		private JButton buttonToHandle;
		
		public FieldValidationHandler(JButton buttonToHandle, JTextField... fields) {
			this.buttonToHandle = buttonToHandle;
			monitorFields = Arrays.asList(fields);
			for (JTextField field : monitorFields) {
	            field.getDocument().addDocumentListener(this);
	        }
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			change();			
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			change();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			change();
		}
		
		private void change(){
			boolean enabled = true;
			//Loops through each JTextField to see if it is empty
			for (JTextField field : monitorFields) {
				if (field.getText().trim().isEmpty()) {
					enabled = false;
				break;
				}
			}
			//If all filled then activate button
			buttonToHandle.setEnabled(enabled);
		}			
	}
}