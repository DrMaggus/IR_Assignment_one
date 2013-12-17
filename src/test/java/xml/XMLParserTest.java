package xml;

import java.util.ArrayList;

import javax.xml.bind.JAXBException;

//import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import xml.XMLParser;
import xml.Lewis;

public class XMLParserTest {
	
	public static XMLParser parser;
	public final static String path = "src/main/resources/reut2-000.xml";
	
	@BeforeClass
	public static void beforeClass() {
		try {
			parser = new XMLParser(path);
		} catch (JAXBException e) {
			System.out.println(e);
		}
	}

	@Test
	public void testParsing() {
		assert(parser.getDocuments().size() == 1000);
		
		for (int i = 1; i < 1000; i++) 
			assert(parser.getDocumentByNewID(i).getNewID() == i);

		//does not work vice versa <=> wrong attribute in xml!
		for (xml.Lewis.Document d : parser.getDocuments())
			if (!d.hasTopics()) 
				assert(d.getTopics().isEmpty());
	}
	
	@Test
	public void viewParsedData() {
		ArrayList<Lewis.Document> Docs = parser.getDocuments();
		for(Lewis.Document d : Docs) {
			System.out.println(d);
			System.out.println("---------------------------------------------");
		}
	}

}
