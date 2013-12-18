
public class Main {
	public static void main(String args[]){
		LIndexer indexer = new LIndexer();
		
		System.out.println("--- reading documents ---");
		indexer.loadDocuments();
		System.out.println("-> finished");
		
		System.out.println("--- constructing index ---");
		indexer.indexing("index");
		System.out.println(" -> finished");
		
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
		indexer.searchQuery("BAHIA-COCOA-REVIEW:[20000101 TO 19701210]");
		System.out.println(" -> finished");
		
	}
}
