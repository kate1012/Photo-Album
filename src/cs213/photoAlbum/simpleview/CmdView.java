/**
 * 
 */
package cs213.photoAlbum.simpleview;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs213.photoAlbum.control.AlbumControl;
import cs213.photoAlbum.control.PhotoControl;
import cs213.photoAlbum.control.UserControl;
import cs213.photoAlbum.model.UserInterface;

/**
 * @author Kate Sussman & Ben Green
 * TODO format all errors properly
 */
public class CmdView {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//do data import things here
		checkData();
		
		
		/*
		 * 
		 * ADMIN MODE STARTS HERE
		 * ADMIN MODE STARTS HERE
		 * ADMIN MODE STARTS HERE
		 * ADMIN MODE STARTS HERE
		 * 
		 */
		
		if (args.length == 0) {
			System.out.println("Type a command pls.");
			return;
		}
		
		switch(args[0]) {
		
		case ("listusers"):{
			//UserControl.listUsers();
			break;
		}
		
		case ("adduser"): {
			if (args.length != 3) {
				System.out.println("usage: adduser <userid> <username>");
			} else {
				UserControl.addUser(args[1], args[2]);
			}
			break;
		}
		
		case ("deleteuser"): {
			if (args.length != 2) {
				System.out.println("usage: deleteuser <userid>");
			} else {
				UserControl.deleteUser(args[1]);
			}
			break;
		}
		
		/*
		 * 
		 * INTERACTIVE MODE STARTS HERE
		 * INTERACTIVE MODE STARTS HERE
		 * INTERACTIVE MODE STARTS HERE
		 * INTERACTIVE MODE STARTS HERE
		 * 
		 */
		
		case ("login"): {
			
			//check if user exists
			if(args.length != 2)
			{
				System.out.println("Incorrect number of arguments");
			} 
			UserInterface current_user = UserControl.readUser(args[1]);
			if (current_user == null) {
				return;
			}
			
			//initialize input scanner
			Scanner input = new Scanner(System.in);
			String cmd = "";
			
			//start command loop
			while (true) {
				
				//get input and tokenize using spaces
				cmd = input.nextLine();
				
				ArrayList<String> parsed_cmd = parseInput(cmd);
				
				
				if (parsed_cmd.size() > 0) {
					switch (parsed_cmd.get(0)) {
					
					case ("createAlbum"): {
						if (parsed_cmd.size() == 2) {
							AlbumControl.createAlbum(current_user, parsed_cmd.get(1));
						} else {
							System.out.println("usage: createAlbum <name>");
						}
						break;
					}
					
					case ("deleteAlbum"): {
						if (parsed_cmd.size() == 2) {
							AlbumControl.deleteAlbum(current_user, parsed_cmd.get(1));
						} else {
							System.out.println("usage: deleteAlbum <name>");
						}
						break;
					}
					
					case ("listAlbums"): {
						//AlbumControl.listAlbums(current_user);
						break;
					}
					
					case ("listPhotos"): {
						if (parsed_cmd.size() == 2) {
							AlbumControl.listPhotos(current_user, parsed_cmd.get(1));
						} else {
							System.out.println("usage: listPhotos <album_name>");
						}
						break;
					}
				
					case ("addPhoto"): {
						//if (parsed_cmd.size() > 4 || parsed_cmd.size() < 2) {
						if (parsed_cmd.size() != 4 && parsed_cmd.size() != 3) {
							System.out.println("usage: addPhoto <filename> [<caption>] <albumname>");
						} else {
							//handle normal photo
							if (parsed_cmd.size() == 4) {
								PhotoControl.addPhoto(current_user, getCanonName(parsed_cmd.get(1)), parsed_cmd.get(2), parsed_cmd.get(3));
							} else {
								//handle photo in new album
								PhotoControl.addPhoto(current_user, getCanonName(parsed_cmd.get(1)), null, parsed_cmd.get(2));
							}
						}
						break;
					}
					
					case ("removePhoto"): {
						if (parsed_cmd.size() != 3) {
							System.out.println("usage: deletePhoto <filename> <album_name>");
						} else {
							PhotoControl.removePhoto(current_user, getCanonName(parsed_cmd.get(1)), parsed_cmd.get(2));
						}
						break;
					}
					
					case ("movePhoto"): {
						if (parsed_cmd.size() != 4) {
							System.out.println("usage: movePhoto <filename> <old_album> <new_album>");
						} else {
								PhotoControl.movePhoto(current_user, getCanonName(parsed_cmd.get(1)), parsed_cmd.get(2), parsed_cmd.get(3));
						}
						break;
					}
					
					case ("listPhotoInfo"): {
						if (parsed_cmd.size() != 2) {
							System.out.println("usage: listPhotoInfo <filename>");
						} else {
							PhotoControl.listPhotoInfo(current_user, getCanonName(parsed_cmd.get(1)));
						}
						break;
					}
					
					case ("addTag"): {
						if (parsed_cmd.size() != 5) {
							System.out.println("usage: addTag <filename> <type:value>");
						} else {
							PhotoControl.addTag(current_user, getCanonName(parsed_cmd.get(1)), parsed_cmd.get(2), parsed_cmd.get(4));
						}
						break;
					}
					
					case ("deleteTag"): {
						if (parsed_cmd.size() != 5) {
							System.out.println("usage: deleteTag <filename> <type:value>");
						} else {
							PhotoControl.deleteTag(current_user, getCanonName(parsed_cmd.get(1)), parsed_cmd.get(2), parsed_cmd.get(4));
						}
						break;
					}
					
					case ("getPhotosByDate"): {
						if (parsed_cmd.size() != 3) {
							System.out.println("usage: getPhotosByDate <start> <end>");
						} else {
							try {
								PhotoControl.getPhotosByDate(current_user, null, PhotoControl.date_format.parse(parsed_cmd.get(1)), PhotoControl.date_format.parse(parsed_cmd.get(1)));
							} catch (ParseException e) {
								System.out.println("Parse exception occurred.");
							}
						}
						break;
					}
					
					case ("getPhotosByTag"): {
						if (parsed_cmd.size() < 2) {
							System.out.println("usage: getPhotosByTag <tag_type>:<tag_value>...");
						} else {
							PhotoControl.getPhotosByTag(current_user, parsed_cmd);
						}
						break;
					}
					
					case ("logout"): {
						UserControl.saveUser(current_user);
						input.close();
						return;
					}
					
					default: {
						System.out.println("Unexpected command.");
					}
					}
				} else {
					System.out.println("Please enter a command.");
				}
			}
		}
		
		default: {
			System.out.println("Unexpected command. Type 'help' for help.");
		}
			
		}

	}
	
	public static ArrayList<String> parseInput(String cmd) {
		
		//check if input string is empty
		if (cmd.trim().length() == 0) {
			return new ArrayList<String>();
		}
		
		//handle date case
		if (cmd.startsWith("getPhotosByDate")) {
			ArrayList<String> list = new ArrayList<String>();
			String[] parsed = cmd.split(" ");
			if (parsed.length != 3) {
				return list;
			} else {
				try {
					PhotoControl.date_format.parse(parsed[1]);
					PhotoControl.date_format.parse(parsed[2]);
				} catch (java.text.ParseException e) {
					return new ArrayList<String>();
				}
				list.add(parsed[0]);
				list.add(parsed[1]);
				list.add(parsed[2]);
				return list;
			}
		}
		
		String[] tokz = cmd.split(":");
		ArrayList<String> list = new ArrayList<String>();
		
		for (String string : tokz) {
			Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(string);
			while (m.find())
			    list.add(m.group(1).replace("\"", ""));
			list.add(":");
		}
		
		list.remove(list.size()-1);
		return list;
	}
	
	private static String getCanonName(String filename) {
		File file = new File(filename);
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			System.out.println("Error: Problem retrieving file's canonical path.");
			return null;
		}
	}
	
	
	/**Checks if data folder exists / creates it if not found
	 * 
	 */
	private static void checkData() {
		File data = new File("data");
		if (!data.isDirectory()) {
			if (!data.exists()) {
				System.err.println("Initializing data folder");
				data.mkdir();
			} else {
				System.err.println("Some 'data' file exists, please delete or move it");
			}
		}
		
	}

}
