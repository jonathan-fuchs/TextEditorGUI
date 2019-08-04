README:
Jonathan Fuchs, Adam Schneider, Melissa Wu
MCIT 591 - Group Assignment - Spell Checker Plus

Instructions:
To run this program, please import all files into your eclipse IDE and run SpellChecker.java. The default dictionary
is "engDictionary.txt". Run the SpellChecker.java file. You will be prompted to choose one of the three options. Option 1 is to open
up a GUI where you can write your own text file and open up and edit an existing file. Option 2 is a text analysis that provides readability,
spelling, vowel, and consonant analysis. Option 2 requires an existing text document to run, so you may add your own or use one of the
ones we provided (i.e. mispelled.txt). Option 2 allows you to create your own data table in the form of a text file after inputing your desired
column/row number and data.

Description:
This program is an extension of the Spell Checker project from Week 4, with 4 key areas of additional functionality:

- GUI
- Text Formatting
- Text Analysis
- Data Table Text File

GUI:
The GUI is a text document creator and editor that includes the some of the spell checking and analysis that is available to the
rest of the program.Through the GUI, there are file options to start new, open, save and exit the program. Under Edit, there is the ability
to copy and paste. Under Format, users can highlight text and change font size. And finally under review, users can deploy the spell
check and readability metrics on the text entered in the UI.

Text Formatting:
This includes sentence capitalization, punctuation, and line breaks to the checked document. Capitalization and punctuation are adding in
based on the checked document, and line breaks are added at a default of 80 characters, where the user can change this default. There is
also the ability to create a table and output it as a textfile.

Text Analysis:
The analysis is centered around character, word, and spell checking. Characters, vowels and consonants are all tracked in the vowelAnalysis
class,and SpellingAnalysis tracks word, sentence, syllable, spelling and readability metrics. These are counted during iterations through the
document and then summarized for the user upon completion.

Data Table Text File:
This function creates a data table in the form of a text file by entering the specificed number of rows and columns, as well as the data.
Build up on this would be to create your own CSV file.


Testing:
The tests written for SpellingAnalysis, PatternChecker, and DataToTextFile test if expected values are returned when run on files with known outcomes.