import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Class containing methods for determining whether an input string is a number.
 * 
 * <p> Used for checking if input string is a number.
 * 
 * @author Jonathan Fuchs
 * @version 1.0
 * @since 1.0
 */
public class NumberChecker {

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
}
