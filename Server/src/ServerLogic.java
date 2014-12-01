import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerLogic {
	
	private ServerSocket server;
	private Socket client;
	private DataInputStream inStream;
	private DataOutputStream outStream;
	
	public ServerLogic(int port){
		try {
			server = new ServerSocket(port);
			
			client = server.accept();
			inStream = new DataInputStream(client.getInputStream());
			outStream = new DataOutputStream(client.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//constructor
	
	public void serverRoutine() throws IOException{
		System.out.println("gay");
		String received = inStream.readUTF();
		System.out.println("blub");
		received = received+" server erhalten ";
		outStream.writeUTF(received);
		outStream.close();
		inStream.close();
		client.close();
	}
	
	public static void main(String args[]) throws IOException{
		ServerLogic s = new ServerLogic(1025);
		System.out.println("server build");
		s.serverRoutine();
	}

}
