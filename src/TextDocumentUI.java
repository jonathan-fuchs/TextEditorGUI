
import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import javax.swing.JPanel;
//import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
 
/**
 * Class to add a GUI interface to the spell checking program
 * 
 * <p>Menu functionality includes:<ul>
 * <li> File menu: create a new document, open a text document from a file, save a currently opened document, save the current document as a new document, exit the program.
 * <li> Edit menu: cut text to system clipboard, copy text to system clipboard, paste text from system clipboard, select all text. 
 * <li> Format menu: highlight selected text, remove all highlights, set global font size to one of three settings.
 * <li> Review menu: spell check text in GUI JTextPane, reset dictionary (i.e., remove all words added to dictionary), display readability score in Eclipse console.
 * <li> Suggestion menu (appears once 'Spell Check' function runs, when spelling mistakes have been found): menu for each misspelled word found.
 * <ul><li> Each misspelled word's menu offers options to ignore all appearances of the word until the program closes 
 * <li>Add the word to the dictionary 
 * <li>Any alternate word suggestions. If one of these alternate words is selected, the misspelled word will be replaced in the JTextPane with the alternate word, and 
 * the highlighter object marking that misspelled word will be removed. 
 * </ul>
 * <li>Current file name menu: the name of this menu updates depending on which file is currently open 
 * </ul></p>
 * 
 * <p> Additional functionality:<ul>
 * <li> After spell check is run, misspelled words will be underlined with a red squiggle. (NOTE: code for red squiggle highlighter was written by another developer. 
 * See SquigglePainter class for more details.) 
 * <li> Punctuation and capitalization is correctly handled by spell check.
 * <li> Many menu commands have keyboard shortcuts. Many menu commands also have hotkeys when menu is open.
 * <li> If text extends past current window size, a scroll bar will be created
 * </ul></p>
 * 
 * <p> I found this tutorial for creating menu items immensely helpful: https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
 * specifically, this: 
 * https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/MenuLookDemoProject/src/components/MenuLookDemo.java
 * 
 * @author Jonathan Fuchs
 *
 */
public class TextDocumentUI {
	
	private JFrame frame;
	private JTextPane output;
	private String documentText;
	private JScrollPane scrollPane;
	private JMenuBar menuBar;
	private JMenuItem suggestionsMenu = new JMenu("Suggestions");;
	
	private JDialog popUpWindow;
	private JTextField popUpWindowTextField;
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	private String titleBar = "Document Editor GUI";
	private String documentName = "Untitled Document";
	private JMenu currentFileName;
	private boolean newDocument = true;
	private File document;

	private DefaultHighlighter highlighter;
	
	private String dictionaryFileName = "engDictionary.txt";
	private String backupDictionaryFileName = "engDictionary_backup.txt";
	private WordRecommender dictionary = new WordRecommender(dictionaryFileName);
	private ArrayList<ArrayList<String>> menuSuggestions = new ArrayList<>();	
	
	/**
	 * Getter method for JFrame component. Used by IOInterface to manipulate .setAlwaysOnTop property.
	 * .setAlwaysOnTop is initially set to true so that the GUI window will come to system foreground, but
	 * should then be set to false so that other windows may later come to the foreground.
	 * 
	 * @return frame
	 */
    public JFrame getFrame() {
    	return frame;
    }
	
    /**
     * Method to create GUI MenuBar
     * 
     * @return menuBar
     */
    public JMenuBar createMenuBar() {
        JMenu fileMenu, editMenu, formatMenu, fontSizeMenu, reviewMenu;
        JMenuItem menuItemNew, menuItemSave, menuItemSaveAs, menuItemOpen, menuItemExit;
        JMenuItem menuItemCopy, menuItemCut, menuItemPaste, menuItemSelectAll;
        JMenuItem menuItemHighlight, menuItemRemoveAllHighlights, menuItemNormalFont, menuItemLargeFont, menuItemLargestFont;
        JMenuItem menuItemSpellCheck, menuItemResetDictionary, menuItemReadabilityScore;
        Action copy = new DefaultEditorKit.CopyAction();
        Action cut = new DefaultEditorKit.CutAction();
        Action paste = new DefaultEditorKit.PasteAction();
 
        // Create the menu bar.
        menuBar = new JMenuBar();
 
        // Build File menu. Hotkey is 'f'
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription("File menu");
        menuBar.add(fileMenu);
 
        // File menu item to create a new document. Hotkey is 'n' and keyboard shortcut is "ctrl + s".
        menuItemNew = new JMenuItem("New", KeyEvent.VK_N);
        menuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItemNew.getAccessibleContext().setAccessibleDescription("Create a new file");
        menuItemNew.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		newDocument();
        	}
        });
        
        // File menu item to save current document. Hotkey is 's' and keyboard shortcut is "ctrl + s".
        menuItemSave = new JMenuItem("Save", KeyEvent.VK_S);
        menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItemSave.getAccessibleContext().setAccessibleDescription("Save the current file");
        menuItemSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		if (newDocument == false) {
        			saveDocument();
        		}
        		else {
        			createPopUpWindow("Save As");
        		}
        	}
        });
        
        // File menu item to save current text as a new document. Keyboard shortcut is "ctrl + shift + a".
        menuItemSaveAs = new JMenuItem("Save As");
        menuItemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        menuItemSaveAs.getAccessibleContext().setAccessibleDescription("Save As for the current file");
        menuItemSaveAs.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		createPopUpWindow("Save As");
        	}
        });
        
        // File menu item to open a document. Hotkey is 'o' and keyboard shortcut is "ctrl + o".
        menuItemOpen = new JMenuItem("Open", KeyEvent.VK_O);
        menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItemOpen.getAccessibleContext().setAccessibleDescription("Open an existing file");
        menuItemOpen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		createPopUpWindow("Open");
        	}
        });
        
        // File menu item to exit program. Hotkey is 'x' and keyboard shortcut is "alt + F4".
        menuItemExit = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        menuItemExit.getAccessibleContext().setAccessibleDescription("Close the program");
        menuItemExit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		System.exit(0);
        	}
        });
        
        // Adds menu items to File menu
        fileMenu.add(menuItemNew);
        fileMenu.add(menuItemSave);
        fileMenu.add(menuItemSaveAs);
        fileMenu.add(menuItemOpen);
        fileMenu.addSeparator();
        fileMenu.add(menuItemExit);
 
        
        // Build Edit menu. Hotkey is 'e'
        editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        editMenu.getAccessibleContext().setAccessibleDescription("Edit Menu");
        menuBar.add(editMenu);
        
        // Edit menu item to copy currently selected text to system clipboard. Hotkey is 'c' and keyboard shortcut is "ctrl + c".
        menuItemCopy = new JMenuItem(copy);
        menuItemCopy.setMnemonic(KeyEvent.VK_C);
        menuItemCopy.setText("Copy");
        menuItemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItemCopy.getAccessibleContext().setAccessibleDescription("Copies selected text in the current document");
       
        // Edit menu item to cut currently selected text to system clipboard. Hotkey is 't' and keyboard shortcut is "ctrl + x".
        menuItemCut = new JMenuItem(cut);
        menuItemCut.setMnemonic(KeyEvent.VK_T);
        menuItemCut.setText("Cut");
        menuItemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItemCut.getAccessibleContext().setAccessibleDescription("Cuts selected text in the current document");

        // Edit menu item to paste text from system clipboard. Hotkey is 'p' and keyboard shortcut is "ctrl + v".
        menuItemPaste = new JMenuItem(paste);
        menuItemPaste.setMnemonic(KeyEvent.VK_P);
        menuItemPaste.setText("Paste");
        menuItemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItemPaste.getAccessibleContext().setAccessibleDescription("Pastes selected text in the current document");
        
        
        // Edit menu item to select all text in JTextPane. Hotkey is 'a' and keyboard shortcut is "ctrl + a".
        menuItemSelectAll = new JMenuItem("Select All", KeyEvent.VK_A);
        menuItemSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menuItemSelectAll.getAccessibleContext().setAccessibleDescription("Select all text in the current document");
        menuItemSelectAll.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		output.selectAll();
        	}
        });
        
        // Adds menu items to Edit menu
        editMenu.add(menuItemCopy);
        editMenu.add(menuItemCut);
        editMenu.add(menuItemPaste);
        editMenu.addSeparator();
        editMenu.add(menuItemSelectAll);
        
        
        // Build Format menu. Hotkey is 'o'
        formatMenu = new JMenu("Format");
        formatMenu.setMnemonic(KeyEvent.VK_O);
        formatMenu.getAccessibleContext().setAccessibleDescription("Format Menu");
        menuBar.add(formatMenu);
        
        // Format menu item to highlight currently selected text in JTextPane. Hotkey is 'h'
        menuItemHighlight = new JMenuItem("Highlight Text", KeyEvent.VK_H);
        menuItemHighlight.getAccessibleContext().setAccessibleDescription("Highlights text in yellow");
        menuItemHighlight.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		highlighter = (DefaultHighlighter)output.getHighlighter();
        		highlighter.setDrawsLayeredHighlights(false);
        		try {        			
        			highlighter.addHighlight(output.getSelectionStart(), output.getSelectionEnd(), new DefaultHighlighter.DefaultHighlightPainter(new Color(0xFAED27)));
					output.setCaretPosition(output.getSelectionEnd());
					
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        
        // Format menu item to remove all highlights from JTextPane. Will also remove highlights created by spell check. Hotkey is 'r'
        menuItemRemoveAllHighlights = new JMenuItem("Remove Highlights", KeyEvent.VK_R);
        menuItemRemoveAllHighlights.getAccessibleContext().setAccessibleDescription("Removes all highlights");
        menuItemRemoveAllHighlights.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		highlighter = (DefaultHighlighter)output.getHighlighter();
        		highlighter.removeAllHighlights();
        	}
        });
        
        // Format submenu listing several font size options. Hotkey is 't'
        fontSizeMenu = new JMenu("Font Size");
        fontSizeMenu.setMnemonic(KeyEvent.VK_T);
        fontSizeMenu.getAccessibleContext().setAccessibleDescription("Font Size Menu");
        
        // Font submenu item to set global font size to "normal" size.
        menuItemNormalFont = new JMenuItem("Normal Font Size");
        menuItemNormalFont.getAccessibleContext().setAccessibleDescription("Sets font size to normal size in output component");
        menuItemNormalFont.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		float size = (float) 12.0;
        		output.setFont(output.getFont().deriveFont(size));
        		output.updateUI();
        	}
        });

        // Font submenu item to set global font size to "large" size.
        menuItemLargeFont = new JMenuItem("Large Font Size");
        menuItemLargeFont.getAccessibleContext().setAccessibleDescription("Sets font size to the large size in output component");
        menuItemLargeFont.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		float size = (float) 20.0;
        		output.setFont(output.getFont().deriveFont(size));
        		output.updateUI();
        	}
        });

        // Font submenu item to set global font size to "largest" size.
        menuItemLargestFont = new JMenuItem("Largest Font Size");
        menuItemLargestFont.getAccessibleContext().setAccessibleDescription("Sets font size to largest size in output component");
        menuItemLargestFont.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		float size = (float) 30.0;
        		output.setFont(output.getFont().deriveFont(size));
        		output.updateUI();
        	}
        });
        
        // Adds menu items to Format menu and Font submenu
        formatMenu.add(menuItemHighlight);
        formatMenu.add(menuItemRemoveAllHighlights);
        formatMenu.add(fontSizeMenu);
        fontSizeMenu.add(menuItemNormalFont);
        fontSizeMenu.add(menuItemLargeFont);
        fontSizeMenu.add(menuItemLargestFont);
        
        
        // Build Review menu. Hotkey is 'r'
        reviewMenu = new JMenu("Review");
        reviewMenu.setMnemonic(KeyEvent.VK_R);
        reviewMenu.getAccessibleContext().setAccessibleDescription("Review Menu");
        menuBar.add(reviewMenu);
        
        menuItemSpellCheck = new JMenuItem("Spell Check", KeyEvent.VK_L);
        menuItemSpellCheck.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        menuItemSpellCheck.getAccessibleContext().setAccessibleDescription("Spell check the current document");
        menuItemSpellCheck.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		checkSpelling();
        	}
        });
        
        menuItemResetDictionary = new JMenuItem("Reset Dictionary", KeyEvent.VK_R);
        menuItemResetDictionary.getAccessibleContext().setAccessibleDescription("Removes user additions to dictionary");
        menuItemResetDictionary.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		try {
        			Path backupDictionary = Paths.get(backupDictionaryFileName);
            		Path currentDictionary = Paths.get(dictionaryFileName);
					Files.copy(backupDictionary, currentDictionary, StandardCopyOption.REPLACE_EXISTING);
					dictionary = new WordRecommender(dictionaryFileName);
					menuBar.remove(suggestionsMenu);
			    	suggestionsMenu = new JMenu("Suggestions");
			    	menuSuggestions = new ArrayList<>();
			        menuBar.updateUI();
			        
			        highlighter = (DefaultHighlighter)output.getHighlighter();
			    	highlighter.removeAllHighlights();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        
        menuItemReadabilityScore = new JMenuItem("Readability Score", KeyEvent.VK_S);
        menuItemReadabilityScore.getAccessibleContext().setAccessibleDescription("Calculates readability score for current document");
        menuItemReadabilityScore.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		SpellingAnalysis sAnalysis = new SpellingAnalysis();            	
            	String currentText = output.getText();
            	Scanner docScanner = new Scanner(currentText);
            	
            	while (docScanner.hasNextLine()) {
            		String currentLine = docScanner.nextLine();
            		//String currentWord;
            		
            		while (!currentLine.equals("")) {
            			
                		int spaceIndex = PatternChecker.detectSpaces(currentLine);
                		int punctuationIndex = PatternChecker.detectPunctuation(currentLine);
                		if (punctuationIndex == 0 || spaceIndex == 0) {
                			if (currentLine.length() == 1) {
                				currentLine = "";
                			}
                			else {
                				currentLine = currentLine.substring(1);
                			}
                		}
                		else if (punctuationIndex == -1 && spaceIndex == -1) {
                			sAnalysis.incrementWordsInCurrentSentence();
                			sAnalysis.getSyllablesInWord(currentLine);
                			currentLine = "";
                			//TODO deal with line breaks
                		}
                		// no punctuation but there is a space
                		else if (punctuationIndex == -1 || ((spaceIndex > -1) && (spaceIndex < punctuationIndex))) {
                			// add 1 to the sentence word count
                			sAnalysis.incrementWordsInCurrentSentence();
                			sAnalysis.getSyllablesInWord(currentLine.substring(0, spaceIndex));
                			// slice currentLine after next word
                			if (spaceIndex == currentLine.length()) {
                				currentLine = "";
                			}
                			else {
                				currentLine = currentLine.substring(spaceIndex + 1);
                			}
                			
                		}
                		else {
                		//there is punctuation before the next space
                		// equivalent to: else if (spaceIndex == -1 || ((punctuationIndex > -1) && (punctuationIndex < spaceIndex))) 
                			sAnalysis.incrementWordsInCurrentSentence();
                			sAnalysis.getSyllablesInWord(currentLine.substring(0, punctuationIndex));
                			sAnalysis.getSentenceLength();
                			
                			if (punctuationIndex == currentLine.length()) {
                				currentLine = "";
                			}
                			else {
                				currentLine = currentLine.substring(punctuationIndex + 1);
                			}
                		}
            		}
            	}
            	sAnalysis.approxReadability();
            	docScanner.close();
        	}
        });
        
        reviewMenu.add(menuItemSpellCheck);
        reviewMenu.add(menuItemResetDictionary);
        reviewMenu.addSeparator();
        reviewMenu.add(menuItemReadabilityScore);
        
        //Fifth menu is the suggestionsMenu and is created when running spellCheck

        
        
        // Sixth menu in menu bar is just a label
        currentFileName = new JMenu(documentName);
        
        menuBar.add(currentFileName);
        
        //System.out.println(menuBar.getComponent(4));
        
        //.getComponent(4).setText("hi");
        
          
        
        return menuBar;
    }
    
    /**
     * Helper method for createPopUpWindow
     * 
     * @param command
     */
    
    public void handlePopUpWindowCommands(String command) {
    	documentName = popUpWindowTextField.getText();
    	if (command.equals("Open")) {
    		Path findDoc = FileSystems.getDefault().getPath(documentName);
    		if(Files.exists(findDoc)) {
        		newDocument = false;
    			openDocument();
    			popUpWindow.dispose();
    		}
    		else {
    			popUpWindowTextField.setText("File not found. Please re-enter");
    			popUpWindowTextField.requestFocus();
    			popUpWindowTextField.selectAll();
    		}

    	}
    	else if (command.equals("Save As")) {
    		newDocument = false;
    		saveDocumentAs();
    		popUpWindow.dispose();
    	}
    	
    }
    
    public void createPopUpWindow(String command) {
    	ActionListener openButtonPressed = new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			handlePopUpWindowCommands(command);
    		}
    	};

    	class demoKL implements KeyListener {
    		public void keyTyped(KeyEvent e) {
    			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
    				handlePopUpWindowCommands(command);
    			}
    		}
    		public void keyPressed(KeyEvent e) {}
    		public void keyReleased(KeyEvent e) {}
    	}

    	popUpWindow = new JDialog(frame, "Type in file name");
    	
    	//popUpWindow.setLocationRelativeTo(frame);
    	popUpWindow.setLocation(dim.width/2-popUpWindow.getSize().width/2 - 200, dim.height/2-popUpWindow.getSize().height/2 - 200);
    	
    	
    	popUpWindow.setSize(318, 70);
    	popUpWindow.setVisible(true);
    	popUpWindow.setResizable(false);
    	popUpWindow.setAlwaysOnTop(true);
    	popUpWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    	Container c1 = popUpWindow.getContentPane();
		c1.setSize(200, 70);
		popUpWindowTextField = new JTextField(20);
		popUpWindowTextField.setLocation(0, 0);
		popUpWindowTextField.setSize(200, 70);
		popUpWindowTextField.setVisible(true);
		demoKL kl = new demoKL();
		popUpWindowTextField.addKeyListener(kl);
		c1.add(popUpWindowTextField, BorderLayout.WEST);
		
		JButton button = new JButton(command);
		button.setLocation(150, 0);
		button.setBounds(150, 0, 200, 70);
		button.setSize(50, 70);
		button.addActionListener(openButtonPressed);
		        		
		c1.add(button, BorderLayout.EAST);
		popUpWindow.pack();
		    	
    }
        
    public void openDocument() {
      	
    	dictionary = new WordRecommender(dictionaryFileName);
    	
    	documentText = "";

    	document = new File(documentName);

    	try {
    		Scanner docScanner = new Scanner(document);

    		while (docScanner.hasNextLine()) {
    			documentText += docScanner.nextLine() + "\n";				
    		}

    		docScanner.close();
    	} catch (FileNotFoundException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	//frame.setSize(0, 0);

    	//createAndShowGUI();
    	output.setText(documentText);
    	currentFileName.setText(documentName);
    	
    	menuBar.remove(suggestionsMenu);
    	suggestionsMenu = new JMenu("Suggestions");
    	//menuSuggestions = new HashMap<>();
    	menuSuggestions = new ArrayList<>();
    	
        menuBar.updateUI();
    }
    
    public void saveDocument() {
      	
		try {
			Path existingDoc = FileSystems.getDefault().getPath(documentName);
			Files.deleteIfExists(existingDoc);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
		try {
			FileWriter fw = new FileWriter(documentName, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.print(output.getText());
			pw.flush();
			pw.close();
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

    }
    
    public void saveDocumentAs() {
    	//TODO
    	try {
			FileWriter fw = new FileWriter(documentName, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.print(output.getText());
			pw.flush();
			pw.close();
			fw.close();
			currentFileName.setText(documentName);
	        menuBar.updateUI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void newDocument() {
    	dictionary = new WordRecommender(dictionaryFileName);
    	documentName = "Untitled Document";
    	newDocument = true;
    	output.setText("");
    	currentFileName.setText(documentName);
    	
    	menuBar.remove(suggestionsMenu);
    	suggestionsMenu = new JMenu("Suggestions");
    	//menuSuggestions = new HashMap<>();
    	menuSuggestions = new ArrayList<>();
    	
        menuBar.updateUI();
    }
  
    public void checkSpelling() {
    	highlighter = (DefaultHighlighter)output.getHighlighter();
    	highlighter.removeAllHighlights();
    	
    	menuBar.remove(suggestionsMenu);
    	suggestionsMenu = new JMenu("Suggestions");
    	//menuSuggestions = new HashMap<>();
    	menuSuggestions = new ArrayList<>();
    	
    	String currentText = output.getText();
    	int overallIndex = 0;
    	Scanner docScanner = new Scanner(currentText);
    	
    	while (docScanner.hasNextLine()) {
    		String currentLine = docScanner.nextLine();
    		String currentWord;
    		
    		while (!currentLine.equals("")) {
    		
    			// trim leading spaces
    			int nonSpaceIndex = PatternChecker.detectNonSpaces(currentLine);
    			if (nonSpaceIndex > -1) {
    				overallIndex += nonSpaceIndex;
        			currentLine = currentLine.substring(nonSpaceIndex);	
    			}
    			
    			// trim leading punctuation
        		int nonPunctuationIndex = PatternChecker.detectNonPunctuation(currentLine);
        		if (nonPunctuationIndex > -1) {
        			overallIndex += nonPunctuationIndex;
            		currentLine = currentLine.substring(nonPunctuationIndex);
        		}
        		
        		currentWord = currentLine;
        		//System.out.println(currentWord);
        				
        		// ignore trailing spaces
        		int spaceIndex = PatternChecker.detectSpaces(currentWord);
        		if (spaceIndex > -1) {
        			currentWord = currentWord.substring(0, spaceIndex);
        		}
        		
        		// ignore trailing punctuation
        		int punctuationIndex = PatternChecker.detectPunctuation(currentWord);
        		if (punctuationIndex > -1) {
        			currentWord = currentWord.substring(0, punctuationIndex);
        		}
        		
        		
        		
        		if(PatternChecker.isBigInteger(currentWord) || PatternChecker.isBigDecimal(currentWord)) {
        			// do nothing
        		}
        		else if(currentWord.equals("")) {
        			if(PatternChecker.detectNonPunctuation(currentLine) == -1) {
        				overallIndex += currentLine.length();
        				currentLine = "";
        			}
        		}
        		else if(!dictionary.checkForExactWord(currentWord.toLowerCase())) {
        			try {
        				// NOTE: the SquigglePainter highlighter is not our code. Please see note in SquigglePainter.java class
        				//Object currentHighlight = highlighter.addHighlight(overallIndex, overallIndex + currentWord.length(), new SquigglePainter(new Color(0xFF0000)));
        				highlighter.addHighlight(overallIndex, overallIndex + currentWord.length(), new SquigglePainter(new Color(0xFF0000)));
        				ArrayList<String> suggestions = dictionary.getWordSuggestions(currentWord.toLowerCase(), 2, 0.7, 4);
        				//wordSuggestions.put(currentHighlight, suggestions);
        				//menuSuggestions.put(currentWord, suggestions);
        				suggestions.add(0, currentWord);
        				menuSuggestions.add(suggestions);
        				
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        		
        		overallIndex += currentWord.length();
        		currentLine = currentLine.substring(currentWord.length());
    			
    		}
    		// add one to the index for each new line
    		overallIndex++;
    		
    	}
    	//System.out.println(highlighter.getHighlights() +  " " + highlighter.getHighlights()[0].getStartOffset() + highlighter.getHighlights()[0].getEndOffset());
    	
    	/*
    	try {
			highlighter.changeHighlight(highlighter.getHighlights()[0], 3, 8);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    	//highlighter.getHighlights()[0]
    	
    	
           	
    	//if (!menuSuggestions.keySet().isEmpty()) {
    	if (menuSuggestions.size() != 0) {
        	
    		int menuIndex = -1;
        	//for (String key : menuSuggestions.keySet()) {
        	for (int i = 0; i < menuSuggestions.size(); i++) {
        		String misspelledWord = menuSuggestions.get(i).get(0);
        		JMenu misspelledWordItem = new JMenu(misspelledWord);
        		suggestionsMenu.add(misspelledWordItem);
        		menuIndex++;
        		
        		JMenuItem ignoreWord = new JMenuItem("Ignore word in document");
        		ignoreWord.addActionListener(new ActionListener() {
    	        	public void actionPerformed(ActionEvent e)
    	        	{
    	        		dictionary.getDictionary().addWordToDictionaries(misspelledWord.toLowerCase());
    	        		dictionary.updateDictionaries();
    	        		
    	        		checkSpelling();
    	        	}
    	        });
        		misspelledWordItem.add(ignoreWord);
        		
        		JMenuItem addWordToDict = new JMenuItem("Add word to dictionary");
        		addWordToDict.addActionListener(new ActionListener() {
    	        	public void actionPerformed(ActionEvent e)
    	        	{
    	        		dictionary.getDictionary().addWordToDictionaries(misspelledWord.toLowerCase());
    	        		dictionary.updateDictionaries();
    	        		File dictionaryDocument = new File(dictionaryFileName);
						try {
							FileWriter fwDictionary = new FileWriter(dictionaryDocument, true);
							PrintWriter pwDictionary = new PrintWriter(fwDictionary);
							pwDictionary.println(misspelledWord.toLowerCase());
							pwDictionary.close();
							fwDictionary.close();
						} catch (IOException e1) {
						/*
						 * If the dictionary file does not exist, tries to create a new dictionary file from the backup dictionary file, and then tries again.
						 */
							try {
								Path backupDictionary = Paths.get(backupDictionaryFileName);
			            		Path currentDictionary = Paths.get(dictionaryFileName);
								Files.copy(backupDictionary, currentDictionary, StandardCopyOption.REPLACE_EXISTING);
								FileWriter fwDictionary = new FileWriter(dictionaryDocument, true);
								PrintWriter pwDictionary = new PrintWriter(fwDictionary);
								pwDictionary.println(misspelledWord.toLowerCase());
								pwDictionary.close();
								fwDictionary.close();
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
						}
    	        		
    	        		checkSpelling();
    	        	}
    	        });
        		
        		
        		misspelledWordItem.add(addWordToDict);
        		if (menuSuggestions.get(i).size() > 1) {
        			misspelledWordItem.addSeparator();
        		}
        		ArrayList<String> misspelledWordSuggestions = menuSuggestions.get(i);
        		for (int j = 1; j < misspelledWordSuggestions.size(); j++) {
        			String suggestedWord = misspelledWordSuggestions.get(j);
        			JMenuItem suggestedWordItem = new JMenuItem(suggestedWord); 
        			int highlighterIndex = menuIndex;
        			//int suggestedArrayListIndex = j;
        			suggestedWordItem.addActionListener(new ActionListener() {
        	        	public void actionPerformed(ActionEvent e)
        	        	{
        	        		
        	        		Highlighter.Highlight currentHighlight = highlighter.getHighlights()[highlighterIndex];
        	        		int wordStart = currentHighlight.getStartOffset();
        	        		int wordEnd = currentHighlight.getEndOffset();
        	        		output.setSelectionStart(wordStart);
        	        		output.setSelectionEnd(wordEnd);
        	        		output.replaceSelection(suggestedWord);
        	        		highlighter.removeHighlight(currentHighlight);
        	        		output.setCaretPosition(wordEnd);
        	        		 /*
        	        		misspelledWordSuggestions.remove(suggestedArrayListIndex);
        	        		suggestionsMenu.remove(suggestedWordItem);
        	        		suggestionsMenu.updateUI();
        	        		menuBar.updateUI();
        	        		*/
        	        		checkSpelling();
        	        	}
        	        });
        			misspelledWordItem.add(suggestedWordItem);
        		}

        	}

        	menuBar.add(suggestionsMenu, 4);
        }
    	
    	menuBar.updateUI();
    	docScanner.close();
    	
    }
    /*
    public void createRightClickMenu() {
    	
    	ArrayList<String> tempInnerArrayList = new ArrayList<>();
    	tempInnerArrayList.add("OriginalWord");
    	tempInnerArrayList.add("Word1");
    	tempInnerArrayList.add("Word2");
    	wordSuggestions.add(tempInnerArrayList);
    	
    	JPopupMenu rightClickMenu = new JPopupMenu(); 
    	
    	for (int j = 1; j < wordSuggestions.size(); j++) {
    		
    		rightClickMenu.add(new JMenuItem(wordSuggestions.get(0).get(j)));
    		
    	}
    	
    	
    }
    */
    
    public Container createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
 
        //Create a scrolled text area.
        output = new JTextPane();
        output.setEditable(true);
        scrollPane = new JScrollPane(output);
 
        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.CENTER);
 
        return contentPane;
    }
  
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    
    // was private, made public so can access from IOInterface
    public void createAndShowGUI() {
    	frame = new JFrame(titleBar);
    	
    	//frame.setLocationRelativeTo(null);
    	frame.setLocation(dim.width/2-frame.getSize().width/2 - 400, dim.height/2-frame.getSize().height/2 - 400);
    	
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        TextDocumentUI doc = new TextDocumentUI();
        
        frame.setJMenuBar(doc.createMenuBar());
        frame.setContentPane(doc.createContentPane());
 
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        
    }
}
