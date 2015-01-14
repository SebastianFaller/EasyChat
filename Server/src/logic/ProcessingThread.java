package logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.DatabaseConnection;

public class ProcessingThread extends Thread {

	private Socket clientSocket;
	private DataInputStream inStream;
	private DataOutputStream outStream;
	private HashMap<String, Socket> clients;
	private String name;
	private DatabaseConnection database;

	private static final Pattern protocolMark = Pattern.compile("<.*?/>");
	public static int number = 0;

	public ProcessingThread(Socket clientSocket, HashMap<String, Socket> clients)
			throws IOException {

		this.clientSocket = clientSocket;
		this.clients = clients;
		inStream = new DataInputStream(clientSocket.getInputStream());
		outStream = new DataOutputStream(clientSocket.getOutputStream());
		number++;
		database = new DatabaseConnection();
	}

	@Override
	public void run() {
		logInClient();
		try {
			Matcher matcher = null;

			while (true) {
				String received = inStream.readUTF();

				matcher = protocolMark.matcher(received);
				matcher.lookingAt();

				switch (matcher.group()) {

				case "<message/>":
					received = received.substring(10);
					String[] segs = received
							.split(Pattern.quote("<adressee/>"));
					try {
						Socket adressee = clients.get(segs[0]);
						DataOutputStream dOut = new DataOutputStream(
								adressee.getOutputStream());
						dOut.writeUTF("<message/>" + segs[1]);
					} catch (NullPointerException e) {
						System.out.println("its a trap!!!!");
					}
					break;

				case "<getUsers/>":
					sendOnlineUsers();
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("Server lost connection to  client");
		}

		finally {
			try {
				database.closeDatabaseConnection();
				closeStreams();
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void logInClient() {
		boolean logginSuccessful = false;
		do {
			try {
				// server asks the client to identify itself
				outStream.writeUTF("<whoareyou/>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			String loginData = null;
			while (loginData == null) {
				try {
					loginData = inStream.readUTF();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}// while

			try {
				// this statement throws ArrayyOutOfBoundsExc when first
				// statement is not login

				// split String. Second part is name
				loginData = loginData.split(Pattern.quote("<login/>"))[1];
				String pwd = loginData.split(Pattern.quote("<pwd/>"))[1];
				name = loginData.split(Pattern.quote("<pwd/>"))[0];
				// ohne führendes <name/>
				name = name.substring(7);
				String[] s = database.getPwdToUserFromDB(name);
				// s is the result from the Database
				if ((s != null) && (s[1].equals(pwd)) && (s[0]).equals(name)) {
					try {
						outStream.writeUTF("<message/>Wilkommen! " + name);
						pwd = "";
					} catch (IOException e) {
						e.printStackTrace();
					}
					clients.put(name, clientSocket);
					logginSuccessful = true;
				}
				// when password and name don't fit the loop will start right again

			} catch (ArrayIndexOutOfBoundsException e) {
				// catches if string doesnt start with "<login/>". Therefore it
				// tries to registrate
				loginData = loginData.split(Pattern.quote("<registrate/>"))[1];
				String pwd = loginData.split(Pattern.quote("<pwd/>"))[1];
				String name = loginData.split(Pattern.quote("<pwd/>"))[0];

				// receiving the picture from user
				String picLenghtString = pwd.split(Pattern.quote("<lenght/>"))[1];
				int lenght = Integer.parseInt(picLenghtString);
				pwd = pwd.split(Pattern.quote("<lenght/>"))[0];
				byte[] image = new byte[lenght];
				try {
					inStream.read(image);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// without <name/>
				name = name.substring(7);

				database = new DatabaseConnection();
				boolean nameTaken = database.isNameTaken(name);

				try {
					if (nameTaken) {
						outStream.writeUTF("<nameTaken/>true");
					} else {
						outStream.writeUTF("<nameTaken/>false");
						// make a record for the user in the Database
						database.makeNewRowInUsers(name, pwd, image);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} while (!logginSuccessful);
	}

	public void closeStreams() throws IOException {
		clients.remove(name);
		inStream.close();
		outStream.close();

	}

	public void sendOnlineUsers() throws IOException {
		String result = "";
		//go through all clients
		for (String k : clients.keySet()) {
			//pack the String with name, pwd and img length and load the images
			result += "<user>" + k;
			byte[] b = database.getImageToUser(k);
			result += "<l/>" + b.length + "<user/>";
		}
		outStream.writeUTF("<UserList/>" + result);
		//write a image for every client in the List
		for (String k : clients.keySet()) {
			byte[] b = database.getImageToUser(k);
			outStream.write(b);
		}

	}
}
