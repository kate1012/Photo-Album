package cs213.photoAlbum.guiview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.control.UserControl;
import cs213.photoAlbum.model.UserInterface;

/**
 * @author Kate Sussman and Ben Green
 *
 */
public class Admin extends JFrame{

	private static final long serialVersionUID = 1L;
	JLabel label_prompt, label_userid, label_username, label_listofusers, label_password;
	JPanel panel_users, panel_new_users, mainpanel;
	JTextField userid_field;
	JTextField username_field;
	JTextField password_field;
	JButton button_create;
	JButton button_user;
	JList<String> list_of_users;
	DefaultListModel<String> model;
	JScrollPane listScroller;
	static String clicked_user;
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	ButtonClicked buttonClicked;

	public Admin()
	{
		buttonClicked = new ButtonClicked(this);
		
		setName("Admin ");
		setLocationRelativeTo(null);
		setSize(600,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(gbl);
		setResizable(false);
		
		initialize();
		
		layoutUsersPanel();
		layoutNewUsersPanel();
		
		/*layout panels on frame*/
		gbc.insets = new Insets(20,20,20,20);
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(panel_users, gbc);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 2;
		add(panel_new_users, gbc);
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.insets = new Insets(5,20,20,5);
		add(button_user, gbc);

	}
	
	
	/**
	 * Initializes fields 
	 */
	public void initialize()
	{
		mainpanel = new JPanel();
		panel_new_users = new JPanel();
		panel_users = new JPanel();
		button_user = new JButton("Logout User");
		button_user.addActionListener(buttonClicked);
		
		label_prompt = new JLabel("Create new user:");
		label_userid = new JLabel("User ID");
		label_username = new JLabel("Name");
		label_password = new JLabel("Password");
		label_listofusers = new JLabel("List of users:");
		
		userid_field = new JTextField(15);
		username_field = new JTextField(15);
		password_field = new JTextField(15);
		
		button_create = new JButton("Create User");
		button_create.addActionListener(buttonClicked);
		
		model = new DefaultListModel<String>();
		
		UserControl.listUsers(model);
		
		list_of_users = new JList<String>(model);
		list_of_users.setLayoutOrientation(JList.VERTICAL);
		list_of_users.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_of_users.setVisibleRowCount(-1);
		list_of_users.addListSelectionListener(new ClickedUser(this));
		list_of_users.addMouseListener(new RightClickListener(this));

		listScroller = new JScrollPane(list_of_users);
		listScroller.setPreferredSize(new Dimension(300, 300));
		
	}
	
	/**
	 * Sets layout for the new users panel
	 */
	public void layoutNewUsersPanel()
	{
		panel_new_users.setLayout(gbl);
		gbc.insets = new Insets(5,5,5,5);
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel_new_users.add(label_prompt, gbc);
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		panel_new_users.add(label_userid, gbc);
		gbc.gridy = 4;
		panel_new_users.add(userid_field, gbc);
		gbc.gridy = 5;
		panel_new_users.add(label_username, gbc);
		gbc.gridy = 6;
		panel_new_users.add(username_field, gbc);
		gbc.gridy = 7;
		panel_new_users.add(label_password, gbc);
		gbc.gridy = 8;
		panel_new_users.add(password_field, gbc);
		gbc.gridy = 9;
		gbc.anchor = GridBagConstraints.CENTER;
		panel_new_users.add(button_create, gbc);		
	}
	
	/**
	 * Sets layout for the users panel
	 */
	public void layoutUsersPanel()
	{
		panel_users.setLayout(gbl);
		panel_users.setBorder(BorderFactory.createLineBorder(Color.black));
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 1;
		panel_users.add(label_listofusers, gbc);
		gbc.gridy = 3;
		panel_users.add(listScroller, gbc);
	}

	/**
	 * Displays list of users in the JScrollPane
	 */
	public void relistUsers()
	{
		model.clear();
		UserControl.listUsers(model);
	}
}

/**
 * Occurs when a button is clicked
 */
class ButtonClicked implements ActionListener
{
	Admin admin;
	
	public ButtonClicked(Admin admin)
	{
		this.admin = admin;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		/*logout*/
		if(e.getSource() == admin.button_user)
		{
			admin.dispose();
			new Login().setVisible(true);
		}
		if(e.getSource() == admin.button_create)
		{
			UserControl.addUser(admin.userid_field.getText(), admin.username_field.getText(), admin.password_field.getText());
			admin.relistUsers();
		}		
	}	
}

/**
 * Occurs when a user is clicked in the JScrollPane
 */
class ClickedUser implements ListSelectionListener
{
	Admin admin;
	
	public ClickedUser(Admin admin)
	{
		this.admin = admin;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		Admin.clicked_user = admin.list_of_users.getSelectedValue();	
	}
}

/**
 * Creates the right click menu and adds components
 */
class RightClickMenu extends JPopupMenu implements ActionListener
{
	private static final long serialVersionUID = 1L;
	JMenuItem delete;
	Admin admin;
	
	public RightClickMenu(Admin admin)
	{
		this.admin = admin;
		delete = new JMenuItem("Delete User");
		delete.addActionListener(this);
		add(delete);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		/*delete user and re-populate the JScrollPane*/
		if(e.getSource() == delete)
		{	
			UserControl.deleteUser(Admin.clicked_user);
			admin.relistUsers();
		}	
	}
}

/**
 * Shows the right click menu
 */
class RightClickListener extends MouseAdapter 
{
	Admin admin;
	
	public RightClickListener(Admin admin)
	{
		this.admin = admin;
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
     * @param e the event 
     * Creates the right click menu and shows the menu 
     */
    private void doPop(MouseEvent e){
    	RightClickMenu menu = new RightClickMenu(admin);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
