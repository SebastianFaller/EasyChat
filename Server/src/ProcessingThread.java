import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.regex.Pattern;


public class ProcessingThread extends Thread{

	private Socket clientSocket;
	private DataInputStream inStream;
	private DataOutputStream outStream; 
	private HashMap<String, Socket> clients;
	public static int number = 0;
	
	public ProcessingThread(Socket clientSocket, HashMap clients) throws IOException{
		
		this.clientSocket = clientSocket;
		this.clients = clients;
		inStream = new DataInputStream(clientSocket.getInputStream());
		outStream = new DataOutputStream(clientSocket.getOutputStream());
		number++;
	}
	
	@Override
	public void run(){
//		while(true){
			try {
				String received = inStream.readUTF();
				String[] segs = received.split(Pattern.quote("<adressee/>"));
				System.out.println(segs[0]);
				Socket adressee = clients.get(segs[0]);
				if(adressee==null) System.out.println("its a trap!!!!");
				DataOutputStream dOut = new DataOutputStream(adressee.getOutputStream());
				System.out.println("outstream build");
				dOut.writeUTF(segs[1]);
				System.out.println("Hier fertig");
				System.out.println(number);
//				dOut.close(); 
//				adressee.close();
//				inStream.close();
//				outStream.close();
//				clientSocket.close();
//				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
//		}
	}
}