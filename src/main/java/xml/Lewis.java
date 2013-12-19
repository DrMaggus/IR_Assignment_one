package xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is the pattern or the so called Context class<br>
 * for the JAXB XML parser. XML with the structure of reut2-000.xml<br>
 * to find in src/main/resource ist parsed into Objects derived from this class.
 * 
 * @author Markus W
 * @see xml.XMLParser
 *
 */

//Each child node in the xml file is mapped to an inner class of
//the root-class. 
//If a child node contains just data and no further child nodes it's
//content is saved in the respective class attribute labeled
//with the annotation @XmlElement

//The Annotations are needed by the JAXB parser to know how to 
//save a certain node from the xml file to an object of this class

//Each class has its own getter to get the data from the Lewis object
//after parsing the xml file

@XmlRootElement(name = "LEWIS") //root node of xml file
public class Lewis {
	static public class Document {
		static class Topics {
			@XmlElement
			private ArrayList<String> D;

			public ArrayList<String> getD() {
				return D == null ? new ArrayList<String>() : D;
			}
		}
		static class Places {
			@XmlElement
			private ArrayList<String> D;
			
			public ArrayList<String> getD() {
				return D == null ? new ArrayList<String>() : D;
			}
		}
		static class Text {
			@XmlElement
			private String TITLE;
			@XmlElement
			private String DATELINE;
			@XmlElement
			private String BODY;
			@XmlAnyElement
			@XmlMixed
			private ArrayList<String> VALUES;
			@XmlAttribute(name="TYPE")
			private String TYPE;
			
			public String getTITLE() {
				return TITLE;
			}
			public String getDATELINE() {
				return DATELINE;
			}
			public String getBODY() {
				String str = "";
				if(TYPE != null && TYPE.equals("BRIEF"))
					if (VALUES != null)
						for(String s : VALUES)
							str += s;
				return BODY == null ? str : BODY;
			}
		}		
		@XmlElement
		private Topics TOPICS;
		@XmlElement
		private Places PLACES;
		@XmlElement
		private String DATE;
		@XmlElement
		private String UNKNOWN;
		@XmlElement
		private Text TEXT;
		
		@XmlAttribute(name="OLDID")
		private int OLDID;
		@XmlAttribute(name="NEWID")
		private int NEWID;
		@XmlAttribute(name="TOPICS")
		private String hasTOPICS;
		
		public String getDate() {
			return DATE;
		}
		public ArrayList<String> getTopics() {
			return TOPICS.getD();
		}
		public ArrayList<String> getPlaces() {
			return PLACES.getD();
		}
		public String getUnknown() {
			return UNKNOWN;
		}
		public String getTitle() {
			return TEXT.getTITLE();
		}
		public String getDateline() {
			return TEXT.getDATELINE();
		}
		public String getBody() {
			return TEXT.getBODY();
		}
		public int getOldID() {
			return OLDID;
		}
		public int getNewID() {
			return NEWID;
		}	
		public boolean hasTopics() {
			return hasTOPICS.equals("YES") ? true : false;
		}
		@Override
		public String toString() {
			String str = "";
			str += "NEWID: "+getNewID()+"\n";
			str += "OLDID: "+getOldID()+"\n";
			str += "DATE: "+getDate()+"\n";
			str += "Dateline: "+getDateline()+"\n";
			str += "TITLE: "+getTitle()+"\n";
			str += "Places:\n";
			for(String _str : getPlaces())
				str += "---"+_str+"\n";
			if (hasTopics()) {
				str += "Topics:\n";
				for(String _str : getTopics())
					str += "---"+_str+"\n";
			} else {str += "No-Topics";}
			return str;
		}
	}

	@XmlElement(name="REUTERS")
	private ArrayList<Document> Documents;
	
	/**
	 * 
	 * @return returns all 'Reuter' objects
	 */
	public ArrayList<Document> getAllDocuments() {
		return Documents;
	}
	
	@Override
	public String toString() {
		return "The XML-File contains: "+Documents.size()+" 'REUTERS-Objects'";
	}
}
