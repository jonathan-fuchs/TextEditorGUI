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

	/**
	 * Constructor extending WordRecommender class. super used for class inheritance.
	 * 
	 * @param fileName name of dictionary file passed into WordRecommender
	 */
	
	public IOInterface(String fileName) {
		super(fileName);
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
	 * Central method for spell-checking user-provided document. Accessed via askForDocument() method.
	 * 
	 * @param docName user provided file name for document to be spell checked
	 * @return boolean for whether user-supplied file was found
	 */
	
	public boolean checkDocument(String docName) {
		SpellingAnalysis analysis = new SpellingAnalysis();
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
				
				while (docScanner.hasNext()) {
					String word = docScanner.next().toLowerCase();
					/*
					 * Checks for exact word matches in dictionary. Then checks for numbers in user document, and will pass them through unchanged.
					 * 
					 * TODO Punctuation and other pass-through (such as user-defined dictionary additions) would be added here. 
					 * 
					 * Then will check misspelled words and will provide alternate word suggestions
					 */
					
					// add word count
					int currentWordCount = analysis.getWordCount();
					analysis.setWordCount(currentWordCount++);
					
					if (checkForExactWord(word) == true) {
						pw.print(word + " ");
					}
					else if(NumberChecker.isBigInteger(word) == true)
					{
						pw.print(word + " ");
					}
					else if(NumberChecker.isBigDecimal(word) == true)
					{
						pw.print(word + " ");
					}
					else {
						/*
						 * getWordSuggestions called here. Adjust variables to adjust set of words retrieved from dictionary.
						 */
						ArrayList<String> possibleWords = getWordSuggestions(word, 2, 0.7, 4);
						System.out.println("The word '" + word + "' is misspelled.");
						boolean rCommandAllowed = true;
						if (possibleWords.size() >= 1) {
							System.out.println("The following suggestions are available");
							System.out.println(prettyPrint(possibleWords));
							System.out.println("Press 'r' for replace, 'a' for accept as is, 't' for type in manually.");
						}
						else {
							System.out.println("There are 0 suggestions in our dictionary for this word.");
							System.out.println("Press 'a' for accept as is, 't' for type in manually.");	
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
									System.out.println("Your word will now be replaced with one of the suggestions");
									System.out.println("Enter the number corresponding to the word that you want to use for replacement.");

									boolean repeatRCommand = true;								
									while (repeatRCommand == true) {
										String replacementNumber = userInput.nextLine();
										if (NumberChecker.isInteger(replacementNumber) && Integer.parseInt(replacementNumber) > 0 && Integer.parseInt(replacementNumber) <= possibleWords.size()) {
											pw.print(possibleWords.get(Integer.parseInt(replacementNumber) - 1) + " ");
											repeatRCommand = false;
										}
										else {
											System.out.print("Input not understood. Please re-enter: ");
										}	

									}

								}
								else if (command.trim().equals("a")) {
									pw.print(word + " ");

								}
								else if (command.trim().equals("t")) {
									System.out.println("Please type the word that will be used as the replacement in the output file.");
									String correctedWord = userInput.nextLine();
									if (correctedWord.equals("")) {
										System.out.print("Input is empty. This will delete the word. Press enter again to confirm, or else re-enter replacement word: ");
										correctedWord = userInput.nextLine();
										if (!correctedWord.equals("")) {
											pw.print(correctedWord + " ");
										}
									}
									else {
										pw.print(correctedWord + " ");
									}								
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
