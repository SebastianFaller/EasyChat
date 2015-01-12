import java.net.InetAddress;
import java.net.UnknownHostException;

public class test {

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		// HashMap<String, Object> h = new HashMap<String, Object>();
		// h.put("hallo", "Wixxxr");
		// System.out.println(h.get("hallo"));
		// System.out.println(h.get(null));
		// System.out.println(h.get("du trattel"));

		// String s1 = "<message/>Wilkommen! user1";
		// String s2 = "<UserList/><user>user2<user/><user>user1<user/>";
		// String s3 = "<user/>frzh6iuzlou6ra<user/>gruhzt<user/>";
		//
		// Pattern pattern = Pattern.compile("<user>.*?<user/>");
		// Matcher matcher = pattern.matcher(s2);
		// while(matcher.find()){
		// System.out.println(matcher.group());
		// }

		System.out.println("Host Name/Adresse: " + InetAddress.getLocalHost());

		// Matcher matcher2 = pattern.matcher(s2);
		// while(matcher2.find()){
		// System.out.println(matcher2.group());
		// }
		//
		// Matcher matcher3 = pattern.matcher(s3);
		// while(matcher3.find()){
		// System.out.println(matcher3.group());
		// }

	}

}
