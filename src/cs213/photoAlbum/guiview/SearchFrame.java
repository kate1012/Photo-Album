package cs213.photoAlbum.guiview;

import cs213.photoAlbum.control.PhotoControl;
import cs213.photoAlbum.control.UserControl;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.simpleview.CmdView;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author Kate Sussman and Ben Green
 *
 */
public class SearchFrame extends JFrame {
	private JLabel label_searchby, label_date, label_tag;
	protected JTextField textfield_tag;
	protected JButton button_search, button_cancel;
	private GridBagConstraints gbc;
	protected JSpinner spinner_from, spinner_to;
	protected static User current_user;
	
	public SearchFrame(User current_user)
	{
		this.current_user = current_user;

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(300, 300);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setTitle("Search");
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    /* (non-Javadoc)
		     * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
		     */
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	    		System.out.println("saving stuff");
	        	UserControl.saveUser(SearchFrame.current_user);
	            System.exit(0);
		    
		        }
		    });
		
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);

		try {
			initialize();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		//gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		
		add(label_searchby, gbc);
		
		gbc.gridy = 1;
		
		add(label_date, gbc);
		
		//gbc.insets = new Insets(1,1,1,1);
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		
		add(spinner_from, gbc);
		
		gbc.gridx = 1;
		
		add(spinner_to, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		
		add(label_tag, gbc);
		
		gbc.gridy = 4;
		
		add(textfield_tag, gbc);
		gbc.insets = new Insets(10,10,10,10);
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		
		add(button_search, gbc);
		
		gbc.gridx = 1;
		
		add(button_cancel, gbc);
	
		
	}
	
	/**
	 * @throws ParseException
	 * Initializes the fields 
	 */
	public void initialize() throws ParseException {
		label_searchby = new JLabel("Search by:");
		label_date = new JLabel("Date:");
		label_tag = new JLabel("Tag:");
		
		Date todaysDate = new Date();
		
		// Create a date spinner & set default to today, no minimum, or max
		// Increment the days on button presses
		// Can also increment YEAR, MONTH, or DAY_OF_MONTH
		
		spinner_from = new JSpinner(new SpinnerDateModel(todaysDate, null, null,
		        Calendar.DAY_OF_MONTH));
		
		// DateEditor is an editor that handles displaying & editing the dates
		
		JSpinner.DateEditor dateEditor_from = new JSpinner.DateEditor(spinner_from, "MM/dd/yy");
		
		spinner_from.setEditor(dateEditor_from);

		spinner_to = new JSpinner(new SpinnerDateModel(todaysDate, null, null,
		        Calendar.DAY_OF_MONTH));
		
		// DateEditor is an editor that handles displaying & editing the dates
		
		JSpinner.DateEditor dateEditor_to = new JSpinner.DateEditor(spinner_to, "MM/dd/yy");
		spinner_to.setEditor(dateEditor_to);
		

		textfield_tag = new JTextField(15);
		
		button_search = new JButton("Search");
		button_search.addActionListener(new SearchFrameListener(this));
		button_cancel = new JButton ("Cancel");
		button_cancel.addActionListener(new SearchFrameListener(this));
	}


	/**
	 * Occurs when a button is clicked 
	 *
	 */
	private class SearchFrameListener implements ActionListener {

		private SearchFrame sf;

		public SearchFrameListener(SearchFrame searchFrame) {
			this.sf = searchFrame;
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == sf.button_search) {

				ArrayList<Photo> results = new ArrayList<>();
				Date from = (Date)sf.spinner_from.getValue();
				Date to = (Date)sf.spinner_to.getValue();
				sf.textfield_tag.setText(sf.textfield_tag.getText().trim());

				if (!from.equals(to) && !sf.textfield_tag.getText().equals("")) {

					ArrayList<String> parsed_input = CmdView.parseInput(sf.textfield_tag.getText());
					ArrayList<Photo> filtered = PhotoControl.getPhotosByTag(sf.current_user, parsed_input);
					results = PhotoControl.getPhotosByDate(sf.current_user, filtered, from, to);

				} else if (!from.equals(to)){
					results = PhotoControl.getPhotosByDate(sf.current_user, null, from, to);
				} else if (!sf.textfield_tag.getText().equals("")) {
					ArrayList<String> parsed_input = CmdView.parseInput(sf.textfield_tag.getText());
					results = PhotoControl.getPhotosByTag(current_user, parsed_input);
				}


				if (!results.isEmpty()) {
					new SearchResults(sf.current_user, results).setVisible(true);
					sf.dispose();
				}
			}

			if (e.getSource() == sf.button_cancel) {
				new NonAdmin(sf.current_user).setVisible(true);
				sf.dispose();
			}

		}
	}
}
