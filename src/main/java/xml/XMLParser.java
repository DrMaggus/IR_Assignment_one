package xml;
import java.io.File;
import java.util.ArrayList;
import javax.xml.bind.*;

/**
 * 
 * This class parses a xml file which structure is specified<br>
 * in the xml.Lewis class.
 * 
 * @author Markus W.
 * @see xml.Lewis
 *
 */

public class XMLParser {
	 
	//contains the xml file
	private File xmlFile;
	//JAXB Context and Unmarshaller to parse the String
	private JAXBContext jaxbContext;
	private Unmarshaller jaxbUnmarshaller;
	//contains a list of all documents in xmlFile
	private ArrayList<xml.Lewis.Document> Documents;
	
	/**
	 *  
	 * @param path - Path to xml file
	 * @throws JAXBException
	 */
	public XMLParser(String path) throws JAXBException {
		/*
		 * create File from path
		 * create for jaxb create context and unmarshaller
		 * from a parsed Lewis object return all Documents (xml.Lewis.Document)
		 */
		xmlFile = new File(path);
		jaxbContext = JAXBContext.newInstance(xml.Lewis.class);
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Lewis lewisObj = (Lewis) jaxbUnmarshaller.unmarshal(xmlFile);
		Documents = lewisObj.getAllDocuments();
	}
	
	public void reParseFile() throws JAXBException {
		//Again unmarshall file and save documents in Documents
		Lewis lewisObj = (Lewis) jaxbUnmarshaller.unmarshal(xmlFile);
		Documents = lewisObj.getAllDocuments();
	}
	
	/**
	 * 
	 * @return returns list of all the documents saved in xmlFile
	 */
	public ArrayList<xml.Lewis.Document> getDocuments() {
		return Documents;
	}
	
	/**
	 * 
	 * @param newid - NewID to search for
	 * @return returns document with specific newid.<br>
	 * When not found, null is returned! 
	 */
	public xml.Lewis.Document getDocumentByNewID(int newid) {
		//for each loop to look for valid ID
		for (xml.Lewis.Document item : Documents)
			if (item.getNewID() == newid)
				return item;
		return null;
	}
	
	/**
	 * 
	 * @param oldid - OldID to search for
	 * @return returns document with specific oldid.<br>
	 * When not found, null is returned! 
	 */
	public xml.Lewis.Document getDocumentByOldID(int oldid) {
		//for each loop to look for valid ID
		for (xml.Lewis.Document item : Documents)
			if (item.getOldID() == oldid)
				return item;
		return null;
	}
}
