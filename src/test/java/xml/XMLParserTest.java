package xml;

import java.util.ArrayList;
import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.fail;

import xml.XMLParser;
import xml.Lewis;
/**
 * A few simple tests to check whether the XMLParser works well or not.<br>
 *
 * 
 * @author Markus W.
 * @see xml.XMLParser
 *
 */
public class XMLParserTest {
	
	public static XMLParser parser;
	public final static String path = "src/main/resources/reut2-000.xml";
	
	@BeforeClass
	public static void beforeClass() {
		try {
			parser = new XMLParser(path);
		} catch (JAXBException e) {
			System.out.println(e);
			fail();
		}
	}

	@Test
	public void testParsing() {
		//xml File should contain 1000 'Reuter' objects
		assert(parser.getDocuments().size() == 1000);
		
		//check if a document returned by getNewID has the desired NewID
		for (int i = 1; i < 1000; i++) 
			assert(parser.getDocumentByNewID(i).getNewID() == i);

		//check topics and getUnknown
		for (xml.Lewis.Document d : parser.getDocuments()) {
			if (!d.hasTopics()) 
				assert(d.getTopics().isEmpty());
			assert(d.getUnknown() != null);
		}
			
		
		try {
			parser.reParseFile();
		} catch (JAXBException e) {System.out.println(e);fail();}
		
		//if reParseFile() was successfull => document list should not be null
		assert(parser.getDocuments() != null);
	}
	
	@Test
	public void viewParsedData() {
		//Printing all documents to check all getters
		ArrayList<Lewis.Document> Docs = parser.getDocuments();
		for(Lewis.Document d : Docs) {
			System.out.println(d);
			System.out.println("---------------------------------------------");
		}
	}

}
