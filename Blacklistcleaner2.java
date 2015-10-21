import java.util.Collections;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Blacklistcleaner2 {

	public static int sort(String s1, String s2) {
		return s1.compareToIgnoreCase(s2);
	}


	public static void printer () {
		int j =0;
		BufferedWriter output = null;

		ArrayList<String> blackListArray = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader("/home/qu/Desktop/blacklist")))
		{
			String line;
			while ((line = br.readLine()) != null){

				blackListArray.add(line);	

			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		Collections.sort(blackListArray); //sorting the blacklist
		try (BufferedReader br = new BufferedReader(new FileReader("/home/qu/Desktop/Seedlist")))
		{
			String line;
			File file = new File("/home/qu/Desktop/cleaned seedlist.txt");
			output = new BufferedWriter(new FileWriter(file));
			while ((line = br.readLine()) != null ){
				//truncate the line if it contains "www" or seedlist are urls. anything after.com
				line = line.replaceAll("www.", "");
					
				if (j >= blackListArray.size()){
					output.write(line + "\n");
				}
				else {
					int result = sort (line, blackListArray.get(j));
					if (result >0){
						j++;
					}
					else if (result <0){
						output.write(line + "\n");

					}
					else if (result ==0 ){
						j++;	
					}
				}

			}
			if ( output != null ) output.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) {

		printer(); // passing a file of blacklisted urls
	}

}