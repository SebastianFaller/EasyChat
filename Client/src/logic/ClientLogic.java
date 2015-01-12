package logic;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

import ui.ChatWindow;
import ui.JListRenderer;
import ui.LoginWindow;
import ui.RegistrationWindow;

//TODO ThreadPool angucken

public class ClientLogic {

	private Socket socket;
	private DataInputStream inStream;
	private DataOutputStream outStream;
	private String userName;
	private ChatWindow window;
	private LoginWindow loginWindow;
	private boolean connected;
	private int port;
	private boolean loggedIn;
	private int loginAttempts;
	private UpdateContactsThread updateContactsThread;
	public static final Pattern protocolPattern = Pattern.compile("<.*?/>");
	private RegistrationWindow regWindow;
	private String[] onlineUseres;
	private HashMap<String, String> textMap;
	private String ip = "87.173.84.135";
	
//	boolean demandUsers;
//	private UpdateContactsThread updateContactsThread;
	// just for test purposes
	private int sent = 0;

	public ClientLogic(int port) {
		this.userName = null;
		this.loggedIn = false;
		this.port = port;
		this.loginAttempts = 0;

		textMap = new HashMap<String, String>();

		// window = new ChatWindow(userName, this);

		try {
			connectWithServer(ip, port);
			connected = true;
		} catch (IOException e) {
			System.out.println("Connection failed");
			connected = false;
			loggedIn = false;
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
				Matcher matcher = null;
				while (true) {
					while (connected) {
//						if(demandUsers){
//							try {
//								demandOnlineUsers();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						
						try {
							System.out.println("in der run");
							String received = inStream.readUTF();
							System.out.println(received);
							matcher = protocolPattern.matcher(received);
							matcher.lookingAt();
							//first match
							String protocolMark = matcher.group(0);

							
							switch (protocolMark){
								
							case "<whoareyou/>":
								System.out.println("registrate in else case");
								logInAtServer();
								break;
								
							case "<UserList/>":
								applyOnlineUsers(received);
								break;
								
							case "<message/>":
								if (window == null) {
									window = new ChatWindow("EasyChat - "+userName,
											ClientLogic.this);
									loginWindow.closeFrame();
									updateContactsThread.start();//TODO dont forget
								}
								if (receivedTotal == 0) {
									window.getChatDisplay().setText("");
								}
								//10 = lenght of <message/>
								received = received.substring(10);
								String message, sender = null;
								try{
								 message = received.split(Pattern.quote("<sender/>"))[1];
								 sender = received.split(Pattern.quote("<sender/>"))[0];
								} catch (ArrayIndexOutOfBoundsException ae){
									 message = received;
								}
								
								displayMessage(message, sender);
								receivedTotal++;
								
								break;
								
							case "<nameTaken/>":
								received = received.split(Pattern.quote("<nameTaken/>"))[1];
								if(received.equals("true")){
									regWindow.showNameTaken();
//									loginWindow.setVisible(false);
								} else {
									regWindow.closeFrame();
								}
								
								break;
								
							}
							
							
							
						} catch (IOException e) {
							System.out
									.println("Nachricht konnte nicht empfangen werden");
							// e.printStackTrace();
							connected = false;
							loggedIn = false;
						}
				
					}// while
					reastablishConnection();
				}// while
			}// run
		};
		
		updateContactsThread = new UpdateContactsThread(window, this);
		

		// Thread senderThread = new Thread() {
		// @Override
		// public void run() {
		//
		//
		// }// run
		//
		// };

		receiverThread.start();
		
		// senderThread.start();

