
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
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

import xml.XMLParser;

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
	
	private Analyzer analyzer;
	private IndexWriterConfig config; 
	private Directory index;
	private List<Document> documents;
	private XMLParser parser;
	private ArrayList<xml.Lewis.Document> lewisDocs;
	
	private static String FILENAME = "reut2-000.xml";
	
	public LIndexer()
	{
		this.analyzer = new StandardAnalyzer(Version.LUCENE_46);
		this.config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
	}
	
	public void loadDocuments()
	{
		
		try
		{
			this.parser = new XMLParser("src//main//resources//" + FILENAME);
			this.lewisDocs = this.parser.getDocuments();
			this.documents = new ArrayList<Document>();
			
			for (xml.Lewis.Document doc : lewisDocs) 
			{
				Document indexDoc = new Document();
				Integer tmp = doc.getNewID();
				if(tmp.toString() != null)
					indexDoc.add(new StringField("id",tmp.toString(),Field.Store.YES));
				if(doc.getDate() != null)
					indexDoc.add(new StringField("date", doc.getDate(), Field.Store.YES));
				if(doc.getTitle() != null)
					indexDoc.add(new TextField("title", doc.getTitle(), Field.Store.YES));
				if(doc.getBody() != null)
					indexDoc.add(new TextField("body", doc.getBody(), Field.Store.YES));
				this.documents.add(indexDoc);	
			}
		}
		catch(JAXBException e)
		{
			e.printStackTrace();
		}
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
			if (querystr.matches(".*:\\[\\d{8} [tT][oO] \\d{8}\\]")){
				String[] temp = querystr.split(":");
				List<String> queryArray = new ArrayList<String>();
				for (int i=0; i<temp.length; i++){
					queryArray.add(temp[i]);
					}
				while (queryArray.size() > 2){
					queryArray.set(0, queryArray.get(0).concat(":").concat(queryArray.get(1)));
					queryArray.remove(1);					
				}
				String query = queryArray.get(0);
				String date1 = queryArray.get(1).substring(1,9);
				String date2 = queryArray.get(1).substring(13,21);
			    //q = NumericRangeQuery.newLongRange("publish_date",milliseconds1, milliseconds2, true, true);
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
			    Document d = searcher.doc(docId);
			    System.out.println((i + 1) + ". " + d.get("id") + "\t" + d.get("title") + "\nScore: " + hits[i].score);
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
}