package logic;

import javax.swing.JList;

import ui.ChatWindow;

public class UpdateContactsThread extends Thread{
	
	private ChatWindow window;
	private ClientLogic clientLogic;
	
	public UpdateContactsThread(ChatWindow w, ClientLogic c){
		window = w;
		clientLogic = c;
	}
	
	@Override
	public void run(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] users = clientLogic.demandOnlineUsers();
		JList<String> userList = window.getUserList();
		userList.setListData(users);
	}

}
