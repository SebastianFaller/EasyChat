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

public class LoginWindow extends UserInputDialog{
	
	private JButton registrateButton;
	
	public LoginWindow(ClientLogic client){
		super(client);
		
//		submitButton = new JButton("Login");
//		cancelButton = new JButton("Abbrechen");
		registrateButton = new JButton("Registrieren");
		
		failedLabel.setText("Name oder Password falsch.");
		
		buttonPanel.add(registrateButton);
		submitButton.setText("Login");

		submitButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				submit();
			}
		});

		
		registrateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RegistrationWindow r = new RegistrationWindow(clientLogic);
				LoginWindow.this.clientLogic.setRegWindow(r);
				LoginWindow.this.setVisible(false);
				
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
		
		this.pack();
		setVisible(true);
	}
	
	public void submit(){
		String user = userNameField.getText();
		char[] password = passwordField.getPassword();
		System.out.println("window password: "+password.toString());
		clientLogic.loginWithValues(password, user);
		for(int i = 0; i < password.length; i++){
			password[0] = 0;
		}
		this.pack();
	}
	
	
	
	public void loginFailed(){
		System.out.println("failed");
		failedLabel.setVisible(true);
	}
	
//	public ClientLogic getClientLogic() {
//		return clientLogic;
//	}

}
