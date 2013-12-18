import org.apache.lucene.document.Document;

public class Result {
	
	Document doc;
	float score;
	
	public Result(Document doc, float score)
	{
		this.doc = doc;
		this.score = score;
	}
	
	public float getScore() { return this.score; }
	public Document getDocument() { return this.doc; }

}
