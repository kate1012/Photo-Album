package cs213.photoAlbum.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map.Entry;

/**Methods for a photo, containing information such as filename, timestamp, and tags
 * @author Ben Green
 *
 */
public interface PhotoInterface {

	//used to sort photos chronologically
	public abstract int compareTo(PhotoInterface photo);

	/**Retrieves number of misc tags the photo has (excluding location and 'person' tags)
	 * @return Number of misc tags
	 */
	public abstract int miscTagsCount();

	/**Retrieves photo's misc tags (excluding location and 'person' tags)
	 * @return Iterator containing said misc tags
	 */
	public abstract Iterator<Entry<String, String>> getTags();

	/**Getter for array of 'person' tags
	 * @return ArrayList of 'person' tags as Strings
	 */
	public abstract ArrayList<String> getPeople();

	/**Getter for photo's filename
	 * @return Photo's filename
	 */
	public abstract String getFilename();

	/**Getter for photo's caption
	 * @return Photo's caption
	 */
	public abstract String getCaption();

	/**Getter for photo's timestamp
	 * @return Photo's timestamp
	 */
	public abstract Calendar getTimestamp();

	/**Finds matching value for a specified misc tag key
	 * @param key Key to search for
	 * @return Value corresponding to said key (or null if no such key exists)
	 */
	public abstract String getTagValue(String key);

	/**Setter for photo's filename
	 * @param filename Filename to set
	 */
	public abstract void setFilename(String filename);

	/**Setter for photo's caption
	 * @param caption Caption to set
	 */
	public abstract void setCaption(String caption);

	/**Setter for photo's timestamp
	 * @param timestamp Timestamp to set
	 */
	public abstract void setTimestamp(Calendar timestamp);

	/**Adds arbitrary(not person or location) tag to photo
	 * @param tag_type Tag key to add
	 * @param tag_value Tag value to add
	 * @return True if tag was successfully added, False if tag already existed
	 */
	public abstract boolean addTag(String tag_type, String tag_value);

	/**Removes tag type/value set from photo
	 * @param tag_type Tag type to remove
	 * @param tag_value Tag value to remove
	 * @return True if tag was deleted, False if tag did not exist (or if tag value did not match specified value)
	 */
	public abstract boolean deleteTag(String tag_type, String tag_value);

	/**Checks if photo's 'people' tag contains a specified person
	 * @param person Person to search for
	 * @return True or False depending if searched person is in the photo's 'people' array
	 */
	public abstract boolean hasPerson(String person);

	public abstract boolean hasTag(String tag_type, String tag_value);

}