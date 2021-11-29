import java.util.LinkedList;
import java.lang.StringBuilder;

class Choice {
	String name;
	int amount;
	
	Choice(String name, int amount) {
		this.name = name;
		this.amount = amount;
	}
}

public class SurveyTableObject extends SurveyObject{
	private String question;
	private LinkedList<Choice> answers;
	
	public SurveyTableObject(String question){
		this.question = question;
		this.answers = new LinkedList<Choice>();
	}
	
	public void addChoice(String name, int amount) {
		answers.addLast(new Choice(name, amount));
	}
	
	public String getName() {
		return this.question;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("SurveyTableObject[");
		str.append(question);
		str.append("][\n");
		for(Choice ch: answers) {
			str.append("Choice(");
			str.append(ch.name);
			str.append(",");
			str.append(ch.amount);
			str.append(")\n");
		}
		str.append("]");
		return str.toString();
	}
	
	//TODO
	public String toTex() {
		StringBuilder str = new StringBuilder();
		str.append("\n");
		//Wide graph for final grade
		if(this.question.contentEquals("Note:")) {
			//Question name
			str.append("\\begin{minipage}{.1\\linewidth}" + this.question + "\\end{minipage}\\hspace{.02\\linewidth}\n");
			//Choices as coordinates
			str.append("\\begin{minipage}{.9\\linewidth}\r\n    \\begin{tikzpicture}\r\n        \\pgfplotsset{symbolic x coords={");
		} else {
			//Question name
			str.append("\\begin{minipage}{.25\\linewidth}" + this.question + "\\end{minipage}\\hspace{.02\\linewidth}\n");
			//Choices as coordinates
			str.append("\\begin{minipage}{.8\\linewidth}\r\n    \\begin{tikzpicture}\r\n        \\pgfplotsset{symbolic x coords={");
		}
		for(Choice c : answers) {
			//Escaping commas, with optimization for G03Q11
			if(!c.name.contains(",")) {
				str.append(c.name + " ,");
			} else {
				System.out.println(c.name);
				str.append(("\\text{" + c.name + "} ,").replace("Stunden", "h").replace("\u221E", "$\\infty$"));
			}
		}
		str.append("}, ylabel={\\#votes}}\n");
		//Actual plot with choices
		str.append("        \\tikzstyle{every node}=[font=\\small]\r\n        \\begin{axis}[bar width=1cm]\r\n            \\addplot coordinates {  ");
		for(Choice c : answers) {
			//Escaping commas for tikz, with optimization for G03Q11 
			if(!c.name.contains(",")) {
				str.append("(" + c.name + " ," + c.amount + ") ");
			} else {
				str.append(("(\\text{" + c.name + "} ," + c.amount + ") ").replace("Stunden", "h").replace("\u221E", "$\\infty$"));
			}
		}
		str.append("};\r\n        \\end{axis}\r\n    \\end{tikzpicture}\r\n\\end{minipage}\n");
		
		return str.toString();
		
	}
}
