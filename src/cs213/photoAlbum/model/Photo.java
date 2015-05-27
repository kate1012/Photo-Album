package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**Object to represent a photo, containing information such as filename, timestamp, and tags
 * @author Ben Green
 *
 */
public class Photo implements Serializable, Comparable<Photo>, PhotoInterface {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// FIELDS
	
	//photo info
	private String filename;
	private String caption;
	
	//timestamp & tags
	private Calendar timestamp;
	private ArrayList<String> people;
	private HashMap<String, String> other_tags;

	// CONSTRUCTOR
	
	/**Constructor for Photo object
	 * @param filename Filename to set
	 * @param caption Caption to set
	 * @param timestamp Timestamp to set, in Calendar format
	 */
	public Photo(String filename, String caption, Calendar timestamp) {
		this.filename = filename;
		this.caption = caption;
		this.timestamp = timestamp;
		this.people = new ArrayList<>();
		this.other_tags = new HashMap<>();
	}
	
	// METHODS




	/* (non-Javadoc)
             * @see cs213.photoAlbum.model.PhotoInterface#compareTo(cs213.photoAlbum.model.Photo)
             */
	@Override
	//used to sort photos chronologically
	public int compareTo(Photo photo) {
		return this.getTimestamp().compareTo(photo.getTimestamp());
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#miscTagsCount()
	 */
	@Override
	public int miscTagsCount() {
		return other_tags.size();
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#getTags()
	 */
	@Override
	public Iterator<Entry<String,String>> getTags() {
		return other_tags.entrySet().iterator();
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#getPeople()
	 */
	@Override
	public ArrayList<String> getPeople() {
		return people;
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#getFilename()
	 */
	@Override
	public String getFilename() {
		return filename;
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#getCaption()
	 */
	@Override
	public String getCaption() {
		return caption;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#getTimestamp()
	 */
	@Override
	public Calendar getTimestamp() {
		return timestamp;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#getTagValue(java.lang.String)
	 */
	@Override
	public String getTagValue(String key) {
		return other_tags.get(key);
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#setFilename(java.lang.String)
	 */
	@Override
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#setCaption(java.lang.String)
	 */
	@Override
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#setTimestamp(java.util.Calendar)
	 */
	@Override
	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#addTag(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addTag(String tag_type, String tag_value) {
		
		//handle person tags
		if (tag_type.equals("person")) {
			if (people.contains(tag_value)) {
				return false;
			} else {
				people.add(tag_value);
				return true;
			}
		}
		
		//handle all other tags
		if (other_tags.containsKey(tag_type)) {
			return false;
		} else {
			other_tags.put(tag_type, tag_value);
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#deleteTag(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean deleteTag(String tag_type, String tag_value) {
		
		//handle person tags
		if (tag_type.equals("person")) {
			return people.remove(tag_value);
		}
		
		//handle all other tags
		if (other_tags.containsKey(tag_type)) {
			if (other_tags.get(tag_type).equals(tag_value)) {
				other_tags.remove(tag_type);
				return true;
			} else {
				return false;
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.PhotoInterface#hasPerson(java.lang.String)
	 */
	@Override
	public boolean hasPerson(String person) {
		return this.people.contains(person);
	}
	
	@Override
	public int compareTo(PhotoInterface photo) {
		return this.timestamp.compareTo(photo.getTimestamp());
	}

	/**Searches photo for a tag value and optionally tag key
	 * @param tag_type Tag type, can be left null
	 * @param tag_value Tag value, must not be null
	 * @return True if tag set is in photo, False otherwise
	 */
	public boolean hasTag(String tag_type, String tag_value) {
		
		//handle sole tag value cases
		if (tag_type == null) {
			return people.contains(tag_value) || other_tags.containsValue(tag_value);
		
		//handle cases where tag type is also specified
		} else {
			if (tag_type.equals("person")) {
				if (people.contains(tag_value)) {
					return true;
				}
			} else {
				if (other_tags.containsKey(tag_type)) {
					if (other_tags.get(tag_type).equals(tag_value)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
