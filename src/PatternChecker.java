import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.*;

/**
 * Class containing methods for determining whether an input string is a number.
 * 
 * <p> Used for checking if input string is a number.
 * 
 * @author Jonathan Fuchs
 * @version 1.0
 * @since 1.0
 */
public class PatternChecker {

	/**
	 * Method for determining if string input is an integer.
	 * Method used for checking validity of user input after 'replace' command. 
	 * Note: this method registers empty carriage returns as invalid inputs.
	 * 
	 * @param str user input, taken as a string, to be checked for validity
	 * @return true if input is an integer, false if not
	 */
	public static boolean isInteger(String str) {
		try {
	        Integer.parseInt(str);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Method for determining if string input is a big integer.
	 * Method used in passing numbers from original document unchanged into _chk file. 
	 * 
	 * @param str string to be checked
	 * @return true if input is a big integer, false if not
	 */
	public static boolean isBigInteger(String str) {
		try {
	        @SuppressWarnings("unused")
			BigInteger test = new BigInteger(str);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Method for determining if string input is a big decimal.
	 * Method used in passing numbers from original document unchanged into _chk file. 
	 * 
	 * @param str string to be checked
	 * @return true if input is a big decimal, false if not
	 */
	public static boolean isBigDecimal(String str) {
		try {
			@SuppressWarnings("unused")
			BigDecimal test = new BigDecimal(str);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}

	/**
	 * Method for detecting punctuation in the document. 
	 * 
	 * TODO add handling for double and single quotation marks
	 * Pattern will then be: "[,.!?\\-;:\"\']"
	 * 
	 * @param str string to be checked for punctuation
	 * @return -1 if no punctuation found, else the first index of a punctuation mark
	 */
	public static int detectPunctuation(String str) {
		Pattern pattern = Pattern.compile("[,.!?\\-;:]");
		Matcher matcher = pattern.matcher(str); 
		
		if (matcher.find() == false) {
			return -1;
		}
		return matcher.start();
	}
	
	/**
	 * Method for detecting earliest 'word character' in a String. Detects letter, digit, or underscore. 
	 *  
	 * @param str
	 * @return -1 if no 'word character' found, else returns the index of the earliest 'word character' 
	 */
	public static int detectNonPunctuation(String str) {
		Pattern pattern = Pattern.compile("[\\w]");
		Matcher matcher = pattern.matcher(str); 
		
		if (matcher.find() == false) {
			return -1;
		}
		return matcher.start();
	}
	
	/**
	 * Method for detecting the earliest non-whitespace character in a String.
	 *  
	 * @param str
	 * @return -1 if no 'non-whitespace character' detected, else returns the index of the earliest 'non-whitespace character'
	 */
	public static int detectNonSpaces(String str) {
		Pattern pattern = Pattern.compile("[^\\s]");
		Matcher matcher = pattern.matcher(str); 
		
		if (matcher.find() == false) {
			return -1;
		}
		return matcher.start();
	}
	
	/**
	 * Method for detecting the earliest whitespace character in a String.
	 *  
	 * @param str
	 * @return -1 if no 'whitespace character' detected, else returns the index of the earliest 'whitespace character'
	 */
	public static int detectSpaces(String str) {
		Pattern pattern = Pattern.compile("[\\s]");
		Matcher matcher = pattern.matcher(str); 
		
		if (matcher.find() == false) {
			return -1;
		}
		return matcher.start();
	}
	
	/**
	 * Method for detecting sentence-ending punctuation in the document. 
	 * 
	 * @param str string to be checked for punctuation
	 * @return -1 if no punctuation found, else the first index of a punctuation mark typically used in ending a sentence
	 */
	public static int detectSentenceEndingPunctuation(String str) {
		Pattern pattern = Pattern.compile("[.!?]");
		Matcher matcher = pattern.matcher(str); 
		
		if (matcher.find() == false) {
			return -1;
		}
		return matcher.start();
	}
}
