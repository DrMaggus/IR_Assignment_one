package xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.lucene.util.ToStringUtils;

@XmlRootElement(name = "LEWIS")
public class Lewis {
	static class Reuter {
		String DATE;

		public String getDATE() {
			return DATE;
		}
		public void setDATE(String dATE) {
			DATE = dATE;
		}
	}

	@XmlElement
	private Reuter REUTERS;
	
	public String toString() {
		return REUTERS.getDATE();
	}
	
	
}
