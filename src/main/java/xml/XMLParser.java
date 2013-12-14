package xml;
import java.io.File;
import java.io.InputStream;

import javax.xml.bind.*;


public class XMLParser {
	public static void main(String[] args) throws JAXBException {
		File file = new File("src/main/resources/reut2-000.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(Lewis.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Lewis Robj = (Lewis) jaxbUnmarshaller.unmarshal(file);
		System.out.println(Robj);
	}
}
