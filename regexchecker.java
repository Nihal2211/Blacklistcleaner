
public class regexchecker {


	public static void main(String[] args) {

		String s = "www.crawler.archive.left/images";
		s = s.replaceAll("www.", "");
		s = s.replaceAll("/.*", "");
		
		System.out.println(s);
	}
}