import java.util.HashMap;


public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap<String, Object> h = new HashMap<String, Object>();
		h.put("hallo", "Wixxxr");
		System.out.println(h.get("hallo"));
		System.out.println(h.get(null));
		System.out.println(h.get("du trattel"));

	}

}
