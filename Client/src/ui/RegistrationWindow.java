package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logic.ClientLogic;

public class RegistrationWindow extends UserInputDialog {
	
	
	private JPasswordField secondPWField;

	public RegistrationWindow(ClientLogic clientLogic){
		super(clientLogic);
		secondPWField = new JPasswordField();
		secondPWField = new JPasswordField("Passwort", 20);
		secondPWField.setForeground(Color.GRAY);
		secondPWField.setHorizontalAlignment(JTextField.CENTER);
		secondPWField.setEchoChar((char) 0);
		secondPWField.setText("Wiederhole Passwort");
		c.gridx = 1;
		c.gridy = 2;
		inputPanel.add(secondPWField, c);
		submitButton.setText("Registrieren");
		
		submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				submit();
				
			}
		});
		
		PwdFieldListener l = new PwdFieldListener(this, secondPWField);
		secondPWField.addMouseListener(l);
		secondPWField.addKeyListener(l);
		
		
		
		this.pack();
		this.setVisible(true);
	}
	
	@Override
	public void submit(){
		String name = userNameField.getText();
		char[] password1 = passwordField.getPassword();
		char[] password2 = secondPWField.getPassword();
		boolean b = RegistrationWindow.this.clientLogic.registrateAtServer(name, password1, password2);
		if(!b) {
			failedLabel.setText("Passwort nicht identisch");
			failedLabel.setVisible(true);
			RegistrationWindow.this.pack();
		} else {
			this.closeFrame();
		}
		for(int i = 0; i<password1.length ; i++) password1[i] = 0;
		for(int i = 0; i<password1.length ; i++) password2[i] = 0;
	}
}