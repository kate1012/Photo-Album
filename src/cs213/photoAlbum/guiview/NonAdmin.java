package cs213.photoAlbum.guiview;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.control.AlbumControl;
import cs213.photoAlbum.control.UserControl;
import cs213.photoAlbum.model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

/**
 * @author Kate Sussman and Ben Green
 *
 */
public class NonAdmin extends JFrame{

	private static final long serialVersionUID = 1L;
	private JPanel panel_album_lists, panel_album_info, panel_new_album;
	private JButton button_search, button_logout, button_create_album;
	private JLabel label_album_name, label_num_photos, label_oldest_photo, label_newest_photo, label_create_album;
	private JScrollPane scrollpane;
	private JList<String> list_albums;
	private static DefaultListModel<String> model;
	private JTextField textfield_new_album;
	private GridBagLayout gbl = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	static UserInterface current_user;
	static AlbumInterface current_album;
	public static String clicked_album;
	
	/**
	 * @param current_user
	 */
	public NonAdmin(UserInterface current_user)
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(current_user.getName() + "'s Albums");
		setSize(600, 350);
		setLocationRelativeTo(null);
		setVisible(true);
		setLayout(gbl);
		setResizable(false);
		
		this.current_user = current_user;
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    /* (non-Javadoc)
		     * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
		     */
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	    		System.out.println("saving stuff");
	        	UserControl.saveUser(NonAdmin.current_user);
	            System.exit(0);
		    
		        }
		    });
		
		initialize();
		layoutAlbumList();
		layoutAlbumInfo();
		layoutNewAlbumPanel();
		
		gbc.insets = new Insets(5,5,5,5);
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		this.add(button_search, gbc);
		
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		
		this.add(button_logout, gbc);
		
		gbc.insets = new Insets(5,20,20,20);
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		//gbc.gridwidth = GridBagConstraints.RELATIVE;
	
		this.add(panel_album_lists, gbc);
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridy = 2;
		gbc.gridx = 1;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		
		add(panel_album_info, gbc);
		
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridy = 3;
		
		add(panel_new_album, gbc);
	
	}
	
	/**
	 * Initializes fields
	 */
	public void initialize()
	{
		model = new DefaultListModel<String>();
		
		RelistAlbums();
		
		list_albums = new JList<String>(model);
		list_albums.addMouseListener(new RightClickListener(this));
		list_albums.addListSelectionListener(new AlbumClicked(this));
		list_albums.addMouseListener(new AlbumDoubleClicked(this));
		
		scrollpane = new JScrollPane(list_albums, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		scrollpane.setPreferredSize(new Dimension(200,260));
		
		//menu = new JMenu();
		
		button_logout = new JButton("Logout User");
		button_logout.addActionListener(new ButtonClicked(this));
		panel_album_lists = new JPanel();
		panel_album_info = new JPanel();
		panel_new_album = new JPanel();

		//TODO make search work
		button_search = new JButton("Search");
		button_search.addActionListener(new ButtonClicked(this));
		button_create_album = new JButton("Create Album");
		button_create_album.addActionListener(new ButtonClicked(this));
		
		label_album_name = new JLabel("");
		label_num_photos = new JLabel("");
		label_oldest_photo = new JLabel("");
		label_newest_photo = new JLabel("");
		label_create_album = new JLabel("Create new album:");
		
		textfield_new_album = new JTextField(10);	
	}
	
	/**
	 * Sets layout for list of albums in its panel
	 */
	public void layoutAlbumList()
	{
		panel_album_lists.setLayout(gbl);
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel_album_lists.add(scrollpane, gbc);
	}
	
	/**
	 * Sets layout for the album info in its panel
	 */
	public void layoutAlbumInfo()
	{
		panel_album_info.setLayout(gbl);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel_album_info.add(label_album_name, gbc);
		gbc.gridy = 1;
		panel_album_info.add(label_num_photos, gbc);
		gbc.gridy = 2;
		panel_album_info.add(label_oldest_photo, gbc);
		gbc.gridy = 3;
		panel_album_info.add(label_newest_photo, gbc);
	}
	
	/**
	 * Sets layout for the panel to create a new album
	 */
	public void layoutNewAlbumPanel()
	{
		panel_new_album.setLayout(gbl);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		panel_new_album.add(label_create_album, gbc);
		
		gbc.gridy = 1;
		
		panel_new_album.add(textfield_new_album, gbc);
		
		gbc.gridy = 2;
		
		panel_new_album.add(button_create_album, gbc);
	}
	
	/**
	 * Repopulates the album names in the list
	 */
	public void RelistAlbums()
	{
		model.clear();
		AlbumControl.listAlbums(current_user, model);
	}

	/**
	 * @param album_name the name of the album
	 * Gets the current album info and sets the labels accordingly
	 */
	public void getAlbumInfo(String album_name)
	{
		
		Collection<Album> albums = current_user.getAlbums();
		
		for(Album album : albums)
		{
			if(album.getName().equals(album_name))
				current_album = album;
		}
		
		if(!model.isEmpty() && current_album != null)
		{
			label_album_name.setText("Album name: " + current_album.getName());
			label_num_photos.setText("Num Photos: " + Integer.toString(current_album.getSize()));
			String earliest_time = AlbumControl.getStartDate(current_album);
			String latest_time = AlbumControl.getEndDate(current_album);
			label_oldest_photo.setText("Oldest photo: " + latest_time);
			label_newest_photo.setText("Newest photo: " + earliest_time);
		}
	}
	
	/**
	 * Clears the album info textfields  
	 */
	public void resetAlbumInfo()
	{
		label_album_name.setText("");
		label_num_photos.setText("");
		label_oldest_photo.setText("");
		label_newest_photo.setText("");
	}

/**
 * Occurs when a button is clicked
 *
 */
class ButtonClicked implements ActionListener 
{
	NonAdmin nonadmin;
	
	public ButtonClicked(NonAdmin nonadmin)
	{
		this.nonadmin = nonadmin;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		/*creates a new album and re-populates the list to include it*/
		if(e.getSource() == button_create_album && textfield_new_album.getText().length() != 0)
		{
			AlbumControl.createAlbum(current_user, textfield_new_album.getText());
			RelistAlbums();
			textfield_new_album.setText("");
		}
		if(e.getSource() == button_logout)
		{
			UserControl.saveUser(current_user);
			nonadmin.dispose();
			new Login().setVisible(true);
		}
		if(e.getSource() == button_search)
		{
			new SearchFrame((User)current_user).setVisible(true);
			dispose();
		}
		
	}
}

	/**
	 * Occurs when an album is clicked. Gets the clicked album and shows its info
	 *
	 */
	class AlbumClicked implements ListSelectionListener
	{
		public NonAdmin nonadmin;

		public AlbumClicked(NonAdmin nonadmin)
		{
			this.nonadmin = nonadmin;
		}

		/* (non-Javadoc)
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {
			current_album = nonadmin.current_user.getAlbum(clicked_album);
			NonAdmin.clicked_album = nonadmin.list_albums.getSelectedValue();
			getAlbumInfo(clicked_album);
	
		}
	}
	
	/**
	 * Occurs when an album is double clicked
	 *
	 */
	class AlbumDoubleClicked implements MouseListener
	{

		NonAdmin nonadmin;
		
		public AlbumDoubleClicked(NonAdmin nonadmin) {
			this.nonadmin = nonadmin;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2)
			{
				if((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
				{
					new OpenAlbum(nonadmin.current_user, current_album).setVisible(true);
					nonadmin.dispose();
				}		
			}			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	/**
	 * Creates the right click menu and adds components
	 *
	 */
	class RightClickMenu extends JPopupMenu implements ActionListener 
	{

		private static final long serialVersionUID = 1L;
		NonAdmin nonadmin;

	    JMenuItem delete;
	    JMenuItem rename;
	    JMenuItem open;
	    
	    public RightClickMenu(NonAdmin nonadmin){
	    	this.nonadmin = nonadmin; 

	        delete = new JMenuItem("Delete Album");
	        delete.addActionListener(this);
	        rename = new JMenuItem("Rename Album");
	        rename.addActionListener(this);
	        open = new JMenuItem("Open Album");
	        open.addActionListener(this);

	        add(delete);
	        add(rename);
	       // add(open);
	    }
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == delete)
			{
				AlbumControl.deleteAlbum(current_user, NonAdmin.current_album.getName());
				nonadmin.RelistAlbums();	
				nonadmin.resetAlbumInfo();
			}			
			if(e.getSource() == rename)
			{
				new RenameAlbum(nonadmin, NonAdmin.clicked_album).setVisible(true);
				nonadmin.resetAlbumInfo();
			}
		}
	}
	
	/**
	 * Shows the right click menu
	 *
	 */
	class RightClickListener extends MouseAdapter {
		NonAdmin nonadmin;
		
		public RightClickListener(NonAdmin nonadmin)
		{
			this.nonadmin = nonadmin;
		}
		
	    /* (non-Javadoc)
	     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	     */
	    public void mousePressed(MouseEvent e){
	        if (e.isPopupTrigger())
	            doPop(e);
	    }

	    /* (non-Javadoc)
	     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	     */
	    public void mouseReleased(MouseEvent e){
	        if (e.isPopupTrigger())
	            doPop(e);
	    }

	    /**
	     * @param e event
	     * Creates and shows the right click menu
	     */
	    private void doPop(MouseEvent e){
	        RightClickMenu menu = new RightClickMenu(nonadmin);
	        menu.show(e.getComponent(), e.getX(), e.getY());
	    }
	}
}
