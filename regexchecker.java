
public class regexchecker {


	public static void main(String[] args) {

    	String s = " abd";
		System.out.println(s.substring(0,1));
		System.out.println(s.substring(0,1).matches("[a-z]"));
		//s = s.replaceAll("/.*", "");
		
		//System.out.println(s);
	}
}