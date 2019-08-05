import java.awt.EventQueue;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Class governing input/output with user
 * 
 * @author Jonathan Fuchs
 * @version 1.0
 * @since 1.0
 */

public class IOInterface extends WordRecommender {
	
	private boolean capitalizeNext = true;
	/*
	 * doubleQuote variable tracks whether a " has been used previously so to track whether a space should be added after the " or not.
	 * doubleQuote variable removed for now as double quotation functionality currently not working. Java is interpreting " mark from input document as the end of the input.
	 * Will need to systematize adding escape character before any quote mark detected in input before re-activating double quotation functionality. 
	 */
	//private boolean doubleQuote = false;

	/*
	 * Counting Instance Variables with Getters
	 */

	private SpellingAnalysis analysis = new SpellingAnalysis();
	private vowelConsonantAnalysis vowelAnalysis = new vowelConsonantAnalysis();
	private TextFormatting formatter = new TextFormatting();
	private boolean endOfSentence = false;
	private boolean oldUI = true;

	public boolean getOldUI() {
		return oldUI;
	}
		
	public vowelConsonantAnalysis getVowelAnalysis() {
		return vowelAnalysis;
	}
	
	public SpellingAnalysis getAnalysis() {
		return analysis;
	}

	/**
	 * Constructor extending WordRecommender class. super used for class inheritance.
	 * 
	 * @param fileName name of dictionary file passed into WordRecommender
	 */
	
	public IOInterface(String fileName) {
		super(fileName);
	}

	public void askForInputMethod() {


		TextDocumentUI ui = new TextDocumentUI();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ui.createAndShowGUI();  

				EventQueue.invokeLater(new Runnable() {
					public void run() {		                		
						ui.getFrame().setAlwaysOnTop(false);
					}
				});
			}
		});




	}

	/**
	 * Method for asking user for file to be spell-checked. Hook for launching checkDocument method, which runs spell-check program on a specified file.
	 */
	public void askForDocument() {
		Scanner docNameInput = new Scanner(System.in);
		boolean end = false;
		System.out.print("Please enter document name: ");
		while (end == false) {
			end = checkDocument(docNameInput.nextLine().trim());
		}
		docNameInput.close();
	}
	
	/**
	 * Helper method for printing words to the output document. Contains logic for controlling whether to 
	 * 
	 * @param pw PrintWriter used for printing to document
	 * @param word word to be printed to document
	 * @param capitalize boolean for whether the first letter of the word should be capitalized
	 * @param hasPunctuation boolean for whether the word is followed by punctuation
	 * @param punctuationString String that contains punctuation to be appended to the word
	 */
	public void printWordToDoc(PrintWriter pw, String word, boolean capitalize, boolean hasPunctuation, String punctuationString) {
		// add word count to analysis
		analysis.incrementWordCount();
		
		analysis.getSyllablesInWord(word);
		
		vowelAnalysis.vowelConsontantCounts(word);	
		
		vowelAnalysis.addCharCount(word.length());
		
		// check for end of sentence
		if (endOfSentence) {
			analysis.getSentenceLength();
			endOfSentence = false;
		}
		else {
			analysis.incrementWordsInCurrentSentence();
		}
		
		if (capitalize == true) {
			char firstChar = word.charAt(0);
			String strFirstChar = firstChar + "";
			word = strFirstChar.toUpperCase() + word.substring(1);
			this.capitalizeNext = false;
		}
		
		String trailingSpace = " ";
		
		if (hasPunctuation == true) {
			/*
			 * removed until support for double quotation added back in.
			if (punctuationString.equals("\"")){
				this.doubleQuote = !this.doubleQuote;
			}
			if (this.doubleQuote == true) {
				trailingSpace = "";
			}
			*/
			
			// line break logic
			String addedString = word + punctuationString + trailingSpace;
			formatter.setLineCharLength(formatter.getLineCharLength() + addedString.length());
			formatter.addLineBreaks(pw);
			
			pw.print(word + punctuationString + trailingSpace);
			if (punctuationString.equals(".") || punctuationString.contains("?") || punctuationString.contains("!") || punctuationString.equals("....")) {
				this.capitalizeNext = true;
			}
		}
		else {
			// line break logic
			String addedString = word + trailingSpace;
			formatter.setLineCharLength(formatter.getLineCharLength() + addedString.length());
			formatter.addLineBreaks(pw);
			
			pw.print(word + trailingSpace);
		}
		
	}
	
	/**
	 * Central method for spell-checking user-provided document. Accessed via askForDocument() method.
	 * 
	 * @param docName user provided file name for document to be spell checked
	 * @return boolean for whether user-supplied file was found
	 */
	
	public boolean checkDocument(String docName) {

		File userDocument = new File(docName);
		String outputDocumentName;
		
		/*
		 * Checks if user file has a file extension. If yes, appends "_chk" before the file extension, else it appends "_chk" to the end of the file name.
		 */
		if (docName.lastIndexOf('.') != -1) {
			outputDocumentName = docName.substring(0, docName.lastIndexOf('.')).concat("_chk").concat(docName.substring(docName.lastIndexOf('.'), docName.length()));
		}
		else {
			outputDocumentName = docName.concat("_chk");
		}
		
		try {
			/*
			 * Scanner for reading user-supplied document
			 */
			Scanner docScanner = new Scanner(userDocument);
			
			try {
				/*
				 * Will delete existing copy of _chk file, and then will create a new one with the same name.
				 */
				Path existingChkDoc = FileSystems.getDefault().getPath(outputDocumentName);
				Files.deleteIfExists(existingChkDoc);
				
				FileWriter fw = new FileWriter(outputDocumentName, true);
				PrintWriter pw = new PrintWriter(fw);
				Scanner userInput = new Scanner(System.in);
				
				String afterPunctuation = "";
				
				while (docScanner.hasNext()) {
					String word;
					if (afterPunctuation.equals("")) {
						word = docScanner.next().toLowerCase();
					}
					else {
						word = afterPunctuation;
					}

					boolean punctuationFound = false;
					String punctuation = "";

					/*
					 * Checks for exact word matches in dictionary. 
					 * Then checks for numbers in user document; if a number is found, it will pass through unchanged and the while loop will skip to next word in the document.
					 * 
					 * TODO Punctuation and other pass-through (such as user-defined dictionary additions) would be added here. 
					 * 
					 * Then will check misspelled words and will provide alternate word suggestions
					 */

					if(PatternChecker.isBigInteger(word) == true)
					{
						printWordToDoc(pw, word, false, false, punctuation);
						continue;
					}
					else if(PatternChecker.isBigDecimal(word) == true)
					{
						printWordToDoc(pw, word, false, false, punctuation);
						continue;
					}

					/*
					 * Detects if the word contains punctuation. If it does, saves the first and last index of the first continuous punctuation string in that word.
					 * If the word contains characters after this index, the substring after this index is saved to afterPunctuation and is fed back into the outer
					 * while-loop.
					 * 
					 * Example: "what?!hesaidthat..." 
					 * The "?!" would be saved as the punctuation string, and "hesaidthat..." would be saved as afterPunctuation, and would then be sent through
					 * the outer while-loop again.   
					 */

					int punctuationIndex = PatternChecker.detectPunctuation(word);
					int firstPunctuationIndex = punctuationIndex;
					int lastPunctuationIndex = -1;
					while(punctuationIndex != -1) {
						lastPunctuationIndex = punctuationIndex;
						int nextPunctuationIndex = PatternChecker.detectPunctuation(word.substring(punctuationIndex) + 1);
						if (nextPunctuationIndex != 0) {
							break;
						}
						punctuationIndex++;
					}
					if (lastPunctuationIndex != -1) {punctuationFound = true;}
					
					if (punctuationFound == true) {
						punctuation = word.substring(firstPunctuationIndex, lastPunctuationIndex);
						afterPunctuation = word.substring(lastPunctuationIndex);
						word = word.substring(0, firstPunctuationIndex);
						
						// for end of sentence word count
						endOfSentence = true;
					}
					else {
						afterPunctuation = "";
					}

					if (checkForExactWord(word) == true) {
						printWordToDoc(pw, word, capitalizeNext, punctuationFound, punctuation);
					}
					else {
						// update count of words checked
						analysis.incrementSpellCheckedWords();
						
						/*
						 * getWordSuggestions called here. Adjust variables to adjust set of words retrieved from dictionary.
						 */
						ArrayList<String> possibleWords = getWordSuggestions(word, 2, 0.7, 4);
						System.out.println("The word '" + word + "' is misspelled.");
						boolean rCommandAllowed = true;
						if (possibleWords.size() >= 1) {
							System.out.println("The following suggestions are available");
							System.out.println(prettyPrint(possibleWords));
							System.out.println("Press 'r' for replace, 'a' for accept as is, 't' for type in manually, 'd' to add word to dictionary.");
						}
						else {
							System.out.println("There are 0 suggestions in our dictionary for this word.");
							System.out.println("Press 'a' for accept as is, 't' for type in manually, 'd' to add word to dictionary.");	
							rCommandAllowed = false;
						}
						
						/*
						 * repeat boolean controls whether user is to be re-prompted to re-enter a command
						 */
						boolean repeat = true;
						
						while (repeat == true) {

							if (userInput.hasNextLine()) {
								repeat = false;

								String command = userInput.nextLine();	
								if (command.trim().equals("r") && rCommandAllowed == true) {
									// update count of words replaced
									analysis.incrementWordsFromSuggestion();
									
									System.out.println("Your word will now be replaced with one of the suggestions");
									System.out.println("Enter the number corresponding to the word that you want to use for replacement.");

									boolean repeatRCommand = true;								
									while (repeatRCommand == true) {
										String replacementNumber = userInput.nextLine();
										if (PatternChecker.isInteger(replacementNumber) && Integer.parseInt(replacementNumber) > 0 && Integer.parseInt(replacementNumber) <= possibleWords.size()) {
											printWordToDoc(pw, possibleWords.get(Integer.parseInt(replacementNumber) - 1), capitalizeNext, punctuationFound, punctuation);
											//pw.print(possibleWords.get(Integer.parseInt(replacementNumber) - 1) + " ");
											repeatRCommand = false;
										}
										else {
											System.out.print("Input not understood. Please re-enter: ");
										}	

									}

								}
								else if (command.trim().equals("a")) {
									// update count of accepted words
									analysis.incrementWordsAccepted();
									
									printWordToDoc(pw, word, capitalizeNext, punctuationFound, punctuation);
								}
								else if (command.trim().equals("t")) {
									//update count of manually entered words
									analysis.incrementWordsFromManualEntry();
									
									System.out.println("Please type the word that will be used as the replacement in the output file.");
									String correctedWord = userInput.nextLine();
									if (correctedWord.equals("")) {
										System.out.print("Input is empty. This will delete the word. Press enter again to confirm, or else re-enter replacement word: ");
										correctedWord = userInput.nextLine();
										if (!correctedWord.equals("")) {
											printWordToDoc(pw, correctedWord, capitalizeNext, punctuationFound, punctuation);
											//pw.print(correctedWord + " ");
										}
									}
									else {
										printWordToDoc(pw, correctedWord, capitalizeNext, punctuationFound, punctuation);
										//pw.print(correctedWord + " ");
									}								
								}
								else if(command.trim().equals("d")) {
									printWordToDoc(pw, word, capitalizeNext, punctuationFound, punctuation);
									getDictionary().addWordToDictionaries(word);
									updateDictionaries();
									// Below code will update dictionary text file
									File dictionaryDocument = new File(getFileName());
									FileWriter fwDictionary = new FileWriter(dictionaryDocument, true);
									PrintWriter pwDictionary = new PrintWriter(fwDictionary);
									pwDictionary.println(word);
									pwDictionary.close();
									fwDictionary.close();
									
								}
								else if (command.contentEquals("")) {
									System.out.print("Please select a command ");
									repeat = true;
								}
								else {
									repeat = true;
									System.out.print("Command not understood. Please re-enter: ");
								}

							}
						}
						
								
						System.out.println();
						System.out.println();
						System.out.println();						
						
					}
					
				}
				userInput.close();
				pw.flush();
							
				pw.close();
				fw.close();
				
			} catch (IOException e) {
				System.out.println("You are already running this spellcheck on the specified file. This instance of the spellcheck program will now exit. Please continue using the other instance of the spellcheck program.");
				docScanner.close();
				return true;
			}
			
			
			docScanner.close();
		} catch (FileNotFoundException e) {
			System.out.print("File not found. Please re-enter filename: ");
			return false;
		}
		System.out.println("Spell check complete.");
		return true;
	}
	
}
