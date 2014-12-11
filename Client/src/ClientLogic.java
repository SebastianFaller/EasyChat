import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
//TODO ThreadPool angucken

public class ClientLogic {
	
	private Socket socket;
	private DataInputStream inStream;
	private DataOutputStream outStream;
	private String userName;
	
	public ClientLogic(int port,String user){
		userName = user;
		try {
			connectWithServer("localHost", port);
		} catch (IOException e) {
			System.out.println("Connection failed");
			e.printStackTrace();
		}
	}
	
	public void clientSendAndReceive() throws IOException {
		System.out.println("kurz vorm senden");
		outStream.writeUTF(userName);

		final Thread receiverThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						String received = inStream.readUTF();
						System.out.println(received + "| Client erhalten");
					} catch (IOException e) {
						System.out
								.println("Nachricht konnte nicht versand werden");
						e.printStackTrace();
						reastablishConnectionIfLost();
						break;
					}
				}//while
			}
		};

		Thread senderThread = new Thread() {
			@Override
			public void run() {
				Scanner s = new Scanner(System.in);
				while (!isInterrupted()) {
					try {
						System.out.println("Empf�nger eingeben");
						String adressee = s.nextLine(); 
						System.out.println("Text eingeben:");
						outStream.writeUTF(adressee+"<adressee/> " + s.nextLine());
						System.out.println("was gesendet");
					} catch (IOException e) {
						System.out
								.println("Nachricht konnte nicht versand werden");
						e.printStackTrace();
						reastablishConnectionIfLost();
						break;
					}

				}//while
				System.out.println("Sender");
			}

		};
		
		receiverThread.start();
		senderThread.start();

		// outStream.close();
		// inStream.close();
		// socket.close();
	}// clientSendAndReceive

	//TODO Find out what first Arg does
	//TODO Define own "ConnectionFaileException" or something like that
	public void connectWithServer(String dontknow, int port) throws IOException{
			socket = new Socket("localHost", port);
			inStream = new DataInputStream(socket.getInputStream());
			outStream = new DataOutputStream(socket.getOutputStream());
	}
	
	
	public synchronized boolean reastablishConnectionIfLost(){
		System.out.println("Connection to Server lost");
		System.out.println("reconnect? y/n");
		if(new Scanner(System.in).next()=="y"){
			try {
				connectWithServer("blabala", 1025);
			} catch (IOException e) {
				System.out.println("Connection still not established");
				e.printStackTrace();
				return false;
			}//catch
			return true;
		}//if
		else

	}
	
	public static void main(String args[]) throws Exception{
//		System.out.println( "Host Name/Adresse: " + InetAddress.getLocalHost() );
//		System.out.println(InetAddress.getByName("sebastian-MS-7502").isSiteLocalAddress());
//		System.out.println(InetAddress.getByName("127.0.1.1").isReachable(20000));
//	
//		String localHost = InetAddress.getLocalHost().getHostName();
//		for ( InetAddress ia : InetAddress.getAllByName(localHost) )
//		  System.out.println( ia );
		Scanner s = new Scanner(System.in);
		System.out.println("Bitte Namen eingeben");
		ClientLogic c = new ClientLogic(1025, s.nextLine());
		System.out.println("client build");
		c.clientSendAndReceive();
	}

}
