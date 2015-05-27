package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**Representation of a user containing a userid, name, HashMap of albums, and a HashMap of photos.
 * @author Kate Sussman
 *
 */
public class User implements Serializable, UserInterface {
	
	
	// FIELDS
	
	/**
	 * Generic serial ID to make Eclipse happy
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Fields for userid and name
	 */
	private String userid;
	private String name;
	private String password;
	
	/**
	 * Containers for albums and photos (albums will contain photos as well, but
	 * having a separate photo storage enables much faster searches
	 */
	private HashMap<String, Album> albums;
	
	
	// CONSTRUCTOR
	
	public User(String userid, String username, String password) {
		this.userid = userid;
		this.name = username;
		this.password = password;
		this.albums = new HashMap<String, Album>();
	}
	
	// METHODS
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.UserInterface#getAlbum(java.lang.String)
	 */
	@Override
	public AlbumInterface getAlbum(String name) {
		
		//check that album exists
		if (this.hasAlbum(name)){
			return albums.get(name);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.UserInterface#getUserid()
	 */
	@Override
	public String getUserid() {
		return userid;
	}
	
	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.UserInterface#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.UserInterface#getAlbums()
	 */
	@Override
	public Collection<Album> getAlbums() {
		return new HashMap<String, Album>(albums).values();
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.UserInterface#getPhoto(java.lang.String)
	 */
	@Override
	public Photo getPhoto(String filename) {
		for (Album album : albums.values()) {
			if (album.hasPhoto(filename)) {
				return album.getPhoto(filename);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.UserInterface#createAlbum(java.lang.String)
	 */
	@Override
	public void createAlbum(String album_name) {
		albums.put(album_name, new Album(album_name));
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.UserInterface#deleteAlbum(java.lang.String)
	 */
	@Override
	public void deleteAlbum(String album_name) {
		
		//check that album exists
		if (this.hasAlbum(album_name)) {
			albums.remove(album_name);
		} else {
			System.out.println("Tried to delete a nonexistant album.");
			//TODO make this (and probably other methods here) boolean
		}
		
	}

	/* (non-Javadoc)
	 * @see cs213.photoAlbum.model.UserInterface#hasAlbum(java.lang.String)
	 */
	@Override
	public boolean hasAlbum(String name) {
		return albums.containsKey(name);
	}

	@Override
	public Collection<Photo> getPhotos() {
		HashMap<String, Photo> photos = new HashMap<String, Photo>();
		
		for(Album album : albums.values()) {
			for (Photo photo : album.getPhotos()) {
				if (!photos.containsKey(photo.getFilename())) {
					photos.put(photo.getFilename(), photo);
				}
			}
		}
		return photos.values();
	}

	@Override
	public void addAlbum(AlbumInterface current_album) {
		albums.put(current_album.getName(), (Album) current_album);
		
	}

	@Override
	public boolean checkPassword(String password){
		if (this.password != null) {
			return this.password.equals(password);
		} else {
			return true;
		}
	}
	

}
