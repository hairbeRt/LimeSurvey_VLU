import java.util.LinkedList;
import java.lang.StringBuilder;

public class SurveyTextObject extends SurveyObject{
	private String question;
	private LinkedList<String> answers;
	
	public SurveyTextObject(String question) {
		this.question = question;
		answers = new LinkedList<String>();
	}
	
	public void addAnswer(String answer) {
		this.answers.addLast(answer);
	}
	
	public String getName() {
		return this.question;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("SurveyTextObject[");
		str.append(question);
		str.append("][\n");
		for(String answer: answers) {
			str.append(answer);
			str.append("\n");
		}
		str.append("]");
		return str.toString();
	}
	
	//TODO
	public String toTex() {
		StringBuilder str = new StringBuilder();
		str.append("\n\\subsection{" + this.question + "}\n");
		for(String answer : this.answers) {
			str.append("\\framedText{" + answer.replace("\"\"", "'' ").replace("\\", "").replace("%", "\\%").replace("_", "\\_").replace("$", "\\$").replace("#", "\\#").replace("~", "\\~").replace("^", "\\^") + "}\n\n");
		}
		str.append("\n");
		return str.toString();
	}
}
