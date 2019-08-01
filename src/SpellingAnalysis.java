import java.util.ArrayList;

public class SpellingAnalysis {
	private int wordCount = 0;
	private int spellCheckedWords = 0;
	private int wordsFromSuggestion = 0;
	private int wordsFromManualEntry = 0;
	private int wordsAccepted = 0;
	private ArrayList<Integer> sentenceLengths = new ArrayList<Integer>();
	private ArrayList<Integer> syllableCounts = new ArrayList<Integer>();
	
	
	public void spellingAnalysisSuite() {
		System.out.println("Here is some exciting analysis on the spelling in your document: ");
		System.out.println();
		spellingPercentBreakdown();
	}
	
	public void spellingPercentBreakdown() {
		double wrongWords = (double)this.wordsFromSuggestion + this.wordsFromManualEntry;
		
		double percentChecked = (double)this.spellCheckedWords / this.wordCount;
		double percentWrong = wrongWords / this.wordCount;
		double percentFromSuggestion = (double) this.wordsFromSuggestion / wrongWords;
		double percentFromManualEntry = (double) this.wordsFromManualEntry / wrongWords;
		double percentAccepted = (double) this.wordsAccepted / this.wordCount;
		
		System.out.println("Words Flagged by Spell Check: " + percentChecked + "%");
		System.out.println("Words Spelled Incorrectly: " + percentWrong + "%");
		System.out.println("Misspelled Words Corrected By Suggestion: " + percentFromSuggestion + "%");
		System.out.println("Misspelled Words Corrected By Manual Entry: " + percentFromManualEntry + "%");
		System.out.println();
	}
	
	
	/**
	 * based on some publicly available resource, how does spelling in document compare?
	 * Possibly grade level? Possibly compared to news sources? 
	 */
//	public void spellingComparison() {
//		
//	}
	
	
	public void approxReadability() {
		/**
		 * formula for readability:
		 *  206.835 – (1.015 x ASL) – (84.6 x ASW)
		 *  where:
		 *  ASL = average sentence length (the number of words divided by the number of sentences)
		 *  ASW = average number of syllables per word (the number of syllables divided by the number of words)
		 */
		
		
	}
	
	/**
	 * There is no easy algorithm for determining the number of syllables in a word. For this calculation,
	 * using the following:
	 * Count vowels
	 * Remove double vowels
	 * Disregard vowels at end of word
	 * Also adds the count to the ArrayList, to determine the average of syllables in readability
	 * @param word to determine syllables
	 */
	public void getSyllablesInWord(String word) {
		word = word.toLowerCase();
		char[] lettersInWord = word.toCharArray();
		
		
		for (char letter : lettersInWord) {
			//if (letter == aeiou || letter == aeiou || letter == aeiou || letter == aeiou ||) {
				
			//}
		}
	}
	
	public int getSentenceLength() {
		return 0;
	}
	
	
	
	
	public int getWordCount() {
		return wordCount;
	}
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	public int getSpellCheckedWords() {
		return spellCheckedWords;
	}
	public void setSpellCheckedWords(int spellCheckedWords) {
		this.spellCheckedWords = spellCheckedWords;
	}
	
	
	
//	public SpellingAnalysis() {
//		
//	}
//	
//	public double percentWordsSpelledCorrectly() {
//		
//	}
//	
//	public double percentWordedMisspelledInDocument() {
//		
//	}
//	
//	public double percentWordsRequiringManualEntry() {
//		
//	}
//	
//	public double percentWordsFromSuggestions() {
//		
//	}
//	
public int totalWordsMisspelled() {
	return 0;
}


	
	
}
