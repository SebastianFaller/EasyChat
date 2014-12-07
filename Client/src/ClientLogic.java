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
			socket = new Socket("localHost", port);
			inStream = new DataInputStream(socket.getInputStream());
			outStream = new DataOutputStream(socket.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clientSendAndReceive() throws IOException {
		System.out.println("kurz vorm senden");
		outStream.writeUTF(userName);

		Thread receiverThread = new Thread() {

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
					}
					System.out.println("was gesendet");
				}
			}
		};
		receiverThread.start();

		Thread senderThread = new Thread() {

			@Override
			public void run() {
				Scanner s = new Scanner(System.in);
				while (true) {
					try {
						System.out.println("Text eingeben:");
						outStream.writeUTF("tom<adressee/> " + s.nextLine());
						System.out.println("was gesendet");
					} catch (IOException e) {
						System.out
								.println("Nachricht konnte nicht versand werden");
						e.printStackTrace();
					}

				}
			}
		};
		senderThread.start();

		// outStream.close();
		// inStream.close();
		// socket.close();
	}// clientSendAndReceive

	
	
	public static void main(String args[]) throws Exception{
//		System.out.println( "Host Name/Adresse: " + InetAddress.getLocalHost() );
//		System.out.println(InetAddress.getByName("sebastian-MS-7502").isSiteLocalAddress());
//		System.out.println(InetAddress.getByName("127.0.1.1").isReachable(20000));
//	
//		String localHost = InetAddress.getLocalHost().getHostName();
//		for ( InetAddress ia : InetAddress.getAllByName(localHost) )
//		  System.out.println( ia );
		
		ClientLogic c = new ClientLogic(1025, "jerry");
		System.out.println("client build");
		c.clientSendAndReceive();
	}

}
