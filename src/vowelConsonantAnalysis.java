import java.util.ArrayList;

public class vowelConsonantAnalysis {
	
	private int totalVowelCount;
	private int totalConsonantCount;
	private ArrayList<Integer> vowelCounts = new ArrayList<Integer>();
	private ArrayList<Integer> consonantCounts = new ArrayList<Integer>();
	private double averageCounts;
	private double avgVowels;
	private double avgConsonants;
	private int charCount;
	
	public void addCharCount(int count) {
		charCount+=count;
	}
	
	
	public double getAvgVowels() {
		return avgVowels;
	}

	public double getAvgConsonants() {
		return avgConsonants;
	}

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
	
	public void vandcAnalysisSuite() {
		System.out.println();
		System.out.println("Here is some vowel and consonant analysis on your document: ");
		System.out.println();
		avgVowels = calculateAverageVowelandConsonantCounts(vowelCounts);
		avgConsonants = calculateAverageVowelandConsonantCounts(consonantCounts);
		System.out.println("The total number of characters in your file: " + charCount);
		System.out.println("The total number of vowels in your file: " + totalVowelCount);
		System.out.println("The total number of consonants in your file: " + totalConsonantCount);
		System.out.println("The average number of vowels per word in your file: " + avgVowels);
		System.out.println("The average number of consonants per word in your file: " + avgConsonants);
		
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
	 * @return 
	 */
	
	public double calculateAverageVowelandConsonantCounts(ArrayList<Integer> array) {
		double sum = 0;
		int length = array.size();

		for (int element : array) {
			sum = sum + element;
		}
		averageCounts = sum/length;
		return averageCounts;	
	}	

}
