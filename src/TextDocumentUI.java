
import java.awt.*;
//import java.awt.List;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
//import javax.swing.text.Highlighter;
//import javax.swing.text.Highlighter.Highlight;
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
import javax.swing.Timer;
 
/**
 * I found this tutorial immensely helpful: https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
 * specifically, this: 
 * https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/MenuLookDemoProject/src/components/MenuLookDemo.java
 * 
 * @author Jonathan
 *
 */
public class TextDocumentUI {
	
	private JFrame frame;
	private JTextPane output;
	private JScrollPane scrollPane;
	private String documentName = "Untitled Document";
	private String titleBar = "Document Editor GUI";
	private JMenuBar menuBar;
	private JMenuItem suggestionsMenu = new JMenu("Suggestions");;
	private JMenu currentFile;
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

	private String documentText;
	private JDialog popUpWindow;
	private JTextField popUpWindowTextField;
    
	private int pause = 800;
	private int speed = 300;
	private Timer timer;
	private boolean alwaysOnTop = false;   
	private boolean newDocument = true;
	private File document;
	private DefaultHighlighter highlighter;
	
	private String dictionaryFileName = "engDictionary.txt";
	private String backupDictionaryFileName = "engDictionary_backup.txt";
	private WordRecommender dictionary = new WordRecommender(dictionaryFileName);
	private ArrayList<ArrayList<String>> menuSuggestions = new ArrayList<>();
	//HashMap<Object, ArrayList<String>> wordSuggestions = new HashMap<>();
	//HashMap<String, ArrayList<String>> menuSuggestions = new HashMap<>();
	
	
    
    public JMenuBar createMenuBar() {
        JMenu fileMenu, editMenu, formatMenu, reviewMenu;
        JMenuItem menuItemNew, menuItemSave, menuItemSaveAs, menuItemOpen, menuItemCopy, menuItemCut, menuItemPaste, menuItemSelectAll, menuItemSpellCheck, menuItemResetDictionary, menuItemExit, menuItemHighlight, menuItemRemoveAllHighlights;
        Action copy = new DefaultEditorKit.CopyAction();
        Action cut = new DefaultEditorKit.CutAction();
        Action paste = new DefaultEditorKit.PasteAction();
 
        //Create the menu bar.
        menuBar = new JMenuBar();
 
        //Build the first menu.
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription("File menu");
        menuBar.add(fileMenu);
 
        //a group of JMenuItems
        menuItemNew = new JMenuItem("New", KeyEvent.VK_N);
        menuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menuItemNew.getAccessibleContext().setAccessibleDescription("Create a new file");
        menuItemNew.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		newDocument();
        	}
        });
        
        
        
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
        
        
        

        menuItemSaveAs = new JMenuItem("Save As");
        menuItemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK + + ActionEvent.SHIFT_MASK));
        menuItemSaveAs.getAccessibleContext().setAccessibleDescription("Save As for the current file");
        menuItemSaveAs.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		createPopUpWindow("Save As");
        	}
        });
        
        
        menuItemOpen = new JMenuItem("Open", KeyEvent.VK_O);
        menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItemOpen.getAccessibleContext().setAccessibleDescription("Open an existing file");
        menuItemOpen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		createPopUpWindow("Open");
        		
        		//javax.swing.SwingUtilities.invokeLater(new Runnable() {public void run() {frame.setTitle(documentName);}});
        		
        	}
        });
        
        menuItemExit = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        menuItemExit.getAccessibleContext().setAccessibleDescription("Close the program");
        menuItemExit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		System.exit(0);
        	}
        });
        
        fileMenu.add(menuItemNew);
        fileMenu.add(menuItemSave);
        fileMenu.add(menuItemSaveAs);
        fileMenu.add(menuItemOpen);
        
        fileMenu.addSeparator();
        
        fileMenu.add(menuItemExit);
 
      //Build second menu in the menu bar.
        
        editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        editMenu.getAccessibleContext().setAccessibleDescription("Edit Menu");
        menuBar.add(editMenu);
        
        menuItemCopy = new JMenuItem(copy);
        menuItemCopy.setMnemonic(KeyEvent.VK_C);
        menuItemCopy.setText("Copy");
        menuItemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItemCopy.getAccessibleContext().setAccessibleDescription("Copies selected text in the current document");
       
        menuItemCut = new JMenuItem(cut);
        menuItemCut.setMnemonic(KeyEvent.VK_T);
        menuItemCut.setText("Cut");
        menuItemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItemCut.getAccessibleContext().setAccessibleDescription("Cuts selected text in the current document");
        
        menuItemPaste = new JMenuItem(paste);
        menuItemPaste.setMnemonic(KeyEvent.VK_P);
        menuItemPaste.setText("Paste");
        menuItemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItemPaste.getAccessibleContext().setAccessibleDescription("Pastes selected text in the current document");
        
        
        
        
        menuItemSelectAll = new JMenuItem("Select All", KeyEvent.VK_A);
        menuItemSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menuItemSelectAll.getAccessibleContext().setAccessibleDescription("Select all text in the current document");
        menuItemSelectAll.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		output.selectAll();
        	}
        });
        
        editMenu.add(menuItemCopy);
        editMenu.add(menuItemCut);
        editMenu.add(menuItemPaste);
        editMenu.addSeparator();
        
        editMenu.add(menuItemSelectAll);
        
        
        
      //Build third menu in the menu bar.
        
        formatMenu = new JMenu("Format");
        formatMenu.setMnemonic(KeyEvent.VK_O);
        formatMenu.getAccessibleContext().setAccessibleDescription("Format Menu");
        menuBar.add(formatMenu);
        
        menuItemHighlight = new JMenuItem("Highlight Text", KeyEvent.VK_H);
        menuItemHighlight.getAccessibleContext().setAccessibleDescription("Highlights text in yellow");
        menuItemHighlight.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		highlighter = (DefaultHighlighter)output.getHighlighter();
        		highlighter.setDrawsLayeredHighlights(false);
        		try {
        			//Highlight[] currentHighlights = painter.getHighlights();
        			
        			//for (int i = output.getSelectionStart(); i <= output.getSelectionEnd(); i++) {
        				
        				
        				
        			//}
        			
        			highlighter.addHighlight(output.getSelectionStart(), output.getSelectionEnd(), new DefaultHighlighter.DefaultHighlightPainter(new Color(0xFAED27)));
					output.setCaretPosition(output.getSelectionEnd());
					
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        
        menuItemRemoveAllHighlights = new JMenuItem("Remove Highlights", KeyEvent.VK_R);
        menuItemRemoveAllHighlights.getAccessibleContext().setAccessibleDescription("Removes all highlights");
        menuItemRemoveAllHighlights.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		highlighter = (DefaultHighlighter)output.getHighlighter();
        		highlighter.removeAllHighlights();
        	}
        });
        
        formatMenu.add(menuItemHighlight);
        formatMenu.add(menuItemRemoveAllHighlights);
        
        
      //Build fourth menu in the menu bar.
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
        
        reviewMenu.add(menuItemSpellCheck);
        reviewMenu.add(menuItemResetDictionary);
        
        //Fifth menu is the suggestionsMenu and is created when running spellCheck

        
        
        // Sixth menu in menu bar is just a label
        currentFile = new JMenu(documentName);
        
        menuBar.add(currentFile);
        
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
    	currentFile.setText(documentName);
    	
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
			currentFile.setText(documentName);
	        menuBar.updateUI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void newDocument() {
    	documentName = "Untitled Document";
    	newDocument = true;
    	output.setText("");
    	currentFile.setText(documentName);
    	
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
        		else if(!currentWord.equals("") && !dictionary.checkForExactWord(currentWord.toLowerCase())) {
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
							// TODO Auto-generated catch block
							e1.printStackTrace();
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
                
        timer = new Timer(speed, new ActionListener() {public void actionPerformed(ActionEvent e){
        	
        	//frame.toFront();
        	if (alwaysOnTop == false) {
        		frame.setAlwaysOnTop(true);
        		alwaysOnTop = false;
        	}
        	else {
        		//request focus doesn't work
        		//frame.requestFocus();
        		//frame.toFront();
        		//frame.setVisible(true);
        		frame.setAlwaysOnTop(false);
        		timer.stop();
        	}

        }});
        timer.setInitialDelay(pause);
        timer.start(); 
        
        //

    }
}
