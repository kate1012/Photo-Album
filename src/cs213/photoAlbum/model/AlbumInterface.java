package cs213.photoAlbum.model;

import java.util.Collection;

/**Methods for a photo album with a name as well as a HashMap of photos.
 * @author Ben Green
 *
 */
public interface AlbumInterface {

	/**Getter for album's name
	 * @return Album's name
	 */
	String getName();

	/**Setter for album's name
	 * 
	 */
	void setName(String new_name);
	
	/**Getter for album's size
	 * @return Number of photos in the album
	 */

	int getSize();

	/**Getter for album's photos
	 * @return Collection of album's photos, duplicated to protect the original HashMap
	 */
	Collection<Photo> getPhotos();

	/**Retrieves photo from album
	 * @param filename Filename to search for
	 * @return Photo object or null if said photo does not exist
	 */
	Photo getPhoto(String filename);

	/**Add photo to album
	 * @param photo Photo to add
	 */
	void addPhoto(Photo photo);

	/**Delete photo from album
	 * @param filename Photo to delete
	 */
	void deletePhoto(String filename);

	/**Searches album for photo by filename
	 * @param filename Filename to search for
	 * @return True or False, depending on if the photo exists
	 */
	boolean hasPhoto(String filename);

	Photo getPhoto(Integer pos);

	boolean hasPhotoAt(int pos);
}