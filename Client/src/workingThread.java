import java.io.IOException;
import java.util.Scanner;


public abstract class workingThread extends Thread{
	
	public workingThread(){
		
	}

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
				break;
			}

		}//while

		
	}
	
	public void workingLoop(){
		
	}
	
	public void handleDisconnection(){
		System.out.println("Sender");
		System.out.println("Connection to Server lost");
		System.out.println("reconnect? y/n");
		if(new Scanner(System.in).next()=="y"){
			try {
//				receiverThread.interrupt();
				connectWithServer("blabala", 1025);
				this.run();
			} catch (IOException e) {
				System.out.println("Connection still not established");
				e.printStackTrace();
			}//catch
		}//if
	}
}
