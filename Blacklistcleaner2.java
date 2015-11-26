import java.util.Collections;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Blacklistcleaner2 {

	static ArrayList<String> blackListDomainArray = new ArrayList<String>();
	static ArrayList<String> blackListURLArray = new ArrayList<String>();
	static BufferedWriter output = null;
	public static int sort(String s1, String s2) {
		return s1.compareToIgnoreCase(s2);
	}
	public static void Readfile(String args){
		try (BufferedReader br = new BufferedReader(new FileReader(args)))
		{
			String url;
			String domainUrl;
			File file = new File("/home/qu/Desktop/cleaned seedlist.txt");
			output = new BufferedWriter(new FileWriter(file));
			while ((url = br.readLine()) != null ){
				domainUrl = url.replaceAll("http://www.","");
				domainUrl = domainUrl.replaceAll("/.*","");
				if (checkdomain(domainUrl) &&checkurl(url)){
					output.write(url + "\n");
				}
			}
			if ( output != null ) output.close();
			//truncate the line if it contains "www" or seedlist are urls. anything after.com

		}
		catch (IOException e) {
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


	public static boolean checkdomain (String args) {
		int j =0;
		
		while (j< blackListDomainArray.size()){
				int result = sort (args, blackListDomainArray.get(j));
				if (result >0){
					j++;
				}
				else if (result <0){
					//System.out.println(args);
					return true;

				}
				else if (result ==0 ){
					return false;	
				}
			}	
		return true;
	}


	public static boolean checkurl(String args) {
		int j =0;
		int result =0;
		while (j< blackListURLArray.size()){
				result = sort (args, blackListURLArray.get(j));
				if (result >0){
					j++;
				}
				else if (result <0){
					return true;

				}
				else if (result ==0 ){
					return false;	
				}
			}
		System.out.println(result);
		return true;
	}
	
	public static void main(String[] args) {
		CreateBlacklist();
		Readfile("/home/qu/Desktop/Seedlist");
		// passing a file of blacklisted urls
	}

}