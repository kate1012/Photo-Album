package cs213.photoAlbum.guiview;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import cs213.photoAlbum.control.AlbumControl;
import cs213.photoAlbum.control.PhotoControl;
import cs213.photoAlbum.control.UserControl;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Kate Sussman and Ben Green
 *
 */
public class SearchResults extends JFrame {

	private static final long serialVersionUID = 1L;
	protected JButton button_back, button_createalbum, button_logout;
	private JLabel label_search_results;
	private JPanel panel_main, panel_pics;
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	private JScrollPane scroller;
	protected ArrayList<Photo> results;
	protected static User current_user;
	
	public SearchResults(User current_user, ArrayList<Photo> results)
	{

		this.current_user = current_user;
		this.results = results;
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    /* (non-Javadoc)
		     * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
		     */
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	    		System.out.println("saving stuff");
	        	UserControl.saveUser(SearchResults.current_user);
	            System.exit(0);
		    
		        }
		    });

		initialize();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(700, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Search Results");
		
		panel_main.setLayout(gbl);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		
		panel_main.add(button_back, gbc);
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 1;
		
		panel_main.add(label_search_results, gbc);
		
		gbc.gridx = 2;
		gbc.anchor = GridBagConstraints.NORTHEAST;
		
		panel_main.add(button_logout, gbc);
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		scroller.setViewportView(panel_pics);
		
		panel_main.add(scroller, gbc);
		
		gbc.gridy = 2;
		gbc.gridx = 0;
		
		panel_main.add(button_createalbum, gbc);
		
		this.add(panel_main);
		
		
	}
	
	/**
	 * Initializes the fields 
	 */
	public void initialize()
	{
		panel_main = new JPanel();
		panel_pics = new JPanel(new GridLayout(0,3, 10, 10));
		
		button_back = new JButton("Back");
		button_createalbum = new JButton("Create Album");

		button_back.addActionListener(new Button_Clicked(this));
		button_createalbum.addActionListener(new SearchResultsListener(this));
		
		button_logout = new JButton("Logout User");
		button_logout.addActionListener(new SearchResultsListener(this));
		
		label_search_results = new JLabel("Search results for: thing searched");
		
		scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setPreferredSize(new Dimension(600,370));

		for (Photo photo : results) {
			addPhotoToDisplay(photo);
		}
		
	}

	/**
	 * @param photo the photo to add to the JScrollPane
	 * Adds the photos from search results to the JScrollPane
	 */
	private void addPhotoToDisplay(Photo photo){

		File photo_file = new File(photo.getFilename());

		Image img = null;
		try {
			img = ImageIO.read(photo_file).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);

		} catch (IOException e) {
			try {
				img = ImageIO.read(new File("data/question.png")).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
			} catch (IOException e1) {
		
			}
		}
		assert img != null;
		JLabel picLabel = new JLabel(new ImageIcon(img));
		picLabel.setToolTipText(photo.getFilename());

		Border basic_border = BorderFactory.createLineBorder(Color.black);
		TitledBorder border = BorderFactory.createTitledBorder(basic_border, photo.getFilename());
		border.setTitlePosition(TitledBorder.BELOW_BOTTOM);
		picLabel.setBorder(border);

		panel_pics.add(picLabel);
	}

	/**
	 * Creates a new album from search results and displays it
	 *
	 */
	private class SearchResultsListener implements ActionListener {

		private SearchResults sr;

		public SearchResultsListener(SearchResults sr) {
			this.sr = sr;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == sr.button_createalbum && !sr.results.isEmpty()) {

				Integer iter = 0;
				while(current_user.hasAlbum("Search Results " + iter)) {
					iter++;
				}

				AlbumControl.createAlbum(current_user, "Search Results " + iter);

				for (Photo photo : sr.results) {
					PhotoControl.addPhoto(sr.current_user, photo.getFilename(), "", "Search Results " + iter);
				}

				new OpenAlbum(sr.current_user, sr.current_user.getAlbum("Search Results " + iter)).setVisible(true);
				sr.dispose();


			}
		}
	}

	/**
	 * Occurs when a button is clicked
	 *
	 */
	private class Button_Clicked implements ActionListener
	{
		SearchResults sr;

		public Button_Clicked(SearchResults sr)
		{
			this.sr = sr;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if(e.getSource() == sr.button_back)
			{
				UserControl.saveUser(sr.current_user);
				new NonAdmin(sr.current_user).setVisible(true);
				sr.dispose();
			}

			if(e.getSource() == sr.button_logout)
			{
				UserControl.saveUser(current_user);
				sr.dispose();
				new Login().setVisible(true);
			}
		}

	}
}
