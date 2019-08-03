import java.util.*;
import java.io.*;

/**
 * Class for creating Dictionary object from input dictionary file.
 * 
 * <p> Contains two instance variables: a HashMap sorted by dictionary words, and a HashMap sorted by word length.
 * The first is used for exact match lookups, the second is used for looking up similar words.
 * 
 * @param str filename of input dictionary file
 * @author Jonathan Fuchs
 * @version 1.0
 * @since 1.0
 */
public class Dictionary {

	/*
	 * wordDictionary HashMap keys are dictionary words (used for exact match lookups) .
	 * wordByLengthDictionary HashMap keys are word-lengths, used for WordRecommender.getWordSuggestions() lookups.
	 */
	private HashMap<String, String> wordDictionary;
	private HashMap<Integer, ArrayList<String>> wordByLengthDictionary;
	
	/**
	 * Constructor for Dictionary class
	 * 
	 * @param fileName name of dictionary file
	 */
	
	public Dictionary (String fileName) {
		this.wordDictionary = new HashMap<>();
		this.wordByLengthDictionary = new HashMap<>();
		File dictionary = new File(fileName);
		try {
			Scanner scanner = new Scanner(dictionary);
			while (scanner.hasNextLine()) {
				String word = scanner.nextLine();
				addWordToDictionaries(word);			
			}	
			scanner.close();
		} catch (FileNotFoundException e) {
			//TODO create dictionary file from backupdictionary file
			System.out.println("No dictionary file present!");
			e.printStackTrace();
		}	
	}
	
	/**
	 * Helper method to add a word to the user dictionaries
	 * 
	 * @param word word to be added to the dictionaries
	 */
	public void addWordToDictionaries(String word) {
		wordDictionary.put(word, word);
		if (wordByLengthDictionary.get(word.length()) != null) {
			wordByLengthDictionary.get(word.length()).add(word);
		}
		else {
			ArrayList<String> seed = new ArrayList<String>();
			seed.add(word);
			wordByLengthDictionary.put(word.length(), seed);
		}		
	}
	
	/**
	 * Helper method for returning wordDictionary instance HashMap.
	 * @return wordDictionary
	 */
	public HashMap<String, String> getWordDictionary() {
		return wordDictionary;
	}

	/**
	 * Helper method for returning wordByLengthDictionary instance HashMap.
	 * @return wordByLengthDictionary
	 */
	public HashMap<Integer, ArrayList<String>> getWordByLengthDictionary() {
		return wordByLengthDictionary;
	}
}
