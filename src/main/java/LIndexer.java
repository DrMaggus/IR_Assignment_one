import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/*
 * Indexinformation, which are needed:
 * NEWID -> ID
 * DATE	 -> Datum
 * TITLE -> Titel
 * BODY  -> Artikel
 */

/* Analyzer:
 * 
 * 
 */


@SuppressWarnings("unused")
public class LIndexer {
	
	Analyzer analyzer;
	IndexWriterConfig config; 
	Directory index;
	List<Document> documents;
	

	
	public LIndexer()
	{
		this.analyzer = new StandardAnalyzer(Version.LUCENE_46);
		this.config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
	}
	
	public void loadDocuments()
	{
		// TODO: Here loading documents into documents list, which is saved internally
		// pattern below can be used:
		/*Document doc = new Document();
		doc.add(new StringField("id",id,Field.Store.YES));
		doc.add(new StringField("date", date, Field.Store.YES));
		doc.add(new TextField("title", title, Field.Store.YES));
		doc.add(new TextField("body", body, Field.Store.YES));*/
		
	}
	
	public void indexing(String FILENAME)
	{	
		File path = new File("src//main//resources//" + FILENAME);
		this.index = new RAMDirectory();
		
		try 
		{
			IndexWriter w = new IndexWriter(index, config);
			for (Document doc : this.documents)
			{
				w.addDocument(doc);
			}
			w.close();	
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/*TODO:
	 * -check if rangequery with regex "date:\\[\\d{8} [tT][oO] \\d{8}\\]"
	 * -TermRangeQuery query = new TermRangeQuery("created_at",lowerDate, upperDate, includeLower, includeUpper);
	 * -put it in the searcher
	 */
	
	public void searchQuery(String querystr)
	{
		int hitCount = 10;
		
		try 
		{
			Query q;
			if (querystr.matches("date:\\[\\d{8} [tT][oO] \\d{8}\\]")){
			    Calendar calendar1 = Calendar.getInstance();
			    Calendar calendar2 = Calendar.getInstance();
			    calendar1.set(Integer.parseInt(querystr.substring(6, 10)), 
			    			  Integer.parseInt(querystr.substring(10, 12)), 
			    			  Integer.parseInt(querystr.substring(12, 14)));
			    calendar2.set(Integer.parseInt(querystr.substring(18, 22)), 
			    			  Integer.parseInt(querystr.substring(22, 24)), 
			    			  Integer.parseInt(querystr.substring(24, 26)));
			    long milliseconds1 = calendar1.getTimeInMillis();
			    long milliseconds2 = calendar2.getTimeInMillis();
			    q = NumericRangeQuery.newLongRange("publish_date",milliseconds1, milliseconds2, true, true);
			}else{
				q = new QueryParser(Version.LUCENE_46, "title", analyzer).parse(querystr);
			}
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitCount, true);
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			System.out.println("Found " + hits.length + " hits.");
			for(int i=0;i<hits.length;++i) 
			{
			    int docId = hits[i].doc;
			    System.out.println(hits[i].score);
			    Document d = searcher.doc(docId);
			    System.out.println((i + 1) + ". " + d.get("id") + "\t" + d.get("title"));
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
}