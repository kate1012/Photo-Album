package cs213.photoAlbum.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.swing.DefaultListModel;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.AlbumInterface;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.PhotoInterface;
import cs213.photoAlbum.model.UserInterface;

/**Provides methods to create, delete, analyze, and manipulate Album objects
 * This class will likely be split into two classes - one to process data,
 * and one to print data to console to facilitate GUI implementation later on.
 * @author Ben Green
 *
 */
public final class AlbumControl {

	/**Creates a new album for specified user, or reports that the album already exists
	 * @param current_user Specified user
	 * @param album_name Name for new album
	 */
	public static void createAlbum(UserInterface current_user, String album_name) {
		//check if album exists for user
		if (current_user.hasAlbum(album_name)) {
			System.out.println("album exists for user " + current_user.getUserid() + ":\n" + album_name);
		} else {
			current_user.createAlbum(album_name);
			System.out.println("created album for user " + current_user.getUserid() + ":\n" + album_name);
		}
		
	}
	
	/**Renames an album for a specific user
	 * 
	 * @param current_user Specified user
	 * @param old_album_name Name of the album to be renamed
	 * @param new_album_name New name for the album
	 */
	public static void renameAlbum(UserInterface current_user, String old_album_name, String new_album_name)
	{
		if(current_user.hasAlbum(old_album_name))
		{
			AlbumInterface current_album = current_user.getAlbum(old_album_name);
			current_album.setName(new_album_name);
			
			current_user.deleteAlbum(old_album_name);
			current_user.addAlbum(current_album);
		}
	}

	/**Deletes album for specified user, or reports that the album does not exist
	 * @param current_user Specified user
	 * @param album_name Name of album to delete
	 */
	public static void deleteAlbum (UserInterface current_user, String album_name) {
		//check if album exists for user
		System.out.println("the album trying to be deleted is " + album_name);
		if (current_user.hasAlbum(album_name)) {
			current_user.deleteAlbum(album_name);
			System.out.println("deleted album from user " + current_user.getUserid() + ": \n" + album_name);
		} else {
			System.out.println("album does not exist for user " + current_user.getUserid() + ": \n" + album_name);
		}
		
	}
	
	/**Returns timestamp of the photo in the album with the earliest timestamp
	 * @param album Album to parse
	 * @return Earliest photo's timestamp
	 */
	public static String getStartDate (AlbumInterface album) {
		//initialize placeholder calendar
		Calendar earliest = Calendar.getInstance();
		
		//iterate through photos and replace placeholder with any earlier photo's timestamp
		for (PhotoInterface photo : album.getPhotos()) {
			if (photo.getTimestamp().before(earliest)) {
				earliest = photo.getTimestamp();
			}
		}
		
		//change to date object and format into string to return
		Date date = earliest.getTime();
		return PhotoControl.date_format.format(date);
	}
	
	/**Returns timestamp of the photo in the album with the latest timestamp
	 * @param album Album to parse
	 * @return Latest photo's timestamp
	 */
	public static String getEndDate(AlbumInterface album) {
		//initialize placeholder calendar
		Calendar latest = Calendar.getInstance();
		
		//set calendar to a *very* early time(the epoch)
		latest.setTimeInMillis(0);
		
		//iterate through photos and replace placeholder with any later photo's timestamp
		for (PhotoInterface photo : album.getPhotos()) {
			if (photo.getTimestamp().after(latest)) {
				latest = photo.getTimestamp();
			}
		}
		
		//change to date object and format into string to return
		Date date = latest.getTime();
		return PhotoControl.date_format.format(date);
		
	}

	/**Parses specified user's albums and displays information about each one
	 * @param current_user Specified user
	 */
	public static void listAlbums(UserInterface current_user, DefaultListModel<String> model) {
		Collection<Album> albums = current_user.getAlbums();
		
		//check to see if the user has any albums
		if (albums.size() == 0){
			System.out.println("No albums exist for user " + current_user.getUserid());
		} else {
			System.out.println("Albums for user " + current_user.getUserid() + ":");
			
			//iterate through user's albums and display information about each one
			for (AlbumInterface album : albums) {
				model.addElement(album.getName());
				System.out.println(album.getName());
			}
		}
	}

	/**Lists all photos in specified album for specified user
	 * @param current_user Specified user
	 * @param album_name Album to parse
	 */
	public static void listPhotos(UserInterface current_user, String album_name) {

		//check to see if album exists for said user
		if (!current_user.hasAlbum(album_name)) {
			System.out.println("Album " + album_name + " does not exist for user " + current_user.getUserid());
			return;
		}
		
		//get collection of all the photos in specified	album
		Collection<Photo> photos = current_user.getAlbum(album_name).getPhotos();
		
		//check to see if said collection is empty
		if (photos.isEmpty()) {
			System.out.println("No photos in album " + album_name);
		} else {
			System.out.println("Photos for album " + album_name + ":");
			
			//iterate through photos and print each one's filename and timestamp
			for (PhotoInterface photo : photos) {
				System.out.println(photo.getFilename() + " - " + PhotoControl.date_format.format(photo.getTimestamp().getTime()));
			}
		}
	}

	public static Integer getPhotoPosition(AlbumInterface album, Photo photo) {

		System.out.println("searching for " + photo.getFilename() + " in " + album.getName());

		if (!album.hasPhoto(photo.getFilename())) {

			System.out.println("photo " + photo.getFilename() + "was not found");
			return -1;
		} else {
			int count = 0;
			for (Photo iter : album.getPhotos()) {
				if (iter.getFilename().equals(photo.getFilename())) {
					System.out.println("returning position " + count);
					return count;
				} else {
					count++;
				}
			}
		}

		return -1;
	}
}
