package logic;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ProcessingThread extends Thread {

	private Socket clientSocket;
	private DataInputStream inStream;
	private DataOutputStream outStream;
	private HashMap<String, Socket> clients;
	public static int number = 0;

	public ProcessingThread(Socket clientSocket, HashMap clients)
			throws IOException {

		this.clientSocket = clientSocket;
		this.clients = clients;
		inStream = new DataInputStream(clientSocket.getInputStream());
		outStream = new DataOutputStream(clientSocket.getOutputStream());
		number++;
	}

	@Override
	public void run() {
		// while(true){
		try {
			while (true) {
				String received = inStream.readUTF();
				System.out.println("angekommen: " + received);
				String[] segs = received.split(Pattern.quote("<adressee/>"));
				System.out.println(segs[0]);
					
				try{
				Socket adressee = clients.get(segs[0]);
				DataOutputStream dOut = new DataOutputStream(
						adressee.getOutputStream());
				System.out.println("outstream build");
				dOut.writeUTF(segs[1]);
				System.out.println("Hier fertig");
				System.out.println(number);
				} catch (NullPointerException e){
					System.out.println("its a trap!!!!");
//					outStream.writeUTF("<Error/>SpecifiyAdressee");
				}

			}

			// dOut.close();
			// adressee.close();
			// inStream.close();
			// outStream.close();
			// clientSocket.close();
			//
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("Server lost connection to  client");
		}

		// }
	}
}