		// outStream.close();
		// inStream.close();
		// socket.close();
	}// clientSendAndReceive

	// TODO Find out what first Arg does
	// TODO Define own "ConnectionFaileException" or something like that
	public void connectWithServer(String dontknow, int port) throws IOException {
		System.out.println("connectWithServer");
//		InetAddress adress = InetAddress.g
		socket = new Socket(ip, port);
		inStream = new DataInputStream(socket.getInputStream());
		outStream = new DataOutputStream(socket.getOutputStream());
		System.out.println("done ceonnection");
	}

	public synchronized boolean reastablishConnection() {
		System.out.println("Connection to Server lost");
		System.out.println("reconnect? y/n");
		int select = JOptionPane.showConfirmDialog(null, "Connection to Server lost - reconnect?","Connection lost",  JOptionPane.YES_NO_OPTION);
//		Scanner scanner = new Scanner(System.in);
//		String yOrN = scanner.nextLine();
		System.out.println(select);
		if ((select == JOptionPane.OK_OPTION) && (!connected)) {
			System.out.println("even tried");
			// scanner.close();
			try {
				connectWithServer("localhost", 1025);
				connected = true;
				System.out.println("succsess");
			} catch (IOException e) {
				System.out.println("Connection still not established");
				// e.printStackTrace();
				connected = false;
				loggedIn = false;
				return false;
			}// catch
			return true;
		}// if
		else {
			// scanner.close();
			return false;
		}
	}// reastablishConnection

	public void logInAtServer() {
		// if(window!=null) {
		// window.closeFrame();
		// window = null;
		// }
		if (loginAttempts == 0) {
			loginWindow = new LoginWindow(this);
		} else {
			loginWindow.loginFailed();
		}

		// while(!loggedIn);
	}

	public void loginWithValues(char[] password, String userName) {
		this.userName = userName;
		do {
			try {
				System.out.println(sent++);
				String pwd = "";
				for (int i = 0; i < password.length; i++) {
					pwd += password[i];
				}
				outStream.writeUTF("<login/><user/>" + userName + "<pwd/>"
						+ pwd);
				this.userName = userName;
				System.out.println("password: " + pwd);
				pwd = "";
				deletePassword(password);
				loginAttempts++;
				// boolean respond = inStream.readBoolean();
				System.out.println("wixxxr");

				// if(respond){
				// loggedIn = true;
				// loginWindow.closeFrame();
				// } else {
				// loginWindow.loginFailed();
				// }

			} catch (IOException e) {
				System.out.println("registrating at the server failed");
				connected = false;
				loggedIn = false;
				reastablishConnection();
				e.printStackTrace();
			}
		} while (!connected);
		// final ChatWindow window = new ChatWindow("EasyChat", this);
		// this.window = window;
	}

	public void displayMessage(String message, String sender) {
		if (window != null) {
			changeCurrentUser(window.getUserList().getSelectedValue(), sender);
			if(sender != null){
//				window.getChatDisplay().setForeground(Color.RED);
			window.getChatDisplay().append("\n"+sender+"\t"+(new Date()).toString());
//			window.getChatDisplay().setForeground(Color.BLACK);
			window.getChatDisplay().append("\n"+message + "\n");
			} else {
				window.getChatDisplay().append(message + "\n");
			}
		} else {
			System.out.println(message + "| Client erhalten");
		}
	}

	public void sendMessage(String message, String adressee) {
		// if no window
		// while (window == null) {
		// Scanner s = new Scanner(System.in);
		// System.out.println("Sender");
		// while (connected) {
		// try {
		// System.out.println("Empf�nger eingeben");
		// String adressee = s.nextLine();
		// System.out.println("Text eingeben:");
		// outStream.writeUTF(adressee + "<adressee/> "
		// + s.nextLine());
		// System.out.println("was gesendet");
		// } catch (IOException e) {
		// System.out
		// .println("Nachricht konnte nicht versand werden");
		// e.printStackTrace();
		// connected = false;
		// }
		// }// while
		// reastablishConnection();
		// }// while
		// else
		if (connected) {
			try {
				if(userName != null){
					window.getChatDisplay().append("\nIch:\t"+(new Date().toString())+"\n"+message+"\n");
				outStream.writeUTF("<message/>"+adressee + "<adressee/>" +userName+"<sender/>"+ message);
//				String text = textMap.get(adressee);
//				window.getChatDisplay().setForeground(Color.GREEN);
				
//				window.getChatDisplay().setForeground(Color.BLACK);
//				window.getChatDisplay().append();
//				text += "Ich:\n"+message;
//				textMap.put(adressee, text);
//				System.out.println("text "+text);
				} else {
					outStream.writeUTF("<message/>"+adressee + "<adressee/>"+ message);
				}
			} catch (IOException e) {
				System.out.println("Nachricht konnte nicht versand werden");
				e.printStackTrace();
				connected = false;
				loggedIn = false;
			}
		} else {
			System.out.println("not connected sending impossible");
			reastablishConnection();

		}

	}

	public int registrateAtServer(String name, char[] password1,
			char[] password2, String filePath) {
		
	
		boolean equal = true;
		if (password1.length == password2.length) {
			for (int i = 0; i < password1.length; i++) {
				if (password1[i] != password2[i])
					equal = false;
			}
		} else {
			equal = false;
		}

		if (equal) {
			byte[] byteArray = null;
			int picLenght = 0;
			try {
				File file = new File(filePath);
				FileInputStream fis = new FileInputStream(file);
				picLenght = (int) file.length();
				byteArray = new byte[picLenght];
				fis.read(byteArray);
				fis.close();
			} catch (FileNotFoundException e1) {
				//return 1 means error with file
				return 1;
			} catch (IOException e) {
				return 1;
//				e.printStackTrace();
			}

			try {
				String pw = "";
				for (int i = 0; i < password1.length; i++) {
					pw += password1[i];
				}
				System.out.println("versuche zu registrieren");
				outStream.writeUTF("<registrate/><name/>" + name + "<pwd/>"
						+ pw+"<lenght/>"+picLenght);
				outStream.write(byteArray);
				System.out.println("searching answer");
				//TODO empfängt nichts
//				boolean answerNameTaken = inStream.readBoolean();
//				System.out.println("answer found "+answerNameTaken);
//				if(answerNameTaken){
//					//3 means name already taken
//					return 3;
//				} 
				System.out.println("registreieren klappt");
				pw = "";
				deletePassword(password1);
				deletePassword(password2);
			} catch (IOException e) {
				System.out.println("registrieren klappt nicht");
				e.printStackTrace();
			} 
			//return 0 menas success
			return 0;
		} else {
			System.out.println("false");
			deletePassword(password1);
			deletePassword(password2);
			//2 means password not equal
			return 2;
		}

	}

	
	public void deletePassword(char[] password) {
		for (int i = 0; i < password.length; i++) {
			password[0] = 0;
		}
	}

	public void demandOnlineUsers() throws IOException {
		if(connected)
		outStream.writeUTF("<getUsers/>");
		System.out.println("demanded users");
	}

	
	//TODO unschöne regex
	public void applyOnlineUsers(String message) {
		System.out.println("String mit allen usern drin: "+message);
		ArrayList<String> arrayList = new ArrayList<String>();
		HashMap<String, byte[]> nameImgMap = new HashMap<String, byte[]>();
		Pattern p = Pattern.compile("<user>.*?<user/>");
		Matcher m = p.matcher(message);
//		m.lookingAt();
		// Lenght of the tag <UserList><users/>
//		message = message.substring(18);
		//TODO boolean always true
		System.out.println("message "+message);
		while (m.find()) {
//			// add everything bevor tag to array
//			arrayList.add(message.split(Pattern.quote("<user/>"))[0]);
//			System.out.println("added to arrayList "+message.split(Pattern.quote("<user/>"))[0]);
//			// new string is everything behind tag
//			try {
//			message = message.split(Pattern.quote("<users/>"))[1];
//			System.out.println("new message "+message);
//			} catch (Exception e){
//				e.printStackTrace();
//				break;
//			}
			String toAdd = m.group();
			//lenght of first tag
			toAdd = toAdd.substring(6);
			//minus the last tag
			toAdd = toAdd.substring(0, toAdd.length()-7);
			System.out.println("toAdd: "+toAdd);
			String name = toAdd.split(Pattern.quote("<l/>"))[0];
			int lenght = Integer.parseInt(toAdd.split(Pattern.quote("<l/>"))[1]);
//			String name = toAdd.split(Pattern.quote("<img/>"))[0];
			arrayList.add(name);
//			toAdd = toAdd.split(Pattern.quote("<img/>"))[1];
//			toAdd = toAdd.substring(6);
			
			
//				String[] regRes = toAdd.split(Pattern.quote("</>"));
//				byte[] b = new byte[regRes.length];
//				for(int i = 0; i<regRes.length;i++){
//					b[i] = Byte.parseByte(regRes[i]);
//				}
			byte [] b = new byte[lenght];
			for(int i = 0; i<b.length;i++){
				try {
					b[i] = inStream.readByte();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			nameImgMap.put(name, b);
				
///////////////////////////////////////////////////
				
//JFrame frame = new JFrame();
//JLabel label = new JLabel(new ImageIcon(b));
//frame.getContentPane().add(label);
//label.setVisible(true);
//frame.pack();
//frame.setVisible(true);
//System.out.println("benutzer empfangen");

////////////////////////////////////////////////////////////
			
			
			
		}
		
		JList<String> userList = window.getUserList();
		System.out.println("fdehoögrewthw"+arrayList);
		String[] result = new String[arrayList.size()];
		for(int i = 0; i<result.length;i++){
			result[i] = arrayList.get(i);
		}
		String selected	= userList.getSelectedValue();
//		while((userList = window.getUserList()) == null);
		System.out.println("userList"+userList);
		System.out.println("result"+result);
		
		if((userList != null) && (result != null)){
		userList.setListData(result);
		}
		
		((JListRenderer)userList.getCellRenderer()).setPictures(nameImgMap);
		userList.setSelectedValue(selected, true);
		onlineUseres = result;
	}
	
	public void setRegWindow (RegistrationWindow regWindow){
	this.regWindow = regWindow;
	}
	
	public void changeCurrentUser(String oldName, String newName){
		if(oldName !=null && newName != null){
		String oldText = textMap.get(oldName);
		oldText = window.getChatDisplay().getText();
		textMap.put(oldName, oldText);
		window.getChatDisplay().setText(textMap.get(newName));
		}
	}
		

	// public void setWindow(ChatWindow window){
	// this.window = window;
	// }

	public static void main(String args[]) throws Exception {
		// System.out.println( "Host Name/Adresse: " +
		// InetAddress.getLocalHost() );
		// System.out.println(InetAddress.getByName("sebastian-MS-7502").isSiteLocalAddress());
		// System.out.println(InetAddress.getByName("127.0.1.1").isReachable(20000));
		//
		// String localHost = InetAddress.getLocalHost().getHostName();
		// for ( InetAddress ia : InetAddress.getAllByName(localHost) )
		// System.out.println( ia );
		//
		// Scanner s = new Scanner(System.in);
		// System.out.println("Bitte Namen eingeben");

		ClientLogic c = new ClientLogic(1025);
		c.clientSendAndReceive();

		System.out.println("client build");
		// loginWindow.getClientLogic().clientSendAndReceive();

		// s.close();
	}

}
