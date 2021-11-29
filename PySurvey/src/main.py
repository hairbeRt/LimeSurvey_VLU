import regex
import data_to_tex
import glob
import os

def get_question_names(survey_data):
    return [regex.search(r'\[(.*)\]',q).group(1) if regex.search(r'\[(.*)\]',q) else q for q in survey_data[0][5:]]

if __name__ == '__main__':
    csv_files = glob.glob("data/*.csv")
    for file in csv_files:
        basic_filename = os.path.basename(file).replace(".csv","")
        data_to_tex.generate_tex(basic_filename.replace("_", " "), "WS 21/22", "template/tex.cfg", "tex_docs/"+str(basic_filename)+".tex", file)