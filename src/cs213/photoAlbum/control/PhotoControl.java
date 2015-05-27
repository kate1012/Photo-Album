package cs213.photoAlbum.control;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import cs213.photoAlbum.model.*;

/**Provides methods to create, delete, manipulate, and analyze Photo objects.
 * @author Ben Green
 *
 */
public final class PhotoControl {
	
	//date format for date input/output
	public static SimpleDateFormat date_format = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");

	/**Add photo to existing album. Reports errors if necessary.
	 * @param current_user Specified user
	 * @param filename Specified photo's filename
	 * @param caption Specified caption (can be null)
	 * @param album_name Specified photo's containing album
     * @return True if successful, false if not
	 */
	public static boolean addPhoto(UserInterface current_user, String filename, String caption, String album_name) {
		AlbumInterface album = current_user.getAlbum(album_name);
		
		//make sure album exists
		if (album == null) {
			System.out.println("Album " + album_name + " does not exist for user " + current_user.getUserid());
		} else {
			
			//check if photo already exists
			if (album.hasPhoto(filename)) {
				System.out.println("Photo " + filename + " already exists in album " + album_name);
			
			
				//check to see if user is trying to add an existing photo to a new album
			} else if (caption == null) {
				
				//check to make sure photo exists in one of the user's other albums
				Photo orig_photo = current_user.getPhoto(filename);
				if (orig_photo != null) {
					album.addPhoto(orig_photo);
				} else {
					
					//photo doesn't exist and the user hasn't specified a caption
					System.out.println("Please specify a caption.");
				}
			
			//otherwise, the user is adding a brand new photo
			} else {
				
				//initialize File object
				File file = new File(filename);
				
				//make sure said file actually exists
				if (!file.exists()) {
					System.out.println("File " + filename + " does not exist.");
				
				} else {
					
					//instantiate and set calendar
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(file.lastModified());
					
					//make sure photo's timestamp isn't before the epoch
					if (cal.getTimeInMillis() < 0) {
						System.out.println("File's 'last modified' must be after 1970.");
					}
					
					//create and add photo
					Photo photo;
					photo = new Photo(filename, caption, cal);
					album.addPhoto(photo);
					System.out.println("Added photo " + filename + ":");
					System.out.println(caption + " - Album: " + album.getName());

                    return true;
				}
			}
		}
        return false;
	}
	
	/**Move photo from one album to another. Reports errors if necessary.
	 * @param current_user Specified user
	 * @param filename Specified photo's filename
	 * @param old_album Album the photo currently resides in
	 * @param new_album Album to move the photo to
     * @return True if successful, False if not
	 */
	public static boolean movePhoto(UserInterface current_user, String filename, String old_album, String new_album) {
		
		if(current_user.getAlbum(old_album) == null) {
			System.out.println(old_album + " does not exist");
            return false;
		}

        if(current_user.getAlbum(new_album) == null) {
            System.out.println(new_album + " does not exist");
            return false;
        }

		//check if photo doesn't exist
		else if (!current_user.getAlbum(old_album).hasPhoto(filename)) {
			System.out.println("Photo " + filename + " does not exist in " + old_album);
            return false;
		
		//check if new album already has said photo
		} else if (current_user.getAlbum(new_album).hasPhoto(filename)) {
			System.out.println("Photo " + filename + " already exists in " + new_album);
			return false;
		} else {
			
			//copy photo into placeholder
			Photo photo = current_user.getAlbum(old_album).getPhoto(filename);
			//add photo into new album
			current_user.getAlbum(new_album).addPhoto(photo);
			//delete photo from old album
			current_user.getAlbum(old_album).deletePhoto(photo.getFilename());
			
			System.out.println("Moved photo " + photo.getFilename() + ":");
			System.out.println(photo.getFilename() + " - From album " + old_album + " to album " + new_album);
            return true;
			
		}
	}

	/**Removes photo from album. Reports errors if necessary.
	 * @param current_user Specified user
	 * @param filename Specified photo's filename
	 * @param album_name Specified photo's containing album
     * @return True if successful, false if not
	 */
	public static boolean removePhoto(UserInterface current_user, String filename, String album_name) {
		
		//check if user has specified album
		if (!current_user.hasAlbum(album_name)) {
			System.out.println("Album "  + album_name + " does not exist for user " + current_user.getName());
		}
		
		//check if specified photo does not exist in said album
		if (!current_user.getAlbum(album_name).hasPhoto(filename)) {
			System.out.println("Photo " + filename + " is not in album " + album_name);
		} else {
			
			//delete photo from album
			current_user.getAlbum(album_name).deletePhoto(filename);
			System.out.println("Removed photo:\n" + filename + " - From album " + album_name);
            return true;
		}

        return false;
	}
	
	/**Add tag set containing a key and a value to the photo. Reports errors if necessary.
	 * @param current_user Specified user
	 * @param filename Specified photo's filename
	 * @param tag_type Tag type, can be null
	 * @param tag_value Tag value, must be included 
	 */
	public static boolean addTag(UserInterface current_user, String filename, String tag_type, String tag_value) {
		
		//check if photo exists
		Photo photo = current_user.getPhoto(filename);
		if (photo == null) {
			System.out.println("Photo " + filename + " does not exist");
		} else {
			
			if (photo.addTag(tag_type, tag_value)) {
				System.out.println("Added tag:");
				System.out.println(filename + " " + tag_type + ":" + tag_value);
                return true;
			} else {
				System.out.println("Tag already exists for " + filename + " " + tag_type + ":" + tag_value);
			}
		}
        return false;
	}
	
	/**Remove tag from photo in user's library. Reports errors if necessary.
	 * @param current_user Specified user
	 * @param filename Filename of specified photo
	 * @param tag_type Tag type
	 * @param tag_value Tag value 
	 */
	public static boolean deleteTag(UserInterface current_user, String filename, String tag_type, String tag_value) {
		
		//check if photo exists
		Photo photo = current_user.getPhoto(filename);
		if (photo == null) {
			System.out.println("Photo " + filename + " does not exist");
		} else{
			
			if (photo.deleteTag(tag_type, tag_value)) {
				System.out.println("Deleted tag:");
				System.out.println(filename + " " + tag_type + ":" + tag_value);
                return true;
			} else {
				System.out.println("Tag does not exist for " + filename + " " + tag_type + ":" + tag_value);
			}
		}
		return false;
	}
	
	/**Lists photo's information, including filename, album, date, caption, and tags
	 * @param current_user Specified user
	 * @param filename Photo to analyze
	 */
	public static void listPhotoInfo(UserInterface current_user, String filename) {
		
		//check that photo exists for specified user
		PhotoInterface photo = current_user.getPhoto(filename);
		if (photo == null) {
			System.out.println("Photo " + filename + " does not exist.");
		} else {
			
			//print photo's information
			System.out.println("Photo file name: " + photo.getFilename());
			System.out.println("Album: " + getContainingAlbums(current_user, photo));
			System.out.println("Date: " + getDate(photo));
			System.out.println("Caption: " + photo.getCaption());
			System.out.println("Tags: ");
			printTags(photo);
		}
	}
	
	/**Prints list of a user's photos that contain timestamps within a specified range of dates
	 * @param current_user Specified user
	 * @param start_date Beginning of date range in proper date_format format
	 * @param end_date End of date range in proper date_format format
	 */
	public static ArrayList<Photo> getPhotosByDate(UserInterface current_user, ArrayList<Photo> photos, Date start_date, Date end_date) {
		
		//parse date arguments and initialize calendars
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		//make sure the user isn't trying to be funny
		if (end.before(start)) {
			System.out.println("Your date range is invalid.");
			return new ArrayList<>();
		}
		date_format.setLenient(false);
		start.setTime(start_date);
		end.setTime(end_date);

		//initialize list to hold matching photos
		ArrayList<Photo> list = new ArrayList<>();

		if (photos == null) {
			photos = new ArrayList<>(current_user.getPhotos());
		}
		
		//iterate through user's photos and check if photo's timestamp is within the proper range
		for (Photo photo : photos) {
			if (photo.getTimestamp().before(end) && photo.getTimestamp().after(start)) {
				list.add(photo);
			}
		}
		
		//check if no photos were within the specified range
		if (list.isEmpty()) {
			System.out.println("No photos are within this date range.");
		} else {
			
			//sort list chronologically
			Collections.sort(list);
			System.out.println("Photos for user " + current_user.getUserid() + " in range " + start_date + " to " + end_date + ":");

            return list;
		}

        return new ArrayList<>();
	}
	
	/**Prints list of user's photos that share a tag, or possibly multiple tags
	 * @param current_user Specified user
	 * @param parsed_cmd List of tag sets containing values and (optionally) keys, comma separated, with colons
	 * separating key/value pairs
	 */
	public static ArrayList<Photo> getPhotosByTag(UserInterface current_user, ArrayList<String> parsed_cmd) {
		
		//initialize list to hold matching photos
		ArrayList<Photo> list = new ArrayList<>();
		
		//get list of photos
		Collection<Photo> photolist = current_user.getPhotos();
		if (photolist.isEmpty()) {
			System.out.println("Error: User has no photos.");
			return new ArrayList<>();
		}
		
		for (Photo photo : photolist) {
			System.out.println("adding photo " + photo.getFilename());
			list.add(photo);
		}
		
		//iterate through tag filters and apply each one
		ArrayList<String[]> parsed_tags = processTags(parsed_cmd);

		System.out.println(parsed_tags);
		
		for (String[] tag : parsed_tags) {
			for (Photo photo : photolist) {
				if (!photo.hasTag(tag[0], tag[1])) {
					list.remove(photo);
				}
			}
		}
		
		//check if list is empty
		if (list.size() == 0) {
			System.out.println("No photos match the specified tag query.");
			return new ArrayList<>();
		} else {
			
			//make list chronological
			Collections.sort(list);

            return list;
		}
	}

	private static String printTags(ArrayList<String[]> parsed_tags) {
		String ret = "";
		for (String[] tagset : parsed_tags) {
			if (tagset[0] == null) {
				ret = ret.concat(tagset[1] + " ");
			} else {
				ret = ret.concat(tagset[0] + ":" + tagset[1] + " ");
			}
			System.out.println(ret);
		}
		return ret;
	}

	/**Returns a string containing all of the albums a particular photo
	 * is in, or null if the photo is in no albums
	 * @param current_user Specified user
	 * @param photo Photo to search for
	 * @return String containing all containing albums of the photo
	 */
	private static ArrayList<AlbumInterface> getContainingAlbums(UserInterface current_user, PhotoInterface photo) {
		ArrayList<AlbumInterface> ret = new ArrayList<>();

		//iterate through user's albums and record each one that has the photo
		for (AlbumInterface album : current_user.getAlbums()) {
			if (album.hasPhoto(photo.getFilename())) {
				ret.add(album);
			}
		}

        return ret;
	}

	/**Returns string representing date photo was taken
	 * @param photo Specified photo
	 * @return Date photo was taken, in date_format format
	 */
	private static String getDate(PhotoInterface photo) {
		return date_format.format(photo.getTimestamp().getTime());
	}

	/**Print all the tags of a particular photo,
	 * Used as a helper for listPhotoInfo()
	 * @param photo Photo to use
	 */
	private static void printTags(PhotoInterface photo) {
		
		//check if photo has 'person' tags
		if (photo.getPeople().size() > 0){
			
			//iterate through person tags and print each one
			for (String person : photo.getPeople()) {
				System.out.println("person: " + person);
			}
		}
		
		//check if photo has any other tags
		if (photo.miscTagsCount() > 0) {
			java.util.Iterator<Entry<String, String>> iter = photo.getTags();
			
			//iterate through these tags and print them
			while (iter.hasNext()) {
				Entry<String, String> next = iter.next();
				System.out.println(next.getKey() + ": " + next.getValue());	
			}
		}
		
	}

	private static ArrayList<String[]> processTags(ArrayList<String> parsed_cmd) {
		
		ArrayList<String[]> return_array = new ArrayList<>();
		
		for (int x = 0; x < parsed_cmd.size(); x++) {
			if (x < parsed_cmd.size()-2) {
				if (parsed_cmd.get(x+1).equals(":")) {
					return_array.add(new String[]{parsed_cmd.get(x), parsed_cmd.get(x+2)});
					x += 2;
				} else {
					return_array.add(new String[]{null, parsed_cmd.get(x)});
				}
			} else {
				return_array.add(new String[]{null, parsed_cmd.get(x)});
			}
		}
		
		return return_array;
	}
}
