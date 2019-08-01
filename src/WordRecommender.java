import java.util.*;

/**
 * Class for spell-checking words and providing alternate word suggestions.
 * 
 * @author Jonathan Fuchs
 * @version 1.0
 * @since 1.0
 */
public class WordRecommender {

	/*
	 * fileName is the name of the file for the dictionary of words
	 */
	private String fileName;
	private Dictionary dictionary;
	private HashMap<String, String> dictionaryWords;
	private HashMap<Integer, ArrayList<String>> dictionaryByWordLength;
	
	/**
	 * Constructor for WordRecommender class 
	 * 
	 * @param fileName name of dictionary file
	 */
	
	public WordRecommender (String fileName) {
		this.fileName = fileName;
		this.dictionary = new Dictionary(fileName);
		updateDictionaries();
	}
	
	/**
	 * Method for determining similarity metric score. Used by getWordSuggestions method
	 * 
	 * @param word1 first word to be checked for similarity score
	 * @param word2 second word to be checked for similarity score
	 * @return similarity score
	 */
	
	public double getSimilarityMetric (String word1, String word2) {
		
		int leftSimilarity = 0;
		int rightSimilarity = 0;
		double similarityScore = 0.0;
		
		int shortestWordLength = word1.length();
		if (word1.length() > word2.length()) { shortestWordLength = word2.length();}
		for (int i = 0; i < shortestWordLength; i++) {
			if(word1.charAt(i) == word2.charAt(i)) {
				leftSimilarity++;
			}
			if(word1.charAt(word1.length() - i - 1) == word2.charAt(word2.length() - i - 1)) {
				rightSimilarity++;
			}
		}
		
		similarityScore = (leftSimilarity + rightSimilarity)/2.0;

		return similarityScore;
	}
	
	/**
	 * Method for finding alternative suggested words to replace a misspelled word.
	 * 
	 * <p> allCandidateWordsSortedByScore is a nested TreeMap. TreeMaps were chosen for their auto-sorting property.
	 * 
	 * <p> The keys for the outer TreeMap are the similarity metric score, and is "reverse order" sorted such that larger scores are earlier keys.
	 * The values for the outer TreeMap are the inner TreeMaps, detailed immediately below. 
	 * 
	 * <p> The keys for each inner TreeMap are individual words mapped to these same words 1-to-1. For example, the key "oblong" maps to the value "oblong". 
	 * The TreeMap's natural ordering sorts these inner TreeMap keys alphabetically.
	 *  
	 * <p> An example structure might be: 
	 * {2.0={attest=attest, beets=beets, beset=beset, best=best, detest=detest, jest=jest, lest=lest, meets=meets, nest=nest, pest=pest, reset=reset, rest=rest, sheet=sheet, 
	 * sheets=sheets, sleet=sleet, steeds=steeds, steels=steels, steeps=steeps, steers=steers, sweet=sweet, sweets=sweets, teas=teas, tells=tells, ten's=ten's, tens=tens, 
	 * tents=tents, tests=tests, texts=texts, theses=theses, toe's=toe's, tree's=tree's, trees=trees, vest=vest, west=west, zest=zest}, 
	 * 1.5={beet's=beet's, besets=besets, 
	 * bets=bets, detests=detests, east=east, esteems=esteems, gets=gets, jets=jets, lets=lets, mets=mets, nets=nets, pets=pets, reseter=reseter, resets=resets, seat=seat, 
	 * sect=sect, seethe=seethe, sent=sent, sets=sets, setters=setters, stem's=stem's, step's=step's, street=street, stress=stress, teases=teases, teethes=teethes, tenses=tenses, 
	 * testers=testers, ties=ties, toes=toes, tress's=tress's, tresses=tresses, wets=wets}, 
	 * 1.0={asset=asset, asset's=asset's, assets=assets, attests=attests, bests=bests, bet's=bet's, eats=eats, estates=estates, jests=jests, jet's=jet's, let's=let's, metes=metes, 
	 * nests=nests, net's=net's, pests=pests, rests=rests, seats=seats, sects=sects, seethes=seethes, set=set, set's=set's, settles=settles, steed=steed, steel=steel, steep=steep, 
	 * steer=steer, steerer=steerer, stems=stems, steps=steps, stews=stews, streets=streets, taste=taste, test's=test's, tested=tested, tester=tester, text's=text's, tosses=tosses, 
	 * vests=vests}, 
	 * 0.5={estate=estate, esteem=esteem, rester=rester, sect's=sect's, setter=setter, settle=settle, site's=site's, state's=state's, states=states, stem=stem, 
	 * step=step, stew=stew, tastes=tastes}, 
	 * 0.0={easts=easts, sate=sate, sates=sates, site=site, sites=sites, state=state}}
	 * 
	 * @param word source word for which alternative words are to be suggested
	 * @param n the number of letters +/- for words to be considered as alternate suggestions 
	 * @param commonPercent the minimum percent of unique letters to be shared by the source word and potential alternate words, from 0.0 to 1.0 
	 * @param topN the number of alternate suggested words to be returned. 
	 * Note: the number of suggestions may exceed this number if multiple words share the same similarity score as the topNth word
	 * @return list of suggested alternative words
	 */
	
