package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import logic.ClientLogic;

public class LoginWindow extends UserInputDialog implements
		LoginWindowInterface {

	private JButton registrateButton;

	public LoginWindow(ClientLogic client) {
		super(client);
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

		this.pack();
		setVisible(true);
	}

	public void submit() {
		String user = userNameField.getText();
		char[] password = passwordField.getPassword();
		System.out.println("window password: " + password.toString());
		clientLogic.loginWithValues(password, user);
		for (int i = 0; i < password.length; i++) {
			password[0] = 0;
		}
		this.pack();
	}

	public void loginFailed() {
		System.out.println("failed");
		failedLabel.setVisible(true);
	}
}
