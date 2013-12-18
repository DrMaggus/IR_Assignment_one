import java.util.Scanner;
import java.util.ArrayList;

import org.apache.lucene.document.Document;


public class Main {
	public static void main(String args[]){
		LIndexer indexer = new LIndexer();
		Scanner scanner = new Scanner(System.in);
		ArrayList<Result> results = new ArrayList<Result>();
		int rank = 1;
		int detailView = -1;
		
		System.out.print("Reading Documents ...");
		indexer.loadDocuments();
		System.out.println(" done");
		
		System.out.print("Constructing index ...");
		indexer.indexing();
		System.out.println(" done\n");
		
		System.out.println("Please enter your search term below:");
		System.out.print("=> ");
		String searchString = scanner.nextLine();
		results = indexer.searchQuery(searchString);
		System.out.println("\n--RESULTS--");
		for(Result result : results) {
			Document doc = result.getDocument();
			System.out.println(rank+".  (Score: "+result.getScore()+")  Title: "+doc.get("title").replace("\n", "")+"");
			rank++;
		}
		System.out.println("\nIf you want a more detailed view of a result type in the rank number:");
		while (detailView < 1 || detailView > rank) {
			System.out.print("=> ");
			detailView = scanner.nextInt();
			if (detailView < 1 || detailView > rank)
				System.out.println("Please enter a valid number!");
		}
		System.out.println(detailView+".  (Score: "+results.get(detailView).getScore()+")  Title: "+results.get(detailView).getDocument().get("title").replace("\n", ""));
		System.out.println("Date: "+results.get(detailView).getDocument().get("date"));
		System.out.println("Content:\n"+results.get(detailView).getDocument().get("body"));
		/*
		System.out.println("--- QueryRequest ---");
		System.out.println("QueryString: BAHIA-COCOA-REVIEW");
		indexer.searchQuery("BAHIA-COCOA-REVIEW");
		System.out.println(" -> finished");
		
		System.out.println("--- QueryRequest ---");
		System.out.println("QueryString:   Nevertheless, BankAmerica, which holds about 2.70 billion dlrs in Brazilian loans");
		indexer.searchQuery("  Nevertheless, BankAmerica, which holds about 2.70 billion dlrs in Brazilian loans");
		System.out.println(" -> finished");
		
		System.out.println("--- QueryRequest ---");
		System.out.println("QueryString: 26-FEB-1987 15:01:01.79");
		indexer.searchQuery("26-FEB-1987");
		System.out.println(" -> finished");
		
		System.out.println("--- QueryRequest ---");
		System.out.println("QueryString: 12");
		indexer.searchQuery("12");
		System.out.println(" -> finished");
		
		System.out.println("--- QueryRequest ---");
		System.out.println("QueryString: 12");
		indexer.searchQuery("date:[19701210 TO 20000101]");
		System.out.println(" -> finished");
		*/
	}
}
