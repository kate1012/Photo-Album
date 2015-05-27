package cs213.photoAlbum.guiview;

//import com.sun.xml.internal.bind.v2.TODO;
import cs213.photoAlbum.control.AlbumControl;
import cs213.photoAlbum.control.PhotoControl;
import cs213.photoAlbum.control.UserControl;
import cs213.photoAlbum.model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * @author Kate Sussman and Ben Green
 *
 */
public class DisplayPhoto extends JFrame{
	//private LoginInfoPanel logininfopanel;
	protected JPanel panel_main, panel_photo, panel_photo_info;
	protected JButton button_back, button_leftarrow, button_rightarrow, button_logout;
	protected JLabel label_caption, label_date, label_photoname, caption, date, name, tag;
	private JLabel[] tags;

	protected Photo current_photo;
	protected AlbumInterface album;
	protected static UserInterface current_user;
	protected ArrayList<Photo> photos;
	protected Integer pos;

	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	
	public DisplayPhoto(UserInterface current_user, AlbumInterface album, Integer pos)
	{

		this.current_user = current_user;
		this.album = album;
		this.pos = pos;
		
		this.setTitle("Photo Information");
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    /* (non-Javadoc)
		     * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
		     */
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	    		System.out.println("saving stuff");
	        	UserControl.saveUser(DisplayPhoto.current_user);
	            System.exit(0);
		    
		        }
		    });

		this.photos = new ArrayList<>();
		for (Photo photo : album.getPhotos()) {
			photos.add(photo);
		}

		initialize(pos);
		LayoutMainPanel();
		LayoutPhotoInfoPanel();
		setupPhoto();
		
		add(panel_main);
	}

	/**
	 * Sets up the photo thumbnails 
	 */
	protected void setupPhoto() {
		File photo_file = new File(current_photo.getFilename());

		System.out.println("setting up photo " + current_photo.getFilename());

		Image img = null;
		try {
			img = ImageIO.read(photo_file);

			BufferedImage icon = ImageIO.read(photo_file);
			double height = (double)icon.getHeight();
			double width = (double)icon.getWidth();
			double ratio = width/height;
			Double new_height = (350 / ratio);
			img = img.getScaledInstance(350, (int)Math.round(new_height), BufferedImage.SCALE_SMOOTH);

		} catch (IOException e) {
			try {
				img = ImageIO.read(new File("assets/question.png")).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
			} catch (IOException e1) {
				System.out.println("where'd the question mark go :(");
			}
		}
		assert img != null;
		JLabel picLabel = new JLabel(new ImageIcon(img));

		Border border = BorderFactory.createLineBorder(Color.black);
		picLabel.setBorder(border);

		panel_photo.add(picLabel);
	}

	/**
	 * @param pos the position of the photo
	 * Initializes the photo and fields 
	 */
	public void initialize(Integer pos){

		System.out.println("displaying photo " + pos);

		this.current_photo = photos.get(pos);

		panel_photo = new JPanel();
		panel_photo.setSize(new Dimension(400, 400));
		panel_photo_info = new JPanel();
		panel_photo.setSize(new Dimension(400, 200));
		
		name = new JLabel("Name:");
		Font font = name.getFont();
		Font boldFont = new Font(font.getFontName(), font.BOLD, font.getSize());
		name.setFont(boldFont);
		tag = new JLabel("Tags:");
		tag.setFont(boldFont);
		date = new JLabel("Date:");
		date.setFont(boldFont);
		caption = new JLabel("Caption:");
		caption.setFont(boldFont);
		


		label_caption = new JLabel(current_photo.getCaption());
		label_date = new JLabel(PhotoControl.date_format.format(current_photo.getTimestamp().getTime()));
		label_photoname = new JLabel(current_photo.getFilename());

		ArrayList<String> tags_list = new ArrayList<>();

		for (String next : current_photo.getPeople()) {
			tags_list.add("Person" + " : " + next);
		}


		Iterator<Entry<String, String>> iter2 = current_photo.getTags();
		while(iter2.hasNext())
		{
			Entry<String, String> next = iter2.next();
			tags_list.add(next.getKey() + " : " + next.getValue());
		}
		
		tags = new JLabel[tags_list.size()];
		
		for(int i=0; i<tags_list.size(); i++)
		{
			tags[i] = new JLabel(tags_list.get(i));
		}

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		
	}
	
	/**
	 * Layouts the main panel
	 */
	public void LayoutMainPanel()
	{
		button_back = new JButton("Back");
		button_back.addActionListener(new NavigatorHandler(this));
		button_leftarrow = new JButton("<--");
		button_leftarrow.addActionListener(new NavigatorHandler(this));
		button_rightarrow = new JButton("-->");
		button_rightarrow.addActionListener(new NavigatorHandler(this));
		button_logout = new JButton("Logout User");
		button_logout.addActionListener(new NavigatorHandler(this));


		panel_main = new JPanel();
		panel_main.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel_main.setLayout(gbl);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		
		panel_main.add(button_back, gbc);
		
		gbc.gridx = 3;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		
		panel_main.add(button_logout, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		
		panel_main.add(panel_photo, gbc);
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		
		panel_main.add(panel_photo_info, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		
		panel_main.add(button_leftarrow, gbc);
		
		gbc.gridx = 2;
		
		panel_main.add(button_rightarrow, gbc);
		
	}
	
	/**
	 * Layouts the Photo info panel
	 */
	public void LayoutPhotoInfoPanel()
	{
		panel_photo_info.setLayout(gbl);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		
		panel_photo_info.add(name, gbc);
		
		gbc.gridy = 1;
		
		panel_photo_info.add(label_photoname, gbc);
		
		gbc.gridy = 2;
		
		panel_photo_info.add(caption, gbc);
		
		gbc.gridy = 3;
		
		panel_photo_info.add(label_caption, gbc);
		
		gbc.gridy = 4;
		
		panel_photo_info.add(date, gbc);
		
		gbc.gridy = 5;
		
		panel_photo_info.add(label_date, gbc);
		
		gbc.gridy = 6;
		
		panel_photo_info.add(tag, gbc);
		
		gbc.gridy = 7;

		for (JLabel tag : tags) {
			panel_photo_info.add(tag, gbc);
			gbc.gridy++;
		}
	}

}

/**
 * Occurs when button clicked
 *
 */
class NavigatorHandler implements ActionListener{

	DisplayPhoto dp;

	public NavigatorHandler(DisplayPhoto dp)
	{
		this.dp = dp;
	}


	//TODO change jlabels instead of stupid shit
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == dp.button_leftarrow){

			if (dp.album.hasPhotoAt(dp.pos-1)) {

				System.out.println("iterating to photo " + (dp.pos-1));

				new DisplayPhoto(dp.current_user, dp.album, dp.pos-1).setVisible(true);
				dp.dispose();
			}
		}

		if(e.getSource() == dp.button_rightarrow){

			if (dp.album.hasPhotoAt(dp.pos+1)) {

				System.out.println("iterating to photo " + (dp.pos+1));

				new DisplayPhoto(dp.current_user, dp.album, dp.pos+1).setVisible(true);
				dp.dispose();
			}
		}

		if(e.getSource() == dp.button_logout)
		{
			UserControl.saveUser(dp.current_user);
			new Login().setVisible(true);
			dp.dispose();
		}

		if (e.getSource() == dp.button_back) {
			new OpenAlbum(dp.current_user, dp.album).setVisible(true);
			dp.dispose();
		}

	}
}
