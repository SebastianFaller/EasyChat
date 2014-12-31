package logic;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ServerLogic {

	private ServerSocket server;
	private HashMap clients;
	private DataInputStream inStream;
	private DataOutputStream outStream;

	public ServerLogic(int port) {
		try {
			server = new ServerSocket(port);
			clients = new HashMap<String, Socket>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// constructor

	public void acceptRoutine() throws IOException {
		while (true) {
			// Auf neuen Client warten
			Socket tempClient = server.accept();
			// TODO Unschön! sollte auch in anderen Thread
			new DataOutputStream(tempClient.getOutputStream())
					.writeUTF("<whoareyou/>");
			String tempName = null;
			while (tempName == null) {
				tempName = new DataInputStream(tempClient.getInputStream())
						.readUTF();
			}
			// zerlege string. zweiter teil ist name
			System.out.println("temp name 1: " + tempName);
			tempName = tempName.split(Pattern.quote("<iam/>"))[1];
			System.out.println("temp name 2: " + tempName);
			clients.put(tempName, tempClient);
			ProcessingThread t = new ProcessingThread(tempClient, clients);
			t.setDaemon(true);
			t.start();
		}

		// Thread processingThread = new Thread() {
		// @Override
		// public void run() {
		//
		// }
		// };

		// System.out.println("gay");
		// String received = inStream.readUTF();
		// System.out.println("blub");
		// received = received + " server erhalten ";
		// outStream.writeUTF(received);
		// outStream.close();
		// inStream.close();
		// client.close();
	}

	public static void main(String args[]) throws IOException {
		ServerLogic s = new ServerLogic(1025);
		System.out.println("server build");
		s.acceptRoutine();
	}

}