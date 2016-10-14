import java.io.*;
import java.util.*;

/*
 * Saves data to files and reads data from files
 */
public class DataPersistence {
	private File dir = new File("Data");
	
	//Saves data to files
	public void save() {
		//Creates directory
		if(!dir.exists()){
			dir.mkdir();
		}
		
		//Creates file for users
		try {
			FileOutputStream users = new FileOutputStream(dir + "/users.txt");
			ObjectOutputStream usersList = new ObjectOutputStream(users);
			usersList.writeObject(Server.getUsers());
			usersList.flush();
			usersList.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Creates files for activeItems
		try {
			FileOutputStream activeItems = new FileOutputStream(dir + "/active_items.txt");
			ObjectOutputStream activeItemsList = new ObjectOutputStream(activeItems);
			activeItemsList.writeObject(Server.getActiveItems());
			activeItemsList.flush();
			activeItemsList.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Creates files for inactiveItems
		try {
			FileOutputStream inactiveItems = new FileOutputStream(dir + "/inactive_items.txt");
			ObjectOutputStream inactiveItemsList = new ObjectOutputStream(inactiveItems);
			inactiveItemsList.writeObject(Server.getInactiveItems());
			inactiveItemsList.flush();
			inactiveItemsList.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Creates files for usedIDs
		try {
			FileOutputStream usedItemID = new FileOutputStream(dir + "/used_IDs.txt");
			ObjectOutputStream usedItemIDList = new ObjectOutputStream(usedItemID);
			usedItemIDList.writeObject(Server.getUsedItemID());
			usedItemIDList.flush();
			usedItemIDList.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Read data from files
	@SuppressWarnings("unchecked")
	public void load() {
		//If directory exists
		if(dir.exists()) {
			try {
				//If users.txt exists, then read and restore old data
				File f = new File(dir + "/users.txt");
				if(f.exists()) {
					FileInputStream users = new FileInputStream(f);
					ObjectInputStream userList = new ObjectInputStream(users);
					List<User> list = (List<User>) userList.readObject();
					Server.setUsers(new ArrayList<>(list));
					userList.close();
				}				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			//If active_items.txt exists, then read and restore old data
			try {
				File f = new File(dir + "/active_items.txt");
				if(f.exists()) {
					FileInputStream activeItems = new FileInputStream(f);
					ObjectInputStream activeItemsList = new ObjectInputStream(activeItems);
					List<Item> list = (List<Item>) activeItemsList.readObject();
					Server.setActiveItems(new ArrayList<>(list));
					activeItemsList.close();
				}				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			//If inactive_items.txt exists, then read and restore old data
			try {
				File f = new File(dir + "/inactive_items.txt");
				if(f.exists()) {
					FileInputStream inactiveItems = new FileInputStream(f);
					ObjectInputStream inactiveItemsList = new ObjectInputStream(inactiveItems);
					List<Item> list = (List<Item>) inactiveItemsList.readObject();
					Server.setInactiveItems(new ArrayList<>(list));
					inactiveItemsList.close();
				}				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			//If used_IDs.txt exists, then read and restore old data
			try {
				File f = new File(dir + "/used_IDs.txt");
				if(f.exists()) {
					FileInputStream usedItemID = new FileInputStream(f);
					ObjectInputStream usedItemIDList = new ObjectInputStream(usedItemID);
					ArrayList<Integer> list = (ArrayList<Integer>) usedItemIDList.readObject();
					Server.setUsedItemID(list);
					usedItemIDList.close();
				}
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
