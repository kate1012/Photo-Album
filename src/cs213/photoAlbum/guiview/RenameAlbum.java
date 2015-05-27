package cs213.photoAlbum.guiview;

import javax.swing.*;

import cs213.photoAlbum.control.AlbumControl;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displayed when 'create new album' or 'rename album' is clicked
 * @author Kate Sussman and Ben Green
 *
 */
public class RenameAlbum extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JLabel label_nameofalbum;
	JTextField textfield_albumname;
	JButton button_create, button_cancel;
	private JPanel panel_main;
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	NonAdmin nonadmin;
	String clicked_album;
	
	public RenameAlbum(NonAdmin nonadmin, String clicked_album)
	{
		this.nonadmin = nonadmin;
		this.clicked_album = clicked_album;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(300, 130);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setTitle("Rename Album");
		
		initialize();
		
		panel_main.setLayout(gbl);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		panel_main.add(label_nameofalbum, gbc);
		gbc.gridx = 1;
		
		panel_main.add(textfield_albumname, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		
		panel_main.add(button_create, gbc);
		gbc.gridx = 1;
		
		panel_main.add(button_cancel, gbc);
		
		
		this.add(panel_main);
		
		
	}
	
	/**
	 * Initializes the fields
	 */
	public void initialize()
	{
		panel_main = new JPanel();
		label_nameofalbum = new JLabel("Name of album:");
		textfield_albumname = new JTextField(10);
		textfield_albumname.setText(NonAdmin.clicked_album);
		button_create = new JButton("Rename");
		button_create.addActionListener(new ClickedButton(this, this.nonadmin));
		button_cancel = new JButton("Cancel");
		button_cancel.addActionListener(new ClickedButton(this, this.nonadmin));
	}

}

/**
 * Occurs when a button is clicked 
 *
 */
class ClickedButton implements ActionListener
{
	RenameAlbum ra;
	NonAdmin na;
	//String clicked_album;
	
	public ClickedButton(RenameAlbum ra, NonAdmin na)
	{
		this.ra = ra;
		this.na = na;
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		/*renames the album*/
		if(e.getSource() == ra.button_create)
		{
			AlbumControl.renameAlbum(na.current_user, ra.clicked_album, ra.textfield_albumname.getText());
			na.RelistAlbums();
			ra.dispose();
		}
		if(e.getSource() == ra.button_cancel)
		{
			ra.dispose();
		}
	}	
}
