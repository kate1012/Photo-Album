package cs213.photoAlbum.guiview;

import javax.swing.*;

import cs213.photoAlbum.control.AlbumControl;
import cs213.photoAlbum.control.PhotoControl;
import cs213.photoAlbum.control.UserControl;
import cs213.photoAlbum.model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Kate Sussman and Ben Green
 *
 */
public class PhotoInfo extends JFrame{
	
	private JPanel panel_main;
	private JLabel label_caption;
	protected String album_name;
	private JLabel label_timestamp;
	private JLabel label_timestamp_time;
	private JLabel label_album;
	protected JTextField textfield_caption, addtag_keyfield, addtag_valuefield, deletetag_keyfield, deletetag_valuefield;
	private JTextField[] tag_keys, tag_values;
	private JButton button_save, button_cancel, button_addtag, button_deletetag;
	private JComboBox<String> albums_combobox;
	GridBagConstraints gbc = new GridBagConstraints();
	PhotoInterface current_photo;
	static UserInterface current_user;
	ArrayList<String> tags_keys_arraylist;
	ArrayList<String> tags_values_arraylist;

	public PhotoInfo(UserInterface current_user, PhotoInterface current_photo, String album_name)
	{
		this.current_user = current_user;
		this.current_photo = current_photo;
		this.album_name = album_name;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(550, 400);

		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    /* (non-Javadoc)
		     * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
		     */
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	    		System.out.println("saving stuff");
	        	UserControl.saveUser(PhotoInfo.current_user);
	            System.exit(0);
		    
		        }
		    });



		setTitle("Edit Info for " + current_photo.getFilename());
		//this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		
		initialize();
		Layout();
		
		add(panel_main);
	}

	
	/**
	 * Initializes fields
	 */
	public void initialize()
	{
		panel_main = new JPanel();
		label_caption = new JLabel("Caption:");
		label_timestamp = new JLabel("Timestamp:");
		label_timestamp_time = new JLabel(PhotoControl.date_format.format(current_photo.getTimestamp().getTime()));
		button_addtag = new JButton("Add Tag:");
		label_album = new JLabel("Album:");
	
		textfield_caption = new JTextField(10);
		textfield_caption.setMinimumSize(new Dimension(200,30));
		textfield_caption.setText(current_photo.getCaption());

		addtag_keyfield = new JTextField(10);
		addtag_keyfield.setMinimumSize(new Dimension(150,30));
		addtag_valuefield = new JTextField(10);
		addtag_valuefield.setMinimumSize(new Dimension(150,30));

		deletetag_keyfield = new JTextField(10);
		deletetag_keyfield.setMinimumSize(new Dimension(150,30));
		deletetag_valuefield = new JTextField(10);
		deletetag_valuefield.setMinimumSize(new Dimension(150,30));

		button_deletetag = new JButton("Delete Tag:");
		button_addtag.addActionListener(new ButtonListener(this));
		button_deletetag.addActionListener(new ButtonListener(this));
		
		//albums_arraylist = new ArrayList<String>();
		Collection<Album> albums_collection = current_user.getAlbums();
		albums_combobox = new JComboBox<String>();
		for (AlbumInterface album : albums_collection) {
			albums_combobox.addItem(album.getName());
		}

		albums_combobox.setSelectedItem(album_name);
		
		button_save = new JButton("Save");
		button_save.addActionListener(new ButtonListener(this));
		button_cancel = new JButton("Cancel");
		button_cancel.addActionListener(new ButtonListener(this));
	}
	
	/**
	 * Sets layout and adds components 
	 */
	public void Layout()
	{
		panel_main.setLayout(new GridBagLayout());
		
		gbc.insets = new Insets(10,30,5,30);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		gbc.anchor = GridBagConstraints.WEST;
		
		panel_main.add(label_caption, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.EAST;
		
		panel_main.add(textfield_caption, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		
		panel_main.add(label_timestamp, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.EAST;
		
		panel_main.add(label_timestamp_time, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		
		panel_main.add(button_addtag, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.EAST;
		
		panel_main.add(addtag_keyfield, gbc);
		
		gbc.gridx = 2;
		
		panel_main.add(addtag_valuefield, gbc);
		
		gbc.gridx = 3;
		
		gbc.gridx = GridBagConstraints.RELATIVE;
		////////////////////////////////////////////////////////////

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;

		panel_main.add(button_deletetag, gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.EAST;

		panel_main.add(deletetag_keyfield, gbc);

		gbc.gridx = 2;

		panel_main.add(deletetag_valuefield, gbc);

		gbc.gridx = 3;

		gbc.gridx = GridBagConstraints.RELATIVE;
		
		gbc.gridx = 1;
		gbc.gridy++;
		gbc.weightx = 1;
		
		
		
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		
		panel_main.add(label_album, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.EAST;
		
		panel_main.add(albums_combobox, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		
		panel_main.add(button_save, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		
		panel_main.add(button_cancel, gbc);
		
	}
	
	/**
	 * Occurs when button is clicked
	 *
	 */
	class ButtonListener implements ActionListener
	{
		
		PhotoInfo photoinfo;
		
		public ButtonListener(PhotoInfo photoinfo)
		{
			this.photoinfo = photoinfo;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == photoinfo.button_save)
			{
				current_photo.setCaption(photoinfo.textfield_caption.getText());

				System.out.println("target album " + photoinfo.albums_combobox.getSelectedItem().toString());

				if (!photoinfo.albums_combobox.getSelectedItem().toString().equals(photoinfo.album_name)) {
					PhotoControl.movePhoto(photoinfo.current_user, current_photo.getFilename(), photoinfo.album_name, photoinfo.albums_combobox.getSelectedItem().toString());
				}

				AlbumInterface album = photoinfo.current_user.getAlbum(album_name);
				new OpenAlbum(photoinfo.current_user, album).setVisible(true);
				photoinfo.dispose();
				
			}
			if(e.getSource() == photoinfo.button_cancel)
			{

				AlbumInterface album = photoinfo.current_user.getAlbum(album_name);
				new OpenAlbum(photoinfo.current_user, album).setVisible(true);
				photoinfo.dispose();
			}
			if(e.getSource() == photoinfo.button_addtag) {
				if (!photoinfo.addtag_keyfield.getText().isEmpty() && !photoinfo.addtag_valuefield.getText().isEmpty()) {
					PhotoControl.addTag(photoinfo.current_user, photoinfo.current_photo.getFilename(), photoinfo.addtag_keyfield.getText(), photoinfo.addtag_valuefield.getText());
					addtag_keyfield.setText("");
					addtag_valuefield.setText("");
				}
			}
			if(e.getSource() == photoinfo.button_deletetag) {
				if (!photoinfo.deletetag_keyfield.getText().isEmpty() && !photoinfo.deletetag_valuefield.getText().isEmpty()) {
					PhotoControl.deleteTag(photoinfo.current_user, photoinfo.current_photo.getFilename(), photoinfo.deletetag_keyfield.getText(), photoinfo.deletetag_valuefield.getText());
					deletetag_keyfield.setText("");
					deletetag_valuefield.setText("");
				}
			}
			
		}
		
	}




}
