
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class Blacklistcleaner {

	public static int sort(String s1, String s2) {
		//System.out.println(s1);
		//System.out.println(s2);
		return s1.compareToIgnoreCase(s2);
	}
	

	public static void printer (String args) {


		ArrayList<String> seedListArray = new ArrayList();
		try (BufferedReader br = new BufferedReader(new FileReader("/home/qu/Desktop/Seedlist")))
		{
			String line;
			while ((line = br.readLine()) != null){
				//truncate the line if it contains "www" or anything after.com
				line = line.replaceAll("www.", "");
				line = line.replaceAll("/.*", "");
				seedListArray.add(line);	
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		Collections.sort(seedListArray); //sorting the seedlist

		
		ArrayList<String> blackListDomainfolder= new ArrayList<String>();
		
		File directory = new File(args);
		for (final File file : directory.listFiles()) {
			//System.out.println(file.getName());
			blackListDomainfolder.add("/home/qu/Desktop/Blacklist/" + file.getName());
		}
		
		for (int a =0 ; a < blackListDomainfolder.size(); a ++){
			//System.out.println(blackListDomainfolder.size());
			ArrayList<String> removeSeed = new ArrayList<String>();
			ArrayList<String> blackListArray = new ArrayList<String>();
			try (BufferedReader br = new BufferedReader(new FileReader(blackListDomainfolder.get(a))))
			{
				String line;
				while ((line = br.readLine()) != null){
					blackListArray.add(line);	
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			} 
			//System.out.println(blackListDomainfolder.get(a));
			int i ,j ;
			i =0; j=0;
		
			//System.out.println("i :" + i);
			//System.out.println("j: "+ j);
			//iterates over the list in the blacklist domain files
			while (i < seedListArray.size() && j < blackListArray.size()){
				/*System.out.println("i" + i + "j" + j + "a"+a);
				System.out.println("seedlistword" + seedListArray.get(i));
				System.out.println("seedlist size" + seedListArray.size());
				System.out.println("blacklistword" + blackListArray.get(j));*/
				int result = sort (seedListArray.get(i), blackListArray.get(j));
				//System.out.println(result);
				if (result >0){
					j++;
				}
				else if (result <0){	 
					i++; 
				}
				else if (result ==0 ){
					removeSeed.add(seedListArray.get(i));
					//System.out.println(seedListArray.get(i));
					i++; j++;		
				}


			}
			seedListArray.removeAll(removeSeed);//cleaning seedlist
		}
		


		// write the final seed list to a text file

		BufferedWriter output = null;
		try {
			File file = new File("/home/qu/Desktop/cleaned seedlist.txt");
			output = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < seedListArray.size(); i++) {
				output.write(seedListArray.get(i).toString()+ "\n");
			}
			if ( output != null ) output.close();

		} catch ( IOException e ) {
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) {

		printer("/home/qu/Desktop/Blacklist"); // passing a directory which contains files of blacklists sorted by categories
	}

}