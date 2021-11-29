import csv
import regex
from functools import lru_cache

def get_data(fname):
    with open(str(fname), newline='', encoding='utf-8') as csvfile:
        reader = csv.reader(csvfile, delimiter='\t')
        data = [row for row in reader]
    return data

def get_question_names(survey_data):
    return [regex.search(r'\[(.*)\]',q).group(1) if regex.search(r'\[(.*)\]',q) else q for q in survey_data[0]]

def section_to_tex(section_name, data):
    return "%==============================================================================\n\\section{"+ str(section_name) + "}\n%==============================================================================\n"

def text_to_tex(question_label, data):
    #Find index of question label
    k = data[0].index(question_label)
    texts = []
    #Subsection with question label
    texts.append(
        "\n\\subsection{" + str(question_label) + "}\n"
    )
    #If found, one text box for every non-empty answer
    if k:
        for text_part in [data[l][k] for l in range(1, len(data)-1) if data[l]]:
            if text_part:
                texts.append(
                    "\\framedText{"+str(tex_cleanup(text_part))+"}\n"
                )
    else:
        return "Frage nicht gefunden: " + str(question_label)
    return ''.join(texts)

@lru_cache(maxsize=128)
def grade_sorting_key(q):
    #TODO dieses Dictionary in einen Key-file auslagern. Hat in Code eigentlich nichts zu suchen.
    key_dictionary = {
        "nie":0,
        "selten":1,
        "manchmal":2,
        "meistens":3,
        "immer":4,
        "ja":5,
        "eher ja":6,
        "neutral":7,
        "eher nein":8,
        "nein":9,
        "zu schnell":10,
        "schnell":11,
        "genau richtig":12,
        "langsam":13,
        "zu langsam":14,
        "Alles":15,
        "das Meiste":16,
        "die Hälfte":17,
        "wenig":18,
        "nichts":19,
        "groß":20,
        "eher groß":21,
        "mittel":22,
        "eher gering":23,
        "gering":24,
        "zu hoch":25,
        "hoch":26,
        "angemessen":27,
        "niedrig":28,
        "zu niedrig":29,
        "stark gestiegen":30,
        "etwas gestiegen":31,
        "gleich geblieben":32,
        "etwas gesunken":33,
        "stark gesunken":34,
        "[0,3) Stunden":35,
        "[3,6) Stunden":36,
        "[6,8) Stunden":37,
        "[8,10) Stunden":38,
        "[10,12) Stunden":39,
        "[12,∞) Stunden":40,
        "nicht":41,
        "schwach":42,
        "mittelmäßig":43,
        "stark":44,
        "sehr stark":45,
        "weit voraus":46,
        "etwas voraus":47,
        "gleichauf":48,
        "etwas hinterher":49,
        "weit hinterher":50,
        "viel zu groß":51,
        "etwas zu groß":52,
        "genau richtig":53,
        "etwas zu klein":54,
        "viel zu klein":55,
        #Cf. "angemessen"
        "zu schwierig":25,
        "schwierig":26,
        "einfach":28,
        "zu einfach":29,
        "Ja":56,
        "Nein":58,
        "Unsicher":57,
        "sehr gut (1)":59,
        "gut (2)":60,
        "befriedigend (3)":61,
        "ausreichend (4)":62,
        "mangelhaft (5)":63,
        "ungenügend (6)":64,
        "Ja, sicher":65,
        "Unsicher oder nein":66,

        "nicht anwendbar":99,
        "Keine Antwort":100
    }
    if q in key_dictionary:
        return key_dictionary[q]
    return 0

def grade_to_tex(grade_label, data):
    #Find index of question label
    k = data[0].index(grade_label)
    answer_histogram = {}
    answer_histogram["nicht anwendbar"] = 0
    answer_histogram["Keine Antwort"] = 0
    if k:
        for answer in [data[l][k] for l in range(1, len(data)-1) if data[l]]:
            if answer:
                if answer in answer_histogram:
                    answer_histogram[answer] += 1
                else:
                    answer_histogram[answer] = 1
            else:
                answer_histogram["Keine Antwort"] += 1
    else:
        return "Frage nicht gefunden: " + str(grade_label)
    keys_sorted = [key for key in answer_histogram]
    keys_sorted.sort(key=lambda Q: grade_sorting_key(Q))

    grade_source = []
    grade_source.append(
        "\\begin{minipage}{.25\\linewidth}" +str(grade_label)+"\\end{minipage}\\hspace{.02\\linewidth}\n\\begin{minipage}{.8\\linewidth}\n    \\begin{tikzpicture}\n        \\pgfplotsset{symbolic x coords={"
    )
    for key in keys_sorted:
        grade_source.append(
            tex_cleanup(key) + " ,"
        )
    grade_source.append(
        "}, ylabel={\\#votes}}\n        \\tikzstyle{every node}=[font=\\small]\n        \\begin{axis}[bar width=1cm]\n            \\addplot coordinates { "
    )
    for key in keys_sorted:
        grade_source.append(
            "(" + tex_cleanup(key) + " ," + str(answer_histogram[key]) + ") "
        )
    grade_source.append(
        "};\n        \\end{axis}\n    \\end{tikzpicture}\n\\end{minipage}\n\n"
    )
    return ''.join(grade_source)

def tex_cleanup(s: str):
    return s.replace(",","{,}").replace("∞", "$\\infty$").replace("%", "\\%").replace("\"", "\'\'").replace("<","$<$").replace(">","$>$")

def generate_tex(modul_name, semester, format_cfg, target_path, source_file):
    data = get_data(source_file)
    #Clean up question names
    data[0] = get_question_names(data)
    #As list of strings that get joined. By contract, end line at end of each string.
    result_tex_source = []

    #Add Preamble
    result_tex_source.append(
        "\\documentclass[a4paper]{scrartcl}\n\\usepackage[utf8]{inputenc}\n\\usepackage[ngerman]{babel}\n\\usepackage{graphicx}\n%\\usepackage{array}\n\\usepackage{enumitem}\n\\usepackage{listings}\n\\usepackage{color}\n\\usepackage{pdfpages}\n\\usepackage{tabularx}\n\n\\usepackage[fleqn]{amsmath}\n\\usepackage{amssymb}\n\n\\usepackage{geometry}\n\\geometry{margin=2cm}\n\n\\setlength{\\parskip}{0.5em}\n\\setlength{\\parindent}{0em}\n\n\\usepackage[colorlinks=true]{hyperref}\n\n\\usepackage{pgfplots}\n\\pgfplotsset{compat=1.8}\n\\pgfplotsset{\n    ybar,\n    xtick=data,\n    nodes near coords,\n    nodes near coords align={vertical},\n    width=\\linewidth,\n    height=7.5em,\n    xticklabel style={align=center, text width=2cm},\n}\n\n\\pagestyle{empty}\n\n\\newcommand{\\he}[1]{\\textbf{\\small #1}}\n\\newcommand{\\tableHeadTypEins}{ & \\he{nie} & \\he{selten} & \\he{manchmal} & \\he{meistens} & \\he{immer} & \\he{nicht anwendbar} \\\\\\hline}\n\\newcommand{\\tableHeadTypZwei}{ & \\he{nie} & \\he{selten} & \\he{manchmal} & \\he{meistens} & \\he{immer} \\\\\\hline}\n\\newcommand{\\framedText}[1]{\\noindent\\framebox{\\begin{minipage}{\\linewidth}#1\\end{minipage}}}\n\\def\\arraystretch{1.33}\n\n%---------------------------------------------------------------------------------------------------\n\n\\newcommand{\\qBarChartQuestion}[1]{\\begin{minipage}{.215\\linewidth}#1\\end{minipage}\\hspace{.02\\linewidth}}\n\n\\newcommand{\\setScalaNewerToAlways}{\\pgfplotsset{symbolic x coords={nie ,selten ,manchmal ,meistens ,immer ,nicht anwendbar ,}, ylabel={\\#votes}}}\n\\newenvironment{qBarChart}[1]{\\qBarChartQuestion{ #1}\\begin{minipage}{.765\\linewidth}\\begin{tikzpicture}\\setScalaNewerToAlways{}\\tikzstyle{every node}=[font=\\small]}{\\end{tikzpicture}\\end{minipage}}\n\n\n\n\\newcommand{\BarChartQuestion}[1]{\\begin{minipage}{.15\\linewidth}#1\\end{minipage}\\hspace{.02\\linewidth}}\n\n\\newcommand{\\setScalaBadToGood}{\\pgfplotsset{symbolic x coords={Wurde nicht eingesetzt ,sehr schlecht ,schlecht ,befriedigend ,gut ,sehr gut ,}, ylabel={\\#votes},}}\n\n\\newenvironment{rBarChart}[1]{\BarChartQuestion{ #1}\\begin{minipage}{.83\\linewidth}\\begin{tikzpicture}\\setScalaBadToGood{}\\tikzstyle{every node}=[font=\\small]}{\\end{tikzpicture}\\end{minipage}}\n\n\n"
    )

    #Kopfzeilen
    result_tex_source.append(
        "\\begin{document}\n\n\\begin{tabularx}{\\linewidth}{|X|l|}\\hline\n\\he{Modul:} & \\he{Semester:} \\\\\n{\\Large " + str(modul_name) +" } &\n{\\Large " + str(semester) + "    } \\\\\\hline\n\\end{tabularx}\n\n\\vspace{1em}\\noindent\nErgebnis der Online-VLU.\nDie Umfrage fand in den letzten beiden Vorlesungswochen statt.\n\n"
    )

    command_functions = {
        "SECTION": section_to_tex,
        "GRADE": grade_to_tex,
        "TEXT": text_to_tex
    }
    with open(format_cfg, encoding="utf-8") as layout_commands:
        for line in layout_commands:
            command = line.partition(' ')
            result_tex_source.append(command_functions[command[0]](command[2].strip()[1:-1], data)+"\n")
            

    #End of document
    result_tex_source.append(
        "\\end{document}"
    )
    f_out = open(target_path, "w", encoding="utf-8")
    f_out.write(''.join(result_tex_source))
    f_out.close()