package ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PwdFieldListener extends NameFieldListener{

	
	
	public PwdFieldListener(LoginWindow w, JTextField textField) {
		super(w, textField);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		((JPasswordField) this.textField).setEchoChar('•');
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		super.mousePressed(arg0);
		((JPasswordField) this.textField).setEchoChar('•');
	}

}

