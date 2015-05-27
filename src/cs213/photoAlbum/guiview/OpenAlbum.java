package cs213.photoAlbum.guiview;

import cs213.photoAlbum.control.AlbumControl;
import cs213.photoAlbum.control.PhotoControl;
import cs213.photoAlbum.control.UserControl;
import cs213.photoAlbum.guiview.NonAdmin.RightClickMenu;
import cs213.photoAlbum.model.AlbumInterface;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.PhotoInterface;
import cs213.photoAlbum.model.UserInterface;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Kate Sussman and Ben Green
 *
 */
public class OpenAlbum extends JFrame{

	private static final long serialVersionUID = 1L;
	JButton button_addphoto, button_user, button_back;
	JScrollPane scroller;
	JPanel panel_main, panel_pics;
	ArrayList<Photo> photos;
	ArrayList<JLabel> captions;
	ImageIcon pic1, pic2, pic3;
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	static UserInterface current_user;
	AlbumInterface current_album;
	JFileChooser fc;
	PhotoInterface current_photo;
	
	
	public OpenAlbum(UserInterface current_user, AlbumInterface current_album)
	{
		this.current_user = current_user;
		this.current_album = current_album;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(750, 500);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setTitle(current_album.getName());
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    /* (non-Javadoc)
		     * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
		     */
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	    		System.out.println("saving stuff");
	        	UserControl.saveUser(OpenAlbum.current_user);
	            System.exit(0);
		    
		        }
		    });
		
		initialize();
		
		LayoutMainPanel();	
		
		this.add(panel_main);
	}
	
	/**
	 * Initializes fields
	 */
	public void initialize()
	{
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "JPG, GIF and PNG Images", "jpg", "gif", "png");
		fc.setFileFilter(filter);
		
		panel_main = new JPanel();
		panel_pics = new JPanel(new GridLayout(0,3, 10, 10));


		button_user = new JButton("Logout User");
		button_user.addActionListener(new Button_Clicked(this));
		button_back = new JButton("Back");
		button_back.addActionListener(new Button_Clicked(this));
		button_addphoto = new JButton("Add Photo");
		button_addphoto.addActionListener(new Button_Clicked(this));
		
		scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setPreferredSize(new Dimension(590, 350));

		refreshPhotos();
		
	}

	/**
	 * Repopulates the photos in the JScrollPane
	 */
	protected void refreshPhotos(){
		panel_pics = new JPanel(new GridLayout(0,3, 10, 10));
		for (Photo photo : current_album.getPhotos()) {
			addPhotoToDisplay(photo);
		}
		scroller.setViewportView(panel_pics);
	}

	/**
	 * @param photo the photo to add
	 * Adds the photo to the panel
	 */
	private void addPhotoToDisplay(Photo photo){

		File photo_file = new File(photo.getFilename());

		Image img = null;
		try {
			img = ImageIO.read(photo_file).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);

		} catch (IOException e) {
			try {
				img = ImageIO.read(new File("assets/question.png")).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
			} catch (IOException e1) {

			}
		}
		assert img != null;
		System.out.println("loading picture " + photo.getFilename());
		JLabel picLabel = new JLabel(new ImageIcon(img));
		picLabel.setToolTipText(photo.getFilename());
		picLabel.addMouseListener(new PhotoDoubleClicked(this));
		picLabel.addMouseListener(new PhotoRightClickListener(this));
	

		Border basic_border = BorderFactory.createLineBorder(Color.black);
		TitledBorder border = BorderFactory.createTitledBorder(basic_border, photo.getCaption());
		border.setTitlePosition(TitledBorder.BELOW_BOTTOM);
		picLabel.setBorder(border);

		panel_pics.add(picLabel);
	}

	/**
	 * Layouts the main panel with its components 
	 */
	public void LayoutMainPanel()
	{
		panel_main.setLayout(gbl);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		
		panel_main.add(button_back, gbc);
		
		gbc.anchor = GridBagConstraints.NORTHEAST;
		
		panel_main.add(button_user, gbc);
		
		gbc.insets = new Insets(10,10,10,10);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = GridBagConstraints.REMAINDER;
		gbc.gridy = 1;
		
		panel_main.add(scroller, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		
		panel_main.add(button_addphoto, gbc);		
	}

	/**
	 * Occurs when a button is clicked
	 *
	 */
	class Button_Clicked implements ActionListener
	{
		OpenAlbum openalbum;
		
		public Button_Clicked(OpenAlbum openalbum)
		{
			this.openalbum = openalbum;
		}
	
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == openalbum.button_addphoto)
			{
				int returnVal = openalbum.fc.showOpenDialog(openalbum);
	
		        if (returnVal == JFileChooser.APPROVE_OPTION) 
		        {
		        	File file = openalbum.fc.getSelectedFile();
		        	System.out.println(file.getPath());
		        	PhotoControl.addPhoto(openalbum.current_user, file.getPath(), " ", openalbum.current_album.getName());
	
					openalbum.refreshPhotos();
		        }
		    }
		            
			if(e.getSource() == openalbum.button_back)
			{
				UserControl.saveUser(openalbum.current_user);
				openalbum.dispose();
				new NonAdmin(openalbum.current_user).setVisible(true);
			}
			if(e.getSource() == openalbum.button_user)
			{
				UserControl.saveUser(openalbum.current_user);
				openalbum.dispose();
				new Login().setVisible(true);
			}	
		}
	}


	/**
	 * Occurs when a photo is double clicked
	 *
	 */
	class PhotoDoubleClicked implements MouseListener
	{
	
		OpenAlbum openalbum;
	
		public PhotoDoubleClicked(OpenAlbum openalbum) {
			this.openalbum = openalbum;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			
			Integer photo_pos = AlbumControl.getPhotoPosition(openalbum.current_album, openalbum.current_user.getPhoto(((JLabel) e.getSource()).getToolTipText()));
			openalbum.current_photo = openalbum.current_album.getPhoto(photo_pos);
			
			if(e.getClickCount() == 2)
			{
				if((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
				{
					new DisplayPhoto(openalbum.current_user, openalbum.current_album, photo_pos);
					openalbum.dispose();
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
	class PhotoRightClickMenu extends JPopupMenu implements ActionListener 
	{

		private static final long serialVersionUID = 1L;
		OpenAlbum openalbum;

	    JMenuItem delete;
	    JMenuItem edit;
	    
	    public PhotoRightClickMenu(OpenAlbum openalbum){
	    	this.openalbum = openalbum;

	        delete = new JMenuItem("Delete Photo");
	        delete.addActionListener(this);
	        edit = new JMenuItem("Edit Photo");
	        edit.addActionListener(this);

	        add(delete);
	        add(edit);

	    }
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			System.out.println("source: " + e.getSource());

//			openalbum.current_photo = openalbum.current_album.getPhoto(e.getSource());

			if(e.getSource() == delete)

			{

				PhotoControl.removePhoto(openalbum.current_user, openalbum.current_photo.getFilename(), openalbum.current_album.getName());
				openalbum.refreshPhotos();
				//openalbum.RelistAlbums();	
				//openalbum.resetAlbumInfo();
			}			
			if(e.getSource() == edit)
			{
				new PhotoInfo(openalbum.current_user, openalbum.current_photo, openalbum.current_album.getName()).setVisible(true);
				//new RenameAlbum(oa, NonAdmin.clicked_album).setVisible(true);
				//oa.resetAlbumInfo();
				openalbum.dispose();
			}
				
			}
		}
	
	/**
	 * Shows the right click menu
	 *
	 */
	class PhotoRightClickListener extends MouseAdapter {
		
		OpenAlbum openalbum;
		
		public PhotoRightClickListener(OpenAlbum openalbum)
		{
			this.openalbum = openalbum;
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
	     * Occurs when a menu item is clicked and does appropriate action
	     */
	    private void doPop(MouseEvent e){

			JLabel label = (JLabel) e.getSource();
			String photoname = label.getToolTipText();

			openalbum.current_photo = openalbum.current_album.getPhoto(photoname);


	        PhotoRightClickMenu menu = new PhotoRightClickMenu(openalbum);
	        menu.show(e.getComponent(), e.getX(), e.getY());
	    }
	}
}


