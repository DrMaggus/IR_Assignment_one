package xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "LEWIS")
public class Lewis {
	static class Document {
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
			
			public String getTITLE() {
				return TITLE;
			}
			public String getDATELINE() {
				return DATELINE;
			}
			public String getBODY() {
				return BODY;
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
	
	public ArrayList<Document> getAllDocuments() {
		return Documents;
	}
	
	@Override
	public String toString() {
		return "The XML-File contains: "+Documents.size()+" 'REUTERS-Objects'";
	}
}

/*
 *     // Helper class to convert the date string to a Date object
    private static class DateAdapter extends XmlAdapter<String, LocalDateTime> {

        private static DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm:ss.SS");

        @Override
        public String marshal(LocalDateTime v) throws Exception {
            return DATE_FORMAT.print(v);
        }

        @Override
        public LocalDateTime unmarshal(String v) throws Exception {
            return DATE_FORMAT.parseLocalDateTime(v);
        }
    }
    */