	public ArrayList<String> getWordSuggestions (String word, int n, double commonPercent, int topN){
		ArrayList<String> candidateWords = new ArrayList<String>();		
		TreeMap <Double, TreeMap <String, String>> allCandidateWordsSortedByScore = new TreeMap<Double, TreeMap <String, String>>(Comparator.reverseOrder());		
		ArrayList<String> output = new ArrayList<String>();
		Set<String> s2 = uniqueLetters(word);
		// Note: if topN <= 0, function might break. Rather than throwing an error, I just set topN to 1. 
		// getWordSuggestions is an internal method, so this should be acceptable. 
		// However, assignment instructions did not specify for this case.
		if (topN <= 0) {
			topN = 1;
		}
		
		/*
		 * Adds to candidateWords all the words (from the word-length dictionary) of lengths differing between +/- n from the input word 
		 */
		for (int i = word.length() - n; i <= word.length() + n; i++) {
			if (i > 0 && dictionaryByWordLength.get(i) != null) {
				candidateWords.addAll(dictionaryByWordLength.get(i));
			}
		}
		
		/*
		 * Loops through each of the words in the candidateWords list. Finds the intersection and the union of the letters from the original word and the current candidate word.
		 * The intersection is divided by the union for the candidatePercent. If the candidatePercent is greater than or equal to the commonPercent specified in the method call, 
		 * then the word is added to the TreeMap, along with it's similarity metric score.
		 */
		for (String candidateWord : candidateWords) {
			Set<String> s1 = uniqueLetters(candidateWord);
			Set<String> unionOfSets = new HashSet<String>();
			Set<String> intersectionOfSets = new HashSet<String>();
			unionOfSets.addAll(s1);
			unionOfSets.addAll(s2);
			for (String element : unionOfSets) {
				if (s1.contains(element) && s2.contains(element)) {
					intersectionOfSets.add(element);
				}
			}			
			double candidatePercent = (1.0 * intersectionOfSets.size()) / unionOfSets.size();			
			
			if (candidatePercent >= commonPercent) {
				double currentSimMetric = getSimilarityMetric(word, candidateWord);
				
				if (allCandidateWordsSortedByScore.keySet() == null || allCandidateWordsSortedByScore.get(currentSimMetric) == null) {
					TreeMap <String, String> seedTreeMap = new TreeMap<String, String>();
					allCandidateWordsSortedByScore.put(currentSimMetric, seedTreeMap);
				}
				
				TreeMap <String, String> internalTreeMap = allCandidateWordsSortedByScore.get(currentSimMetric);	

				internalTreeMap.put(candidateWord, candidateWord);
				allCandidateWordsSortedByScore.put(currentSimMetric, internalTreeMap);
				
			}
		}
				
		/*
		 *  Loops through each key of outer TreeMap in ascending order (higher to lower similarity metric scores).
		 *  Adds all values at that similarity metric score to the output array. If the size of the array is greater or equal to topN, the loop ends,
		 *  else the next lowest similarity metric score's values are added to the output. 
		 *  Once the last outer TreeMap key's values are added, the counter is set to topN, exiting the while loop
		 *  
		 *  Note that the values() method preserves the ordering. From the JavaDocs documentation about values() method: 
	     *  "Returns a Collection view of the values contained in this map. The collection's iterator returns the values in ascending order of the corresponding keys." 
		 */
		
		if (allCandidateWordsSortedByScore.size() != 0 && allCandidateWordsSortedByScore.get(allCandidateWordsSortedByScore.firstKey()) != null) {
			double key = allCandidateWordsSortedByScore.firstKey();
			int counter = 0;
			while (counter < topN) {			
				output.addAll(allCandidateWordsSortedByScore.get(key).values());
				counter += allCandidateWordsSortedByScore.get(key).values().size();
				if (key != allCandidateWordsSortedByScore.lastKey()) {
					key = allCandidateWordsSortedByScore.higherKey(key);	
				}
				else {
					counter = topN;
				}
			}
		}
		
		/* 
		 * The below for-loop checks for words that end in "'s". 
		 * If that word WITHOUT the apostrophe exists in the output, the word WITH the apostrophe is removed. 
		 * If that word WITHOUT the apostrophe is not in the output, the word WITH the apostrophe is replaced by the word WITHOUT the apostrophe.
		 * Words with apostrophes could have been weeded out when initially populating the output ArrayList, but removing words with apostrophes
		 * is specific to this assignment; ultimately I would want to output words with apostrophes, and would remove the below for-loop.   
		 */   
		for (int i = 0; i < output.size(); i++) {
			String withApostrophe = output.get(i);
			if (withApostrophe.charAt(withApostrophe.length() - 2) == '\'') {
				String withoutApostrophe = withApostrophe.replace("\'", "");
				if (output.contains(withoutApostrophe)) {
					output.remove(withApostrophe);
				}
				else {
					int replacementIndex = output.indexOf(withApostrophe);
					output.set(replacementIndex, withoutApostrophe);
				}
				
			}
		}
		return output;
	}
	
