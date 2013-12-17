package xml;
import java.io.File;
import java.util.ArrayList;
import javax.xml.bind.*;


public class XMLParser {
	
	private File xmlFile;
	private JAXBContext jaxbContext;
	private Unmarshaller jaxbUnmarshaller;
	private ArrayList<xml.Lewis.Document> Documents;
	
	public XMLParser(String path) throws JAXBException {
		xmlFile = new File(path);
		jaxbContext = JAXBContext.newInstance(xml.Lewis.class);
		jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Lewis lewisObj = (Lewis) jaxbUnmarshaller.unmarshal(xmlFile);
		Documents = lewisObj.getAllDocuments();
	}
	
	public void reParseFile() throws JAXBException {
		Lewis lewisObj = (Lewis) jaxbUnmarshaller.unmarshal(xmlFile);
		Documents = lewisObj.getAllDocuments();
	}
	
	public ArrayList<xml.Lewis.Document> getDocuments() {
		return Documents;
	}
	
	public xml.Lewis.Document getDocumentByNewID(int i) {
		for (xml.Lewis.Document item : Documents)
			if (item.getNewID() == i)
				return item;
		return null;
	}
	
	public xml.Lewis.Document getDocumentByOldID(int i) {
		for (xml.Lewis.Document item : Documents)
			if (item.getOldID() == i)
				return item;
		return null;
	}
	
	public static void main(String[] args) throws JAXBException {
		XMLParser ll = new XMLParser("src/main/resources/reut2-000.xml");
		for(Lewis.Document reut : ll.getDocuments()){
			System.out.println("NID: "+reut.getNewID());
			System.out.println("OID: "+reut.getOldID());
			System.out.println("DATE: "+reut.getDate());
			System.out.println("Dateline: "+reut.getDateline());
			System.out.println("TITLE: "+reut.getTitle());
			System.out.println("Places:");
			for(String str : reut.getPlaces())
				System.out.println("---"+str);
			if (reut.hasTopics()) {
				System.out.println("Topics:");
				for(String str : reut.getTopics())
					System.out.println("---"+str);
			} else {System.out.println("No-Topics");}
			System.out.println("-------------------------------------------------");
		}
		ll.reParseFile();
		System.out.println(ll.getDocumentByNewID(991).getNewID());
	}
}
