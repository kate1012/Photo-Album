package cs213.photoAlbum.guiview;

import javax.swing.*;

import cs213.photoAlbum.control.UserControl;
import cs213.photoAlbum.model.UserInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Kate Sussman and Ben Green
 *
 */
public class Login extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JTextField userID_textfield, password_textfield;
	private JLabel userID_label, password_label;
	private JButton login_button;
	public JPanel panel, mainpanel;
	GridBagConstraints gbc = new GridBagConstraints();
	
	public Login()
	{		
		setSize(225, 125);
		setTitle("Login");
		setSize(225, 125);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		
		initialize();
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(userID_label, gbc);
		
		gbc.gridx = GridBagConstraints.RELATIVE;
		panel.add(userID_textfield, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;	
		panel.add(password_label, gbc);
		
		gbc.gridx = GridBagConstraints.RELATIVE;	
		panel.add(password_textfield, gbc);
		
		//gbc.gridx = gbc.anchor;
		gbc.gridx = 1;
		gbc.gridy = 3;	
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(login_button, gbc);
		
		mainpanel.add(panel);
		
		this.add(mainpanel);
		
	}
	

	/**
	 * Initializes fields
	 */
	public void initialize()
	{
		panel = new JPanel(new GridBagLayout());
		mainpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		userID_textfield = new JTextField(10);
		password_textfield = new JTextField(10);
		userID_label = new JLabel("User ID: ");
		password_label = new JLabel("Password: ");
		login_button = new JButton("Login");
		login_button.addActionListener(new loginClicked(this));
	}
	

	public static void main(String[] args) {
		JFrame login = new Login();
		login.setTitle("Login");
		login.setSize(225, 125);
		login.setDefaultCloseOperation(EXIT_ON_CLOSE);
		login.setLocationRelativeTo(null);
		login.setVisible(true);
		login.setResizable(false);

	}
	
	/**
	 * Occurs when the Login button is clicked 
	 *
	 */
	public class loginClicked implements ActionListener
	{
		Login login;
		
		public loginClicked(Login login)
		{
			this.login = login;
		}
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String user_id = userID_textfield.getText();
			String password = password_textfield.getText();

			if(!UserControl.userExists(user_id) && !user_id.equals("admin"))
			{
				JOptionPane.showMessageDialog(Login.this, "User does not exist", "Information", JOptionPane.INFORMATION_MESSAGE);
				//JOptionPane.
			} else if(user_id.equals("admin")) {
				dispose();
				new Admin().setVisible(true);
			} else {
				UserInterface current_user = UserControl.readUser(user_id);
				if (current_user.checkPassword(password)) {
					new NonAdmin(current_user).setVisible(true);
					dispose();
				}
			}


		}
		
	}

}
