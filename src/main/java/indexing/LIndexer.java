package indexing;

import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import xml.XMLParser;

/**
 * 
 * @author Julez
 * This Class provides functionality for constructing index and requesting queries.
 */
public class LIndexer {
	
	private Analyzer analyzer;
	private IndexWriterConfig config; 
	private Directory index;
	private List<Document> documents;
	private XMLParser parser;
	private ArrayList<xml.Lewis.Document> lewisDocs;
	
	private static String FILENAME = "reut2-000.xml";
	
	/**
	 * Constructing LuceneIndex using Version LUCENE_46
	 */
	public LIndexer()
	{
		this.analyzer = new StandardAnalyzer(Version.LUCENE_46);
		this.config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
	}
	
	/**
	 * Load Documents into index.
	 */
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
				Integer id = doc.getNewID();
				indexDoc.add(new StringField("id", id.toString(), Field.Store.YES));
				if(doc.getDate() != null)
				{
					indexDoc.add(new StringField("date", parseDate(doc.getDate()), Field.Store.YES));
				}
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
	
	/**
	 * Parse the date into necessary form.
	 * @param xml date format
	 * @return lucene date format
	 */
	private String parseDate(String date)
	{
		StringBuilder day = new StringBuilder();
		StringBuilder month = new StringBuilder();
		StringBuilder year = new StringBuilder();
		
		int c = 0;
		while(date.charAt(c) != '-')
		{
			day.append(date.charAt(c));
			c++;
		}
		if(day.length() == 1)
		{
			day.insert(0, '0');
		}
		for(int i = 0; i < day.length(); i++)
		{
			if(day.charAt(i) == ' ')
			{
				day.deleteCharAt(i);
				day.insert(i, '0');
			}
		}
		while(day.length() > 2)
		{
			day.deleteCharAt(0);
		}
		c++;
		while(date.charAt(c) != '-')
		{
			month.append(date.charAt(c));
			c++;
		}
		month = new StringBuilder(this.monthTable(month.toString()));
		c++;
		while(date.charAt(c) != ' ')
		{
			year.append(date.charAt(c));
			c++;
		}
		return year.append(month.append(day)).toString();
	}
	
	/**
	 * LookUpTable for MonthAbbreviations.
	 * @param month abbreviation
	 * @return month number
	 */
	private String monthTable(String month)
	{
		if(month.equals("JAN"))
			return "01";
		if(month.equals("FEB"))
			return "02";
		if(month.equals("MAR"))
			return "03";
		if(month.equals("APR"))
			return "04";
		if(month.equals("MAY"))
			return "05";
		if(month.equals("JUNE"))
			return "06";
		if(month.equals("JULY"))
			return "07";
		if(month.equals("AUG"))
			return "08";
		if(month.equals("SEPT"))
			return "09";
		if(month.equals("OCT"))
			return "10";
		if(month.equals("NOV"))
			return "11";
		if(month.equals("DEC"))
			return "12";
		
		return "00";
		
	}
	
	/**
	 * Constructing index into memory.
	 */
	public void indexing()
	{	
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

	/**
	 * QueryRequest
	 * @param querystr
	 * @return ArrayList with Results
	 */
	public ArrayList<Result> searchQuery(String querystr)
	{
		int hitCount = 10;
		
		try 
		{
			BooleanQuery booleanQuery = new BooleanQuery();
			//regex matches all rangequeries
			if (querystr.matches(".*:\\[\\d{8} [tT][oO] \\d{8}\\]")){
				//split after the ":"
				String[] temp = querystr.split(":");
				List<String> queryArray = new ArrayList<String>();
				for (int i=0; i<temp.length; i++){
					queryArray.add(temp[i]);
					}
				//if there are ":"s in the searchterm, concat the wrong splitted strings and restore the ":"s
				while (queryArray.size() > 2){
					queryArray.set(0, queryArray.get(0).concat(":").concat(queryArray.get(1)));
					queryArray.remove(1);					
				}
				//searchterm
				String query = queryArray.get(0);
				//Strings to ints for comparison
				int intDate1 = Integer.parseInt(queryArray.get(1).substring(1,9));
				int intDate2 = Integer.parseInt(queryArray.get(1).substring(13,21));
				//back to Strings after comparison
				String stringDate1 = Integer.toString(min(intDate1, intDate2));
				String stringDate2 = Integer.toString(max(intDate1, intDate2));
			    //add to booleanQuery with "BooleanClause.Occur.MUST" to create an AND
				booleanQuery.add(TermRangeQuery.newStringRange("date", stringDate1 , stringDate2, true, true),BooleanClause.Occur.MUST);
				booleanQuery.add(new QueryParser(Version.LUCENE_46, "title", analyzer).parse(query),BooleanClause.Occur.MUST);
			}else{
				//If normal, do normal
				booleanQuery.add(new QueryParser(Version.LUCENE_46, "title", analyzer).parse(querystr),BooleanClause.Occur.MUST);
			}
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitCount, true);
			searcher.search(booleanQuery, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			ArrayList<Result> results = new ArrayList<Result>();
			for(int i=0;i<hits.length;++i) 
			{
			    int docId = hits[i].doc;
			    Document d = searcher.doc(docId);
			    results.add(new Result(d, hits[i].score));
			}
			return results;
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	private int min(int x, int y){
		return (x < y) ? x : y;
	}
	private int max(int x, int y){
		return (x > y) ? x : y;
	}
}