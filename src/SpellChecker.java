
/**
 * Runner for spell checker program.
 * 
 * @author Jonathan Fuchs
 * @version 1.0
 * @since 1.0
 */
public class SpellChecker {

	/**
	 * Main method 
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {		
		IOInterface instance = new IOInterface("engDictionary.txt");		
		instance.askForInputMethod();	
	}
}
