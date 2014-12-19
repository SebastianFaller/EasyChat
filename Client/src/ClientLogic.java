import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

//TODO ThreadPool angucken

public class ClientLogic {

	private Socket socket;
	private DataInputStream inStream;
	private DataOutputStream outStream;
	private String userName;
	private boolean connected;

	public ClientLogic(int port, String user) {
		userName = user;
		try {
			connectWithServer("localHost", port);
			connected = true;
		} catch (IOException e) {
			System.out.println("Connection failed");
			connected = false;
			while(!reastablishConnection());
//			e.printStackTrace();
		}
	}//constructor

	public void clientSendAndReceive(){
		System.out.println("kurz vorm senden");
		registrateAtServer();
		
		final Thread receiverThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					while (connected) {
						try {
							String received = inStream.readUTF();
							if(!received.equals("<whoareyou/>")){
							System.out.println(received + "| Client erhalten");
							}
							else{
								registrateAtServer();
							}
						} catch (IOException e) {
							System.out
									.println("Nachricht konnte nicht empfangen werden");
//							e.printStackTrace();
							connected = false;
						}
					}// while
					reastablishConnection();
				}// while
			}// run
		};

		Thread senderThread = new Thread() {
			@Override
			public void run() {
				Scanner s = new Scanner(System.in);
				while (true) {
					System.out.println("Sender");
					while (connected) {
						try {
							System.out.println("Empf�nger eingeben");
							String adressee = s.nextLine();
							System.out.println("Text eingeben:");
							outStream.writeUTF(adressee + "<adressee/> "
									+ s.nextLine());
							System.out.println("was gesendet");
						} catch (IOException e) {
							System.out
									.println("Nachricht konnte nicht versand werden");
							e.printStackTrace();
							connected = false;
						}
					}// while
					reastablishConnection();
				}// while
			}// run

		};

		receiverThread.start();
		senderThread.start();

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
		if ((yOrN.charAt(0) == 'y')&&(!connected)) {
			System.out.println("even tried");
//			scanner.close();
			try {
				connectWithServer("blabala", 1025);
				connected = true;
				System.out.println("succsess");
			} catch (IOException e) {
				System.out.println("Connection still not established");
//				e.printStackTrace();
				connected = false;
				return false;
			}// catch
			return true;
		}// if
		else {
//			scanner.close();
			return false;
		}
	}// reastablishConnection
	
	public void registrateAtServer(){
		do{
			try{
			outStream.writeUTF("<adressee/>"+userName);
			} catch(IOException e){
				System.out.println("registrating at the server failed");
				connected = false;
				reastablishConnection();
				e.printStackTrace();
			}
			}while(!connected);
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
		System.out.println("client build");
		c.clientSendAndReceive();
//		s.close();
	}

}
