package ui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;


public class NameFieldListener implements MouseListener, KeyListener{
	
	private UserInputDialog userInputDialog;
	protected JTextField textField;
	private boolean firstKeyPressed;
	
	public NameFieldListener(UserInputDialog userInputDialog, JTextField textField){
		this.userInputDialog = userInputDialog;
		this.textField = textField;
		firstKeyPressed = false;
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		setTextDefault();	
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!firstKeyPressed){
			firstKeyPressed = true;
			setTextDefault();
		}
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			userInputDialog.submit();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void setTextDefault(){
		textField.setText("");
		textField.setForeground(Color.BLACK);
		textField.setHorizontalAlignment(JTextField.LEFT);
	}

}
