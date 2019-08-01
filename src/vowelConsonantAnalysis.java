import java.util.ArrayList;

public class vowelConsonantAnalysis {
	
	private int totalVowelCount;
	private int totalConsonantCount;
	private ArrayList<Integer> vowelCounts = new ArrayList<Integer>();
	private ArrayList<Integer> consonantCounts = new ArrayList<Integer>();;
	private double averageCounts;
	
	public ArrayList<Integer> getVowelCounts() {
		return vowelCounts;
	}

	public ArrayList<Integer> getConsonantCounts() {
		return consonantCounts;
	}

	public int getTotalVowelCount() {
		return totalVowelCount;
	}

	public int getTotalConsonantCount() {
		return totalConsonantCount;
	}

	public double getAverageCounts() {
		return averageCounts;
	}
	
	/**
	 * Finds the total number of consonants and vowels in the document. 
	 * Makes an array of all individual word's consonant and vowel counts to be used in the method below.
	 * @param word
	 */
	public void vowelConsontantCounts(String word) {
		char[] vowelArray = new char[5];
		vowelArray[0] = 'a';
		vowelArray[1] = 'e';
		vowelArray[2] = 'i';
		vowelArray[3] = 'o';
		vowelArray[4] = 'u';
		
		int individualWordVowelCount = 0;
		int individualWordConsonantCount = 0;
		
		for (int i = 0; i < word.length(); i++) {
			if (new String (vowelArray).contains(Character.toString(word.charAt(i)))) {
				totalVowelCount++;
				individualWordVowelCount++;
			}
			else {
				totalConsonantCount++;
				individualWordConsonantCount++;
			}
			
		vowelCounts.add(individualWordVowelCount);
		consonantCounts.add(individualWordConsonantCount);
		
		}
	}
	
	/**
	 * Calculates the average number of vowels and consonants per word
	 * @param array
	 */
	
	public void calculateAverageVowelandConsonantCounts(ArrayList<Integer> array) {
		double sum = 0;
		int length = array.size();

		for (int element : array) {
			sum = sum + element;
		}
		averageCounts = sum/length;	
	}	

}
