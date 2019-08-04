# GroupSpellCheckProject
 
README:
Jonathan Fuchs, Adam Schneider, Melissa Wu
MCIT 591 - Group Assignment - Spell Checker Plus

To run this program, please import all files into your eclipse IDE and run SpellChecker.java. The default dictionary is "engDictionary.txt", and a text
document is needed for the old interface. The new UI can operate without an existing text document.

------

This program is an extension of the Spell Checker project from Week 4, with three key areas of additional functionality:
- Text Formatting
- Analysis
- New UI


Text Formatting

This includes sentence capitalization, punctuation, and line breaks to the checked document. Capitalization and punctuation are adding in based
on the checked document, and line breaks are added at a default of 80 characters, where the user can change this default.

Analysis

The analysis is centered around character, word, and spell checking. Characters, vowels and consonants are all tracked in the vowelAnalysis class,
and SpellingAnalysis tracks word, sentence, syllable, spelling and readability metrics. These are counted during iterations through the document and then summarized for
the user upon completion.

New UI

The new UI is a text document creator and editor that includes the some of the spell checking and analysis that is available to the rest of the program.
Through the new UI, there are file options to start new, open, save and exit the program. Under Edit, there is the ability to copy and paste. Under Format,
users can highlight text and change font size. And finally under review, users can deploy the spell check and readability metrics on the text entered in
the UI.



------

Testing

The tests written for SpellingAnalysis, PatternChecker, and DataToTextFile test if expected values are returned when run on files with known outcomes.
