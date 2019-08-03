import java.util.ArrayList;

public class SpellingAnalysis {
	private int wordCount = 0;
	private int spellCheckedWords = 0;
	private int wordsFromSuggestion = 0;
	private int wordsFromManualEntry = 0;
	private int wordsAccepted = 0;
	private ArrayList<Integer> sentenceLengths = new ArrayList<Integer>();
	private int wordsInCurrentSentence = 0;
	private ArrayList<Integer> syllableCounts = new ArrayList<Integer>();
	
	private double percentChecked;
	private double percentWrong;
	private double percentFromSuggestion;
	private double percentFromManualEntry;
	private double percentAccepted;
	
	private double readingEase;
	private double readingGradeLevel;
	
	
	public void spellingAnalysisSuite() {
		System.out.println("Here is some exciting analysis on your document: ");
		System.out.println();
		spellingPercentBreakdown();
		approxReadability();
	}
	
	public void spellingPercentBreakdown() {
		double wrongWords = (double)this.wordsFromSuggestion + this.wordsFromManualEntry;
		
		this.percentChecked = (double)this.spellCheckedWords / this.wordCount;
		this.percentWrong = wrongWords / this.wordCount;
		this.percentFromSuggestion = (double) this.wordsFromSuggestion / wrongWords;
		this.percentFromManualEntry = (double) this.wordsFromManualEntry / wrongWords;
		this.percentAccepted = (double) this.wordsAccepted / this.wordCount;
		
		System.out.println();
		System.out.println("Spelling Breakdown");
		System.out.println("------------------");
		System.out.println();
		System.out.println("Words Flagged by Spell Check: " + Double.toString(this.percentChecked) + "%");
		System.out.println("Words Approved over Spell Check: " + Double.toString(this.percentAccepted) + "%");
		System.out.println("Words Spelled Incorrectly: " + Double.toString(this.percentWrong) + "%");
		System.out.println("Misspelled Words Corrected By Suggestion: " + Double.toString(this.percentFromSuggestion) + "%");
		System.out.println("Misspelled Words Corrected By Manual Entry: " + Double.toString(this.percentFromManualEntry) + "%");
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
		 * formula for Flesch Reading Ease readability:
		 *  206.835 – (1.015 x ASL) – (84.6 x ASW)
		 *  
		 *  The formula for the Flesch-Kincaid Grade Level score is:
		 *  (.39 x ASL) + (11.8 x ASW) – 15.59 
		 *  
		 *  where:
		 *  ASL = average sentence length (the number of words divided by the number of sentences)
		 *  ASW = average number of syllables per word (the number of syllables divided by the number of words)
		 */
		double sentenceLen = 0;
		double syllablePerWord = 0;
		double aveSentenceLen;
		double aveSyllablePerWord;
		
		for (int sentence : this.sentenceLengths) {
			sentenceLen += sentence;
		}
		aveSentenceLen = sentenceLen / this.getSentenceLengths().size();
		
		for (int syllables : this.syllableCounts) {
			syllablePerWord += syllables;
		}
		aveSyllablePerWord = syllablePerWord / this.getSyllableCounts().size();
		
		this.readingEase = 206.835 - (1.015 * aveSentenceLen) - (84.6 * aveSyllablePerWord);
		this.readingGradeLevel = (.39 * aveSentenceLen) + (11.8 * aveSyllablePerWord) - 15.59;
		
		System.out.println();
		System.out.println("Readability Metrics");
		System.out.println("-------------------");
		System.out.println();
		System.out.println("The Flesch Reading Ease score for this text is: " + readingEase);
		System.out.println("This rates out of 100-points and a standard score is between 60-70");
		System.out.println();
		System.out.println("The Flesch Kincaid Grade Level score for this text is: " + readingGradeLevel);
		System.out.println("This gives the grade level with a standard score between 7-8");
		System.out.println();
	}
	
	/**
	 * There is no easy algorithm for determining the number of syllables in a word. For this rough calculation,
	 * using the following:
	 * Count vowels
	 * Remove double vowels
	 * Also adds the count to the ArrayList, to determine the average of syllables in readability
	 * @param word to determine syllables
	 */
	public void getSyllablesInWord(String word) {
		word = word.toLowerCase();
		char[] lettersInWord = word.toCharArray();
		boolean lastLetterVowel = false;
		int syllableCount = 0;
		
		
		for (char letter : lettersInWord) {
			if (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u' || letter == 'y') {
				if (!lastLetterVowel) {
					syllableCount++;
					lastLetterVowel = true;
				}
				else {
					lastLetterVowel = false;
				}
			}
			else {
				lastLetterVowel = false;
			}
		}
		this.syllableCounts.add(syllableCount);
	}
	
	
	public void getSentenceLength() {
		this.sentenceLengths.add(wordsInCurrentSentence);
		wordsInCurrentSentence = 0;
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
	
	public void setWordsFromSuggestion(int wordsFromSuggestion) {
		this.wordsFromSuggestion = wordsFromSuggestion;
	}

	public void setWordsFromManualEntry(int wordsFromManualEntry) {
		this.wordsFromManualEntry = wordsFromManualEntry;
	}

	public void setWordsAccepted(int wordsAccepted) {
		this.wordsAccepted = wordsAccepted;
	}

	public void setSentenceLengths(ArrayList<Integer> sentenceLengths) {
		this.sentenceLengths = sentenceLengths;
	}

	public int getWordsFromSuggestion() {
		return wordsFromSuggestion;
	}

	public int getWordsFromManualEntry() {
		return wordsFromManualEntry;
	}

	public int getWordsAccepted() {
		return wordsAccepted;
	}

	public ArrayList<Integer> getSentenceLengths() {
		return sentenceLengths;
	}

	public ArrayList<Integer> getSyllableCounts() {
		return syllableCounts;
	}

	public int getWordsInCurrentSentence() {
		return wordsInCurrentSentence;
	}

	public void setWordsInCurrentSentence(int wordsInCurrentSentence) {
		this.wordsInCurrentSentence = wordsInCurrentSentence;
	}



	
	
}
