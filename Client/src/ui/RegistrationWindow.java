package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logic.ClientLogic;

public class RegistrationWindow extends UserInputDialog implements
		RegistrationWindowInterface {

	private JPasswordField secondPWField;
	private JTextField filePathField;
	private JPanel filePathPanel;
	private JButton browse;
	private JFileChooser chooser;
	private JLabel imageLabel;

	public RegistrationWindow(ClientLogic clientLogic) {
		super(clientLogic);
		secondPWField = new JPasswordField();
		secondPWField = new JPasswordField("Passwort", 20);
		secondPWField.setForeground(Color.GRAY);
		secondPWField.setHorizontalAlignment(JTextField.CENTER);
		secondPWField.setEchoChar((char) 0);
		secondPWField.setText("Wiederhole Passwort");
		filePathField = new JTextField();
		filePathPanel = new JPanel();
		filePathPanel.setLayout(new BoxLayout(filePathPanel, BoxLayout.X_AXIS));
		filePathPanel.add(filePathField);
		browse = new JButton("Durchsuchen");
		filePathPanel.add(browse);
		chooser = new JFileChooser();
		imageLabel = new JLabel("Profilbild:");

		c.gridx = 1;
		c.gridy = 2;
		inputPanel.add(secondPWField, c);

		c.gridx = 1;
		c.gridy = 3;
		inputPanel.add(filePathPanel, c);

		c.gridx = 0;
		c.gridy = 3;
		inputPanel.add(imageLabel, c);
		submitButton.setText("Registrieren");

		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				submit();

			}
		});

		browse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnState = chooser
						.showOpenDialog(RegistrationWindow.this);
				if (returnState == JFileChooser.APPROVE_OPTION) {
					filePathField.setText(chooser.getSelectedFile().getPath());
				}

			}
		});

		PwdFieldListener l = new PwdFieldListener(this, secondPWField);
		secondPWField.addMouseListener(l);
		secondPWField.addKeyListener(l);

		this.pack();
		this.setVisible(true);
	}

	@Override
	public void submit() {
		String name = userNameField.getText();
		char[] password1 = passwordField.getPassword();
		char[] password2 = secondPWField.getPassword();
		String filePath = filePathField.getText();
		System.out.println("submitting in regist");
		int b = RegistrationWindow.this.clientLogic.registrateAtServer(name,
				password1, password2, filePath);
		switch (b) {
		case 0:
			break;
		case 1:
			failedLabel.setText("Pfad falsch");
			failedLabel.setVisible(true);
			RegistrationWindow.this.pack();
			break;
		case 2:
			failedLabel.setText("Password nicht identisch");
			failedLabel.setVisible(true);
			RegistrationWindow.this.pack();
			break;

		}
		for (int i = 0; i < password1.length; i++)
			password1[i] = 0;
		for (int i = 0; i < password2.length; i++)
			password2[i] = 0;
	}

	public void showNameTaken() {
		failedLabel.setText("Name bereits vergeben");
		failedLabel.setVisible(true);
		this.pack();
		this.setVisible(true);
	}
}