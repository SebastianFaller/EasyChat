package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logic.ClientLogic;

public class LoginWindow extends JFrame{
	
	private ClientLogic clientLogic;
	private int port;
	
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JLabel nameLabel;
	private JLabel pwdLabel;
//	private JPanel namePanel;
//	private JPanel pwdPanel;
	private JPanel inputPanel;
	private JButton loginButton;
	private JLabel failedLabel;
	
	public LoginWindow(ClientLogic client){
		
		clientLogic = client;
//		setSize(400, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container p = this.getContentPane();
		p.setLayout(new BoxLayout(p ,BoxLayout.Y_AXIS ));
		
		userNameField = new JTextField("Benutzername", 20);
		userNameField.setForeground(Color.GRAY);
		userNameField.setHorizontalAlignment(JTextField.CENTER);
//		userNameField.setPreferredSize(new Dimension(20, this.getHeight()));
//		userNameField.setSize(160, 25);
		passwordField = new JPasswordField("Passwort", 20);
		passwordField.setForeground(Color.GRAY);
		passwordField.setHorizontalAlignment(JTextField.CENTER);
		passwordField.setEchoChar((char) 0);
//		passwordField.setPreferredSize(new Dimension(20, this.getHeight()));
//		passwordField.setSize(160, 25);
//		passwordField.setPreferredSize(new Dimension(160, 25));
		
		nameLabel = new JLabel("Benutzer:");
		pwdLabel = new JLabel("Passwort:");
//		namePanel = new JPanel();
//		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
//		pwdPanel = new JPanel();
//		pwdPanel.setLayout(new BoxLayout(pwdPanel, BoxLayout.X_AXIS));
		inputPanel = new JPanel();
//		inputPanel.setLayout(new BorderLayout());
		loginButton = new JButton("Login");
		
		failedLabel = new JLabel("Name oder Password falsch.");
		failedLabel.setForeground(Color.RED);
		failedLabel.setVisible(false);
		
		GridBagLayout layout = new GridBagLayout();
		inputPanel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		int padding = 5;
		Insets insets = new Insets(padding, padding, padding, padding);
		c .insets = insets;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		
		inputPanel.add(nameLabel,c);
		
		c.gridx = 1;
		c.gridy = 0;
		
		inputPanel.add(userNameField, c);
		
		c.gridx = 0;
		c.gridy = 1;
		
		inputPanel.add(pwdLabel, c);
		
		c.gridx = 1;
		c.gridy = 1;
		
		inputPanel.add(passwordField, c);
		
		
		NameFieldListener l = new NameFieldListener(this, userNameField);
		userNameField.addMouseListener(l);
		userNameField.addKeyListener(l);
		
		PwdFieldListener pwdl = new PwdFieldListener(this, passwordField);
		passwordField.addMouseListener(pwdl);
		passwordField.addKeyListener(pwdl);

		
		loginButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
		
//		namePanel.add(nameLabel);
//		namePanel.add(userNameField);
//		
//		pwdPanel.add(pwdLabel);
//		pwdPanel.add(passwordField);
//		
//		
//		
//		inputPanel.add(namePanel, BorderLayout.WEST);
//		inputPanel.add(pwdPanel, BorderLayout.CENTER);
////		inputPanel.setPreferredSize(new Dimension(350, 600));
//		
//		loginButton.setAlignmentX(CENTER_ALIGNMENT);
//		
//		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		p.add(Box.createRigidArea(new Dimension(0, 5)));
		p.add(inputPanel);
		p.add(failedLabel);
		p.add(Box.createRigidArea(new Dimension(0, 15)));
		p.add(loginButton);
		loginButton.setAlignmentX(CENTER_ALIGNMENT);
		p.add(Box.createRigidArea(new Dimension(0, 5)));
		this.pack();
		setVisible(true);
		
	}
	
	public void login(){
		String user = userNameField.getText();
		char[] password = passwordField.getPassword();
		System.out.println("window password: "+password.toString());
		clientLogic.loginWithValues(password, user);
		for(int i = 0; i < password.length; i++){
			password[0] = 0;
		}
	}
	
	public void closeFrame(){
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	public void loginFailed(){
		System.out.println("failed");
		failedLabel.setVisible(true);
	}
	
	public JTextField getUserNameField(){
		return userNameField;
	}
	
	public JPasswordField getPasswordField(){
		return passwordField;
	}

//	public ClientLogic getClientLogic() {
//		return clientLogic;
//	}

}
