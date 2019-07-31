
import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultEditorKit;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import javax.swing.JPanel;
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
	private String documentName = "Document Editor GUI";

	private String documentText;
	private JDialog popUpWindow;
	private JTextField popUpWindowTextField;
    
	private int pause = 800;
	private int speed = 300;
	private Timer timer;
	private boolean alwaysOnTop = false;   
	private boolean newDocument = true;
	private File document;
    
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu fileMenu, editMenu, reviewMenu;
        JMenuItem menuItemNew, menuItemSave, menuItemSaveAs, menuItemOpen, menuItemCopy, menuItemCut, menuItemPaste, menuItemSelectAll, menuItemSpellCheck, menuItemExit;
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
        
        reviewMenu.add(menuItemSpellCheck);
          
        
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
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void newDocument() {
    	documentName = "Document Editor GUI";
    	newDocument = true;
    	output.setText("");
    	
    }
  
    public void checkSpelling() {
    	System.out.println("On my TODO list!");
    }
    
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
    	frame = new JFrame(documentName);
    	
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
