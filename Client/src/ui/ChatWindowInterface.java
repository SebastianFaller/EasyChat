package ui;

import java.util.HashMap;

public interface ChatWindowInterface extends ClientUiInterface{
	
	public String getCurrentChatPartner();
	
	public void displayMessage(String message, String sender);
	
	public void updateOnlineUsers(HashMap<String, byte[]> nameImgMap);

	public void setDisplayedText(String text);
	
	public String getDisplayedText();
	
	public void setVisible(Boolean b);
}