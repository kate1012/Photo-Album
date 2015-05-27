package cs213.photoAlbum.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.DefaultListModel;

import cs213.photoAlbum.model.User;
import cs213.photoAlbum.model.UserInterface;

/**Provides methods to create, delete, manipulate, and analyze User objects.
 * @author Kate Sussman
 *
 */
public final class UserControl {
	
	/**Creates new user and adds it to the userlist
	 * @param userid Userid to set
	 * @param username Username to set
	 * @return True if user was added, False if user already exists
	 */
	public static boolean addUser(String userid, String username, String password) {
		
		//check if user already exists
		if (userExists(userid)) {
			System.out.println("user " + userid + " already exists with name " + readUser(userid).getName());
			return false;
		} else {
			saveUser(new User(userid, username, password));
			System.out.println("created user " + userid + " with name " + username);
			return true;
		}
		
	}

	/**Deletes specified user from the userlist
	 * @param userid Userid to delete
	 * @return True if user was deleted, False if user never existed to begin with
	 */
	public static boolean deleteUser(String userid) {
		
		//check if user exists
		System.out.println("deleting user " + userid);

		File userfile = new File("data/".concat(userid));
		if (!userfile.exists() || userfile.isDirectory()) {
			System.out.println("user " + userid + " does not exist");
			return false;
		} else {
			userfile.delete();
			System.out.println("deleted user " + userid);
			return true;
		}
		
	}

	/**
	 * Lists all existing users
	 */
	public static void listUsers(DefaultListModel<String> model) {
		File data = new File("data");
		if (data.list().length == 0) {
			System.out.println("no users exist");
		} else {
			for (String file : data.list()) {
				if (!new File("data/" + file).isDirectory()) {
					model.addElement(file);
					System.out.println(file);
				}
			}
		}
	}

	/**Checks if a particular user exists
	 * @param userid Userid to search for
	 * @return True or False depending on if the user exists
	 */
	public static boolean userExists(String userid) {
		File userfile = new File("data/".concat(userid));
		return userfile.isFile();
	}
	
	/**Read user from filename and return the object, or null if something goes wrong
	 * @return User read from filename, or null if an error occurs
	 */
	public static User readUser(String userid) {
		
		//initialize input streams and new User object
		FileInputStream file;
		ObjectInputStream object = null;
		User new_user;
		String filename = "data/".concat(userid);
		
		//read file into object
		try {
			file = new FileInputStream(filename);
			object = new ObjectInputStream(file);
			new_user = (User) object.readObject();
			object.close();
		} catch (FileNotFoundException e) {
			System.out.println("File for user " + userid + " not found.");
			return null;
		} catch (IOException e) {
			System.out.println("User " + userid + "'s data is corrupted.");
			return null;
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException occurred when importing filename " + filename);
			
			//additional catch for IOException to make eclipse shut up
			try {
				object.close();
			} catch (IOException e1) {
				System.out.println("File " + filename + " is giving way too many errors :(");
			}
			return null;
		}
		
		return new_user;
	}
	
	/**Serializes user object to filename
	 * @param current_user User to serialize
	 */
	public static void saveUser(UserInterface current_user) {
		try {
			//delete preexisting file
			File file = new File("data/".concat(current_user.getUserid()));
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			} else {
				file.createNewFile();
			}
			
			//create streams
			FileOutputStream fileout = new FileOutputStream("data/".concat(current_user.getUserid()));
			ObjectOutputStream objout = new ObjectOutputStream(fileout);
			
			//write object
			objout.writeObject(current_user);
			
			//close streams
			objout.close();
			fileout.close();
		
		//error handling
		} catch (IOException e) {
			System.err.println("Error saving user " + current_user.getUserid());
			e.printStackTrace();
		}
	}

	public static void addUser(String arg, String arg1) {
		addUser(arg, arg1, "");
	}
}
