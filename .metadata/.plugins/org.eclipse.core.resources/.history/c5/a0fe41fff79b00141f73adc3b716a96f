package logic;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JList;

import ui.ChatWindow;

public class UpdateContactsThread extends Thread{
	
	private ChatWindow window;
	private ClientLogic clientLogic;
	private String[] list;
	
	
	public UpdateContactsThread(ChatWindow w, ClientLogic c){
		window = w;
		clientLogic = c;
		list = null;
		
	}
	
	@Override
	public void run(){
		while(true){
			if(window != null){
			window.getUserList().setVisible(true);
			}
//			clientLogic.demandUsers = false;
		
//		clientLogic.demandUsers = true;
		try {
			clientLogic.demandOnlineUsers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		String[] usersArray = 
//		JList<String> userList = window.getUserList();
//		userList.setListData(users);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}

}
