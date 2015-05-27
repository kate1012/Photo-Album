package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**Representation of a photo album with a name as well as a HashMap of photos.
 * @author Ben Green
 *
 */
public class Album implements Serializable, AlbumInterface {
	
	// FIELDS
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * HashMap storing Photo objects, and String representation of album's name
	 */
	private HashMap<String, Photo> photos;
	private String name;
	
	// CONSTRUCTOR
	
	/**Constructor for Album object
	 * @param album_name Name for new album
	 */
	public Album(String album_name) {
		this.name = album_name;
		this.photos = new HashMap<>();
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.AlbumInterface#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.AlbumInterface#getSize()
	 */
	@Override
	public int getSize() {
		return photos.size();
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.AlbumInterface#getPhotos()
	 */
	@Override
	public Collection<Photo> getPhotos() {
		return new HashMap<>(photos).values();
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.AlbumInterface#getPhoto(java.lang.String)
	 */
	@Override
	public Photo getPhoto(String filename) {
		return photos.get(filename);
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.AlbumInterface#addPhoto(cs213.photoAlbum.model.Photo)
	 */
	@Override
	public void addPhoto(Photo photo) {
		photos.put(photo.getFilename(), photo);
		
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.AlbumInterface#deletePhoto(java.lang.String)
	 */
	@Override
	public void deletePhoto(String filename) {
		photos.remove(filename);
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.AlbumInterface#hasPhoto(java.lang.String)
	 */
	@Override
	public boolean hasPhoto(String filename) {
		return photos.containsKey(filename);
	}

	@Override
	public Photo getPhoto(Integer pos) {

		if (!hasPhotoAt(pos)) {
			return null;
		}


		ArrayList<Photo> photos = new ArrayList<>();

		for (Photo photo : this.photos.values()) {
			photos.add(photo);
		}

		System.out.println("found photo at position " + pos);

		return photos.get(pos);
	}

	@Override
	public void setName(String new_name) {
		this.name = new_name;
		
	}

	@Override
	public boolean hasPhotoAt(int pos) {
		return !(pos < 0 || pos > photos.size() - 1);
	}

}
