package logic;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


import ui.ChatWindow;

//TODO ThreadPool angucken

public class ClientLogic {

	private Socket socket;
	private DataInputStream inStream;
	private DataOutputStream outStream;
	private String userName;
	private ChatWindow window;
	private boolean connected;
	private int port;
	//just for test purposes
	int sent = 0;

	public ClientLogic(int port, String user) {
		userName = user;
		this.port = port;
		
		try {
			connectWithServer("localHost", port);
			connected = true;
		} catch (IOException e) {
			System.out.println("Connection failed");
			connected = false;
			while (!reastablishConnection())
				;
			// e.printStackTrace();
		}
	}// constructor

	public void clientSendAndReceive() {
		// System.out.println("kurz vorm senden");
		// String firstReceived = null;
		// try {
		// firstReceived = inStream.readUTF();
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// // e1.printStackTrace();
		// System.out.println("could not connect properly");
		// }
		// if(firstReceived.equals("<whoareyou/>")){
		// registrateAtServer();
		// }
		// else{
		// socket = null;
		// connectWithServer("dehifreiqtgjria", port);
		// }

		final Thread receiverThread = new Thread() {
			@Override
			public void run() {
				int receivedTotal = 0;
				while (true) {
					while (connected) {
						try {
							String received = inStream.readUTF();
							if (!received.equals("<whoareyou/>")) {
								if(receivedTotal==0) window.getChatDisplay().setText("");
								displayMessage(received);
								receivedTotal++;
							} else {
								System.out.println("registrate in else case");
								registrateAtServer();
							}
						} catch (IOException e) {
							System.out
									.println("Nachricht konnte nicht empfangen werden");
							// e.printStackTrace();
							connected = false;
						}
					}// while
					reastablishConnection();
				}// while
			}// run
		};

//		Thread senderThread = new Thread() {
//			@Override
//			public void run() {
//				
//				
//			}// run
//
//		};

		receiverThread.start();
//		senderThread.start();

		// outStream.close();
		// inStream.close();
		// socket.close();
	}// clientSendAndReceive

	// TODO Find out what first Arg does
	// TODO Define own "ConnectionFaileException" or something like that
	public void connectWithServer(String dontknow, int port) throws IOException {
		System.out.println("connectWithServer");
		socket = new Socket("localHost", port);
		inStream = new DataInputStream(socket.getInputStream());
		outStream = new DataOutputStream(socket.getOutputStream());
	}

	public synchronized boolean reastablishConnection() {
		System.out.println("Connection to Server lost");
		System.out.println("reconnect? y/n");
		Scanner scanner = new Scanner(System.in);
		String yOrN = scanner.nextLine();
		System.out.println(yOrN);
		if ((yOrN.charAt(0) == 'y') && (!connected)) {
			System.out.println("even tried");
			// scanner.close();
			try {
				connectWithServer("blabala", 1025);
				connected = true;
				System.out.println("succsess");
			} catch (IOException e) {
				System.out.println("Connection still not established");
				// e.printStackTrace();
				connected = false;
				return false;
			}// catch
			return true;
		}// if
		else {
			// scanner.close();
			return false;
		}
	}// reastablishConnection

	public void registrateAtServer() {

		do {
			try {
				System.out.println(sent++);
				outStream.writeUTF("<iam/>" + userName);
			} catch (IOException e) {
				System.out.println("registrating at the server failed");
				connected = false;
				reastablishConnection();
				e.printStackTrace();
			}
		} while (!connected);
	}
	
	public void displayMessage(String message){
		if(window != null){
			window.getChatDisplay().append(message+"\n");
		} else {
			System.out.println(message+ "| Client erhalten");
		}	
	}
	
	public void sendMessage(String message, String adressee){
		//if no window
//			while (window == null) {
//				Scanner s = new Scanner(System.in);
//				System.out.println("Sender");
//				while (connected) {
//					try {
//						System.out.println("Empf�nger eingeben");
//						String adressee = s.nextLine();
//						System.out.println("Text eingeben:");
//						outStream.writeUTF(adressee + "<adressee/> "
//								+ s.nextLine());
//						System.out.println("was gesendet");
//					} catch (IOException e) {
//						System.out
//								.println("Nachricht konnte nicht versand werden");
//						e.printStackTrace();
//						connected = false;
//					}
//				}// while
//				reastablishConnection();
//			}// while
			//else
		if(connected){
			try {
				outStream.writeUTF(adressee+"<adressee/>"+message);
			} catch (IOException e) {
				System.out.println("Nachricht konnte nicht versand werden");
				e.printStackTrace();
				connected = false;
			}
		} else {
			System.out.println("not cennected sending impossible");
			reastablishConnection();
		
		}
	}
	
	public void setWindow(ChatWindow window){
		this.window = window;
	}
	

	public static void main(String args[]) throws Exception {
		// System.out.println( "Host Name/Adresse: " +
		// InetAddress.getLocalHost() );
		// System.out.println(InetAddress.getByName("sebastian-MS-7502").isSiteLocalAddress());
		// System.out.println(InetAddress.getByName("127.0.1.1").isReachable(20000));
		//
		// String localHost = InetAddress.getLocalHost().getHostName();
		// for ( InetAddress ia : InetAddress.getAllByName(localHost) )
		// System.out.println( ia );
		
		Scanner s = new Scanner(System.in);
		System.out.println("Bitte Namen eingeben");
		ClientLogic c = new ClientLogic(1025, s.nextLine());
		ChatWindow window = new ChatWindow("EasyChat", c);
		c.setWindow(window);
		
		System.out.println("client build");
		c.clientSendAndReceive();
		
		// s.close();
	}

}