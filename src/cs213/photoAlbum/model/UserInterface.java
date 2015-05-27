package cs213.photoAlbum.model;

import java.util.Collection;

/**Methods for a user containing a userid, name, HashMap of albums, and a HashMap of photos.
 * @author Kate Sussman
 *
 */
public interface UserInterface {

	/**Retrieve album from user object
	 * @param name Album to retrieve
	 * @return Album searched for or null if no such album exists
	 */
	public abstract AlbumInterface getAlbum(String name);

	/**Getter for user's userid
	 * @return User's userid
	 */
	public abstract String getUserid();

	/**Getter for user's name
	 * @return User's name
	 */
	public abstract String getName();

	/**Getter for user's collection of albums
	 * @return Collection of user's albums, duplicated to protect the original HashMap
	 */
	public abstract Collection<Album> getAlbums();

	/**Searches for particular filename among user's photos
	 * @param filename Filename to search for
	 * @return Photo object or null if no such photo exists
	 */
	public abstract Photo getPhoto(String filename);

	/**Getter for user's collection of photos
	 * @return Collection of user's photos, duplicated to protect the original HashMap
	 */
	public abstract Collection<Photo> getPhotos();

	/**Creates new album for user and places it in their album collection
	 * @param album_name Name for new album
	 */
	public abstract void createAlbum(String album_name);

	/**Deletes album from user's collection
	 * @param album_name Name of album to delete
	 */
	public abstract void deleteAlbum(String album_name);

	/**Checks to see if an album exists for a specific user
	 * @param name Album name to check for
	 * @return True or False, depending on if the album exists
	 */
	public abstract boolean hasAlbum(String name);

	public abstract void addAlbum(AlbumInterface current_album);

	boolean checkPassword(String password);
}