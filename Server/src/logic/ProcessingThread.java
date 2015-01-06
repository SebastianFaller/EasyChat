package logic;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

import data.DatabaseConnection;

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
		logInClient();
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
		
		finally {
			try {
				closeStreams();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// }
	}
	
	public void logInClient(){
		boolean logginSuccessful = false;
		do{
					try {
						outStream.writeUTF("<whoareyou/>");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String loginData = null;
					while (loginData == null) {
						try {
							loginData = inStream.readUTF();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;	
						}
					}
					// zerlege string. zweiter teil ist name
//					System.out.println("temp name 1: " + tempName);
					System.out.println("loginData"+loginData);
					loginData = loginData.split(Pattern.quote("<login/>"))[1];
//					System.out.println("temp name 2: " + tempName);
					String pwd = loginData.split(Pattern.quote("<pwd/>"))[1];
					String name = loginData.split(Pattern.quote("<pwd/>"))[0];
					//ohne führendes <name/>
					name = name.substring(7);
					System.out.println(name);
					DatabaseConnection database = new DatabaseConnection();
					String[] s = database.getPwdToUserFromDB(name);
					try {
						database.closeDatabaseConnection();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if((s!=null) && (s[1].equals(pwd)) && (s[0]).equals(name)){
						try {
							System.out.println("succsses");
							outStream.writeUTF("Wilkommen! "+name);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						clients.put(name, clientSocket);
						logginSuccessful = true;
						System.out.println("pwd laut sql: "+s[1]);
					}
					
		} while(!logginSuccessful);
									
	}
	
	public void closeStreams() throws IOException{
		inStream.close();
		outStream.close();
	}
}
