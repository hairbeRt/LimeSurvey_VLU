import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.util.Locale;
import java.util.regex.*;
import java.io.PrintWriter;


public class Script {

	public static void main(String[] args) throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException{
		LectureSurvey currSurvey;
		PrintWriter texOut;
		for(File f : new File("csv_docs/").listFiles()) {
			System.out.println(f.getName());
			currSurvey = new LectureSurvey(f.getName().replace(".csv", ""));
			for(SurveyObject o : readFile(f)) {
				currSurvey.add(o);
			}
			texOut = new PrintWriter("tex_docs/" + f.getName().replace(".csv", ".tex"), "UTF-8");
			texOut.print(currSurvey.toTex());
			texOut.close();
		}
	}

	public static LinkedList<SurveyObject> readFile(File f) {
		try {
			String tempstr;
			LinkedList<SurveyObject> surveyResult = new LinkedList<SurveyObject>();
			Scanner fileIn = new Scanner(f, "UTF-8");
			fileIn.useLocale(Locale.GERMANY);

			while (fileIn.hasNextLine()) {
				tempstr = fileIn.nextLine();
				// Matche für Anfang von Antwortenblock
				if (Pattern.matches(".*Zusammenfassung für G[0-9]+Q[0-9]+.*", tempstr)) {
					if (tempstr.contains("[")) {
						// Tabellenfrage
						Matcher nameMatch = Pattern.compile(".*\\[(.*)\\].*").matcher(tempstr);
						nameMatch.find();
						SurveyTableObject currSurv = new SurveyTableObject(nameMatch.group(1));
						surveyResult.addLast(currSurv);
						while (!tempstr.contentEquals("Antwort|Anzahl|Prozent") && fileIn.hasNextLine()) {
							tempstr = fileIn.nextLine();
						}
						if(fileIn.hasNextLine()) {
							tempstr = fileIn.nextLine();
						}
						while (!tempstr.contentEquals("||") && fileIn.hasNextLine()) {
							nameMatch = Pattern.compile("(.*)\\|([0-9]+)\\|.*").matcher(tempstr);
							nameMatch.find();
							String answer = nameMatch.group(1);
							if (answer.contains("(")) {
								answer = answer.substring(0, answer.lastIndexOf('('));
							}
							int amount = Integer.parseInt(nameMatch.group(2));
							if (!answer.contentEquals("Nicht beendet oder nicht gezeigt")) {
								currSurv.addChoice(answer.trim(), amount);
							}
							tempstr = fileIn.nextLine();

						}
					} else {
						// Textfrage
						SurveyTextObject currSurv = new SurveyTextObject(fileIn.nextLine().replace("||", ""));
						surveyResult.addLast(currSurv);
						while (!tempstr.contentEquals("ID|Antwort|") && fileIn.hasNextLine()) {
							tempstr = fileIn.nextLine();
						}
						if(fileIn.hasNextLine()) {
							tempstr = fileIn.nextLine();
						}
						while (!tempstr.contentEquals("||") && fileIn.hasNextLine()) {
							if(Pattern.matches("[0-9]+\\|\".*", tempstr)) {
								while(!tempstr.contains("|\"") || !tempstr.contains("\"|")) {
									tempstr += "\n";
									tempstr += fileIn.nextLine();
								}
							}
							tempstr = tempstr.substring(tempstr.indexOf('|')+1,tempstr.lastIndexOf('|'));
							if(tempstr.charAt(0) == '\"') {
								tempstr = tempstr.substring(1,tempstr.length()-1);
							}
							currSurv.addAnswer(tempstr);
							tempstr = fileIn.nextLine();
						}
					}
				}
			}

			fileIn.close();
			return surveyResult;
		} catch (java.io.FileNotFoundException e) {
			System.out.println("File could not be found!");
			return null;
		}
	}
}
