import java.util.HashMap;
import java.lang.StringBuilder;

public class LectureSurvey {
	private static String SEMESTER = "Wintersemester 20/21";
	
	private final String name;
	private HashMap<String,SurveyObject> questions;
	
	public LectureSurvey(String name) {
		this.name = name;
		this.questions = new HashMap<String,SurveyObject>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void add(SurveyObject o) {
		this.questions.put(o.getName(), o);
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		for(SurveyObject o : this.questions.values()) {
			result.append(o);
			result.append("\n");
		}
		return result.toString();
	}
	
	public String toTex() {
		StringBuilder result = new StringBuilder();
		//Preamble
		result.append(
				"\\documentclass[a4paper]{scrartcl}\r\n\\usepackage[utf8]{inputenc}\r\n\\usepackage[ngerman]{babel}\r\n\\usepackage{graphicx}\r\n%\\usepackage{array}\r\n\\usepackage{enumitem}\r\n\\usepackage{listings}\r\n\\usepackage{color}\r\n\\usepackage{pdfpages}\r\n\\usepackage{tabularx}\r\n\r\n\\usepackage[fleqn]{amsmath}\r\n\\usepackage{amssymb}\r\n\r\n\\usepackage{geometry}\r\n\\geometry{margin=2cm}\r\n\r\n\\setlength{\\parskip}{0.5em}\r\n\\setlength{\\parindent}{0em}\r\n\r\n\\usepackage[colorlinks=true]{hyperref}\r\n\r\n\\usepackage{pgfplots}\r\n\\pgfplotsset{compat=1.8}\r\n\\pgfplotsset{\r\n    ybar,\r\n    xtick=data,\r\n    nodes near coords,\r\n    nodes near coords align={vertical},\r\n    width=\\linewidth,\r\n    height=7.5em,\r\n    xticklabel style={align=center, text width=2cm},\r\n}\r\n\r\n\\pagestyle{empty}\r\n\r\n\\newcommand{\\he}[1]{\\textbf{\\small #1}}\r\n\\newcommand{\\tableHeadTypEins}{ & \\he{nie} & \\he{selten} & \\he{manchmal} & \\he{meistens} & \\he{immer} & \\he{nicht anwendbar} \\\\\\hline}\r\n\\newcommand{\\tableHeadTypZwei}{ & \\he{nie} & \\he{selten} & \\he{manchmal} & \\he{meistens} & \\he{immer} \\\\\\hline}\r\n\\newcommand{\\framedText}[1]{\\noindent\\framebox{\\begin{minipage}{\\linewidth}#1\\end{minipage}}}\r\n\\def\\arraystretch{1.33}\r\n\r\n%---------------------------------------------------------------------------------------------------\r\n\r\n\\newcommand{\\qBarChartQuestion}[1]{\\begin{minipage}{.215\\linewidth}#1\\end{minipage}\\hspace{.02\\linewidth}}\r\n\r\n\\newcommand{\\setScalaNewerToAlways}{\\pgfplotsset{symbolic x coords={nie ,selten ,manchmal ,meistens ,immer ,nicht anwendbar ,}, ylabel={\\#votes}}}\r\n\r\n\\newenvironment{qBarChart}[1]{\\qBarChartQuestion{ #1}\\begin{minipage}{.765\\linewidth}\\begin{tikzpicture}\\setScalaNewerToAlways{}\\tikzstyle{every node}=[font=\\small]}{\\end{tikzpicture}\\end{minipage}}\r\n\r\n\r\n\r\n\\newcommand{\\rBarChartQuestion}[1]{\\begin{minipage}{.15\\linewidth}#1\\end{minipage}\\hspace{.02\\linewidth}}\r\n\r\n\\newcommand{\\setScalaBadToGood}{\\pgfplotsset{symbolic x coords={Wurde nicht eingesetzt ,sehr schlecht ,schlecht ,befriedigend ,gut ,sehr gut ,}, ylabel={\\#votes},}}\r\n\r\n\\newenvironment{rBarChart}[1]{\\rBarChartQuestion{ #1}\\begin{minipage}{.83\\linewidth}\\begin{tikzpicture}\\setScalaBadToGood{}\\tikzstyle{every node}=[font=\\small]}{\\end{tikzpicture}\\end{minipage}}\r\n\r\n\r\n");
		
		//Kopfzeilen
		result.append("\\begin{document}\r\n\r\n\\begin{tabularx}{\\linewidth}{|X|l|}\\hline\r\n\\he{Modul:} & \\he{Semester:} \\\\\r\n{\\Large " + this.name +" } &\r\n{\\Large " + SEMESTER + "    } \\\\\\hline\r\n\\end{tabularx}\r\n\r\n\\vspace{1em}\\noindent\r\nErgebnis der Online-VLU.\r\nDie Umfrage fand in den letzten beiden Vorlesungswochen statt.\n\n");
		//Bewertung der Vorlesung
		result.append("%==============================================================================\r\n\\section{Bewertung der Vorlesung}\r\n%==============================================================================\n");
		result.append(questions.get("Wie oft hast du die Vorlesung besucht?").toTex());
		result.append(questions.get("Wurden Themen durch Beispiele veranschaulicht?").toTex());
		result.append(questions.get("Wurden die Themen ausführlich genug erklärt?").toTex());
		result.append(questions.get("War die Struktur der Vorlesung klar zu erkennen?").toTex());
		result.append(questions.get("Waren die Folien/das Skript hilfreich?").toTex());
		
		//Bewertung der Dozierenden
		result.append("%==============================================================================\r\n\\section{Bewertung der Dozierenden}\r\n%==============================================================================\n");
		result.append(questions.get("Die Geschwindigkeit der Vorlesung war...").toTex());
		result.append(questions.get("Wie viel verstehst du während der Vorlesung?").toTex());
		result.append(questions.get("Ist der Dozent/die Dozentin gut auf Fragen eingegangen?").toTex());
		result.append(questions.get("War der Dozent/die Dozentin außerhalb der Vorlesung für Fragen etc. erreichbar?").toTex());
		result.append(questions.get("War die Dozentin / der Dozent akustisch gut zu verstehen?").toTex());
		
		//Bewertung des Moduls
		result.append("%==============================================================================\r\n\\section{Bewertung des Moduls}\r\n%==============================================================================\n");
		result.append(questions.get("Helfen die verlangten Studienleistungen, das Modul erfolgreich abzuschließen?").toTex());
		result.append(questions.get("Findest du die verlangten Studienleistungen für dieses Modul angemessen?").toTex());
		result.append(questions.get("Würdest du das Modul weiterempfehlen?").toTex());
		result.append(questions.get("Der Praxisbezug war...").toTex());
		result.append(questions.get("Ist der Arbeitsaufwand für dieses Modul im Hinblick auf die LP-Zahl angemessen?").toTex());
		result.append(questions.get("Dein Interesse für dieses Thema ist...").toTex());
		result.append(questions.get("Wie viele Stunden hast du insgesamt, inkl. Vorlesung, Übung, Übungsaufgaben\\dots, pro Woche für dieses Modul aufgewendet?").toTex());
		
		//Bewertung der Übungsaufgaben
		result.append("%==============================================================================\r\n\\section{Bewertung der Übungsaufgaben}\r\n%==============================================================================\n");
		result.append(questions.get("Wie oft hast du die Übungen besucht?").toTex());
		result.append(questions.get("Wurden die Übungsaufgaben rechtzeitig zur Verfügung gestellt?").toTex());
		result.append(questions.get("Die Schwierigkeit der Übungsblätter schwankte...").toTex());
		result.append(questions.get("Die Vorlesung war...").toTex());
		result.append(questions.get("Die Übungsgruppe war...").toTex());
		result.append(questions.get("Die Übungsaufgaben waren meistens...").toTex());
		
		//Bewertung des Tutoriums
		result.append("%==============================================================================\r\n\\section{Bewertung des Tutoriums}\r\n%==============================================================================\n");
		result.append(questions.get("War der Tutor/die Tutorin außerhalb der Übung für Fragen etc. erreichbar?").toTex());
		result.append(questions.get("Waren die Korrekturen des Tutors/der Tutorin nachvollziehbar?").toTex());
		result.append(questions.get("Wurde der Tutor/die Tutorin mit dem Stoff der Übung fertig?").toTex());
		result.append(questions.get("Lohnt sich der Besuch des Tutoriums?").toTex());
		
		//Abschließende Bewertung
		result.append("%==============================================================================\r\n\\section{Abschließende Bewertung des Moduls}\r\n%==============================================================================\n");
		result.append(questions.get("Note:").toTex());
		
		//Freitextkommentare
		result.append("%==============================================================================\r\n\\section{Freitextkommentare}\r\n%==============================================================================\n");
		result.append(questions.get("Was hat dir an dieser Lehrveranstaltung gefallen?").toTex());
		result.append(questions.get("Was könnte noch besser gemacht werden?").toTex());
		result.append(questions.get("Hier hast du Platz für weitere Anmerkungen und Feedback zum Modul.").toTex());
		result.append(questions.get("Hier hast du Platz für Anmerkungen und Feedback zur Umfrage.").toTex());
		//---
		result.append("\\end{document}");
		return result.toString();
	}
}
