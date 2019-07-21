README:
Jonathan Fuchs
MCIT 591 - Assignment 4 - Spell Checker

In order to run this program, please import all files into your eclipse IDE and run SpellChecker.java.

------

In order to improve the similarity metric algorithm, I would also give weight to shared sequences of consecutive letters. 
For example, if the typo was "testt" and the intended word was "test", the rightSimilarity score would only be 1, 
whereas both share the string "tset" (reading right-to-left).

I would also allow for the suggested words to include words with apostrophes. I designed my code to have an isolate-able for-loop responsible 
for removing words with apostrophes (as commented in the code); in an improved version of this code, I would remove that for-loop.

Finally, the getWordSuggestions and getWordsWithCommonLetters methods both require the comparison of sets. My suggestion for improving the design 
as-given would be to also specify a helper method for comparing sets that would be used by both of the other two methods.