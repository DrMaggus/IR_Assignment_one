package main;
import indexing.LIndexer;
import indexing.Result;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

import org.apache.lucene.document.Document;

/**
 * 
 * @author Markus W., Julez, Matthias E.
 *
 */
public class Main {
	public static void main(String args[]){
		//instantiate the IR-System 'LIndexer', a Scanner for keyboard input, a list for the results and a searchString
		LIndexer indexer = new LIndexer();
		Scanner scanner = new Scanner(System.in);
		ArrayList<Result> results = new ArrayList<Result>();
		String searchString = "";
		
		//reading file from memory and parsing xml
		System.out.print("Reading Documents ...");
		indexer.loadDocuments();
		System.out.println(" done");
		
		//creating index from documents
		System.out.print("Constructing index ...");
		indexer.indexing();
		System.out.println(" done\n");
		
		//reading search term from keyboard
		System.out.println("Please enter your search term below:");
		while(searchString.split(":")[0].isEmpty()) {
			System.out.print("=> ");
			searchString = scanner.nextLine();
			if (searchString.split(":")[0].isEmpty()){
				System.out.println("You cannot search for an empty string!");
			}
		}
		
		//get results and display them on the console
		results = indexer.searchQuery(searchString);
		System.out.println("\n--RESULTS--");
		
		printRanking(results, scanner);
	
		System.out.println("Quitting Program...");
	}
	
	public static void printRanking(ArrayList<Result> results, Scanner scanner) {
		int rank = 0;
		int detailView = 0;
		
		//Display the ranking
		for(Result result : results) {
			rank++;
			Document doc = result.getDocument();
			System.out.println(rank+".  (Score: "+result.getScore()+")  Title: "+doc.get("title").replace("\n", "")+"");
		}
		
		//Print out all information until 'quit' is typed in
		while(true){
			System.out.println("\nIf you want a more detailed view of a result type in the rank number:");
			while (detailView < 1 || detailView > rank) {
				try {
					System.out.print("=> ");
					detailView = scanner.nextInt(); //read in user choice
				} catch (InputMismatchException e) {
					//if input was not a integer => check if it was quit
					String in = scanner.next();
					detailView = 0;
					if (in.toLowerCase().equals("quit"))
						return;
				}
				if (detailView < 1 || detailView > rank)
					System.out.println("Please enter a valid number!");
			}
			System.out.println(detailView+".  (Score: "+results.get(detailView-1).getScore()+")  Title: "+results.get(detailView-1).getDocument().get("title").replace("\n", ""));
			System.out.println("Date: "+makeDateFormat(results.get(detailView-1).getDocument().get("date")));
			System.out.println("Content:\n"+results.get(detailView-1).getDocument().get("body"));
			detailView = 0; //reset choice
		}
	}
	
	/**
	 * 
	 * @param s - "Datestring" format: yyyyddmm
	 * @return returns Date in dd.mm.yyyy format
	 */
	private static String makeDateFormat(String s) {
		return s.substring(6)+"."+s.substring(4, 6)+"."+s.substring(0,4);
	}
}