	/**
	 * Method for finding other words with at least n unique letters in common to supplied word
	 * 
	 * @param word source word to be used for finding other words with at least n unique letters in common
	 * @param listOfWords list of words to be checked for shared unique letters with supplied word.
	 * @param n minimum number of unique letters in common
	 * @return list of words that have at least n unique letters in common with supplied word 
	 */
	
	public ArrayList<String> getWordsWithCommonLetters (String word, ArrayList<String> listOfWords, int n){
		ArrayList<String> output = new ArrayList<String>();
		
		Set<String> s2 = uniqueLetters(word);
		
		for (String wordFromList : listOfWords) {
			Set<String> s1 = uniqueLetters(wordFromList);
			Set<String> unionOfSets = new HashSet<String>();
			Set<String> intersectionOfSets = new HashSet<String>();
			unionOfSets.addAll(s1);
			unionOfSets.addAll(s2);
			for (String element : unionOfSets) {
				if (s1.contains(element) && s2.contains(element)) {
					intersectionOfSets.add(element);
				}
			}
			if (intersectionOfSets.size() >= n) {
				output.add(wordFromList);
			}
		}
		
		return output;
	}	
	
	/**
	 * Helper method for getWordsWithCommonLetters. Returns HashSet of unique letters in a word
	 * 
	 * @param word word to be checked for unique letters
	 * @return Set containing unique letters in a word
	 */
	
	public Set<String> uniqueLetters(String word) {
		
		Set<String> output = new HashSet<String>();
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) != '\'') {
				output.add(Character.toString(word.charAt(i)));
			}
        }
		return output;
		
	}
	
	/**
	 * Method for displaying list of words returned by getWordSuggestions method 
	 * 
	 * @param list list of words returned by getWordSuggestions
	 * @return display-ready String of words
	 */
	
	public String prettyPrint (ArrayList<String> list) {
		String[] outputArray = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			int place = i + 1;
			outputArray[i] = (place + ". " + list.get(i));
		}
		String output = Arrays.toString(outputArray);
		return output.substring(1, output.length() - 1).replaceAll(", ", "\n");
	}
	
	/**
	 * getter method for dictionary filename. Currently unused.
	 * 
	 * @return fileName name of dictionary file
	 */
	
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * getter method for Dictionary instance dictionary
	 * 
	 * @return dictionary;
	 */
	public Dictionary getDictionary () {
		return dictionary;
	}
	
	public void updateDictionaries() {
		this.dictionaryWords = dictionary.getWordDictionary();
		this.dictionaryByWordLength = dictionary.getWordByLengthDictionary();
	}
	
	/**
	 * Method for checking if an individual word from the user-supplied document is in the dictionary  
	 * 
	 * @param word word being checked
	 * @return boolean; true if word was found, false if word was not found
	 */
	public boolean checkForExactWord(String word) {
		if (dictionaryWords.get(word) != null){
			return true;
		}
		return false;
	}

}
