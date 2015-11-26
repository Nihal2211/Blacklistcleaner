import com.mysql.jdbc.Driver;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Blacklistcleaner3{

	static ArrayList<String> blackListDomainArray = new ArrayList<String>();
	static HashMap <String, Integer> Blacklist_Domain = new HashMap <String, Integer>();
	static HashMap <String, Integer> Blacklist_URL = new HashMap <String, Integer>();
	static ArrayList<String> blackListURLArray = new ArrayList<String>();
	
	static ArrayList<String> Domain_Doc_Array = new ArrayList<String>();
	static ArrayList<String> URL_Doc_Array = new ArrayList<String>();
	
	
	
	static BufferedWriter output = null;
	static BufferedWriter badoutput = null;
	
	public static int sort(String s1, String s2) {
		return s1.compareToIgnoreCase(s2);
	}

	public static void Readexcelfile(String Inputfile, Connection con) throws SQLException, IOException{
		File inputWorkbook = new File(Inputfile);
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			String url;
			String domainUrl;
			File file = new File("/home/qu/Desktop/Cleaned_Arabic.txt");
			output = new BufferedWriter(new FileWriter(file));
			File file1 = new File("/home/qu/Desktop/Bad_list_alexa.txt");
			badoutput = new BufferedWriter(new FileWriter(file1));
			// Get the first sheet
			
			
			
			for (int j = 0; j < w.getNumberOfSheets(); j++) {
				Sheet sheet = w.getSheet(j);

				// Loop over first 10 column nd lines
				for (int i = 0; i < sheet.getRows(); i++) {
					Cell cell = sheet.getCell(0, i);
					CellType type = cell.getType();
					System.out.println("getting rows" + cell.getContents());
    
					if (type != CellType.NUMBER) {
						url = cell.getContents();
						if (url.contains("https")){
							domainUrl = url.replaceAll("https://","");
						}
						else{
							domainUrl = url.replaceAll("http://","");
						}
						
						domainUrl = domainUrl.replaceAll("www.","");
						domainUrl = domainUrl.replaceAll("/.*","");		
						// &&checkurl(url,urlresult))
						if (checkdomain(domainUrl,con)){
							
							output.write(url + "\n");
							
						}
						else {
							badoutput.write(url + "___"+ j + "\n");
						}
					}
				}
			}
			if ( output != null ) {
				output.close();
			}
			if ( badoutput != null ) {
				badoutput.close();
			}
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}
	public static void Readfile(String args, Connection con) throws SQLException{
		try (BufferedReader br = new BufferedReader(new FileReader(args)))
		{
			String url;
			String domainUrl;
			File file = new File("/home/qu/Desktop/Cleaned_Arabiclist.txt");
			File badfile = new File("/home/qu/Desktop/Arabic_badlist.txt");
			output = new BufferedWriter(new FileWriter(file));
			badoutput = new BufferedWriter(new FileWriter(badfile));
			while ((url = br.readLine()) != null ){
				if (url.contains("https")){
					domainUrl = url.replaceAll("https://","");
				}
				else {
					domainUrl = url.replaceAll("http://","");
				}
				
				domainUrl = domainUrl.replaceAll("www.","");
				//domainUrl = url.replaceAll("https://www.","");
				domainUrl = domainUrl.replaceAll("/.*","");
				// &&checkurl(url,urlresult))
				if (checkdomain(domainUrl,con)){
					
					output.write(url + "\n");
				}
				else{
					badoutput.write(url + "\n");
				}
			}
			if ( output != null ) output.close();
			if ( badoutput != null ) badoutput.close();
			//truncate the line if it contains "www" or seedlist are urls. anything after.com

		}
		catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static boolean Check_Database_URL(String url, Connection con) throws SQLException{
	        String domainUrl;
			if (url.contains("https")){
				domainUrl = url.replaceAll("https://","");
			}
			else {
				domainUrl = url.replaceAll("http://","");
			}
			
			domainUrl = domainUrl.replaceAll("www.","");
			//domainUrl = url.replaceAll("https://www.","");
			domainUrl = domainUrl.replaceAll("/.*","");
			// &&checkurl(url,urlresult))
			if ((checkdomain(domainUrl,con)) && (checkurl(domainUrl,con))) return true;
		    return false;
	}
	public static boolean Check_Hash_URL(String url){
        String domainUrl;
		if (url.contains("https")){
			domainUrl = url.replaceAll("https://","");
		}
		else {
			domainUrl = url.replaceAll("http://","");
		}
		
		domainUrl = domainUrl.replaceAll("www.","");
		//domainUrl = url.replaceAll("https://www.","");
		domainUrl = domainUrl.replaceAll("/.*","");
		// &&checkurl(url,urlresult))
		if ((Hash_checkdomain(domainUrl)) && (Hash_checkurl(domainUrl))) return true;
	    return false;
}
	public static void Hash_Blacklist(File directory, File directory1){

		//File directory = new File("/Blacklist/Domain");
		for (final File file : directory.listFiles()) {
			Domain_Doc_Array.add(file.getPath());
		}
		//File directory1 = new File("Blacklist/URL");
		for (final File file1 : directory1.listFiles()) {
			URL_Doc_Array.add(file1.getPath());
		}
		try{

			for (int docno = 0; docno <URL_Doc_Array.size(); docno++ ){
				BufferedReader br = new BufferedReader(new FileReader(URL_Doc_Array.get(docno)));
				String line;
				while ((line = br.readLine()) != null){

					Blacklist_Domain.put(line, 0);
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		try{

			for (int docno = 0; docno <Domain_Doc_Array.size(); docno++ ){
				BufferedReader br = new BufferedReader(new FileReader(Domain_Doc_Array.get(docno)));
				String line;
				while ((line = br.readLine()) != null){

					Blacklist_URL.put(line, 0);
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		

	
	}
	public static void CreateBlacklist(){

		try (BufferedReader br = new BufferedReader(new FileReader("/home/qu/Desktop/Blacklist/Domain")))
		{
			String line;
			while ((line = br.readLine()) != null){

				blackListDomainArray.add(line);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(blackListDomainArray); //sorting the blacklist 
		try (BufferedReader br = new BufferedReader(new FileReader("/home/qu/Desktop/Blacklist/URL")))
		{
			String line;
			while ((line = br.readLine()) != null){

				blackListURLArray.add(line);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(blackListURLArray);
	}


	public static boolean checkdomain (String args, Connection con) throws SQLException {
		
		
		PreparedStatement domainstatement = con.prepareStatement("Select count(*) from domains where DOMAIN='"+args+"'");
		
		ResultSet domainresult = domainstatement.executeQuery();

		while(domainresult.next()){
		
			if (!domainresult.getString(1).equals("0")){
				return false;
			}
		}
		return true;
	}
	
public static boolean Hash_checkdomain (String args)  {			
		if (Blacklist_Domain.containsKey(args)) return false;
		return true;
	}
	
public static boolean Hash_checkurl (String args)  {			
	if (Blacklist_URL.containsKey(args)) return false;
	return true;
}


	public static boolean checkurl(String args, Connection con) throws SQLException {
		
PreparedStatement urlstatement = con.prepareStatement("Select count(*) from urls where URL='"+args+"'");
		
		ResultSet urlresult = urlstatement.executeQuery();

		while(urlresult.next()){
		
			if (!urlresult.getString(1).equals("0")){
				return false;
			}
		}
		return true;
		
	}


	public static void main(String[] args) throws Exception{
		//Class.forName("com.mysql.jdbc.Driver");
		//Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "root");
		//Hash_Blacklist();
		System.out.println(Check_Hash_URL("http://www.facebook.com"));
		System.out.println(Check_Hash_URL("http://www.facebook.com/images"));
		System.out.println(Check_Hash_URL("http://www.pornhub.com"));
		System.out.println(Check_Hash_URL("abortbypill.com"));
		System.out.println(Check_Hash_URL("http://www.facebook.com"));
		System.out.println(Check_Hash_URL("http://www.facebook.com/images"));
		//System.out.println(Check_Hash_URL("http://www.facebook.com"));
		//CreateBlacklist();
		//Readfile("/home/qu/Desktop/arabic_pages", con); //for text files
		//Readexcelfile("/home/qu/Desktop/arabic_pages", con );
		// passing a file of blacklisted urls
	}

}