package logic;

import java.io.IOException;

import ui.ChatWindowInterface;

public class UpdateContactsThread extends Thread {

	private ChatWindowInterface window;
	private ClientLogic clientLogic;

	public UpdateContactsThread(ChatWindowInterface w, ClientLogic c) {
		window = w;
		clientLogic = c;
	}

	@Override
	public void run() {
		while (true) {
			if (window != null) {
				window.setVisible(true);
			}
			try {
				clientLogic.demandOnlineUsers();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
