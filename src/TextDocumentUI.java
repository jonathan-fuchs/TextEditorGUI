
import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
	
	JFrame frame;
    JTextPane output;
    JScrollPane scrollPane;
    String documentName = "Document Editor GUI";

	String documentText;
    JDialog popUpWindow;
    JTextArea popUpWindowTextArea;
    
    int pause = 800;
    int speed = 300;
    Timer timer;
    boolean alwaysOnTop = false;   
    
    File document;
    
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu fileMenu, editMenu, reviewMenu;
        JMenuItem menuItemNew, menuItemSave, menuItemSaveAs, menuItemOpen, menuItemSelectAll, menuItemSpellCheck, menuItemExit;
 
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
        		if (!documentName.equals("Document Editor GUI")) {
        			saveDocument();
        		}
        		else {
        			saveDocumentAs();
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
        editMenu.setMnemonic(KeyEvent.VK_A);
        editMenu.getAccessibleContext().setAccessibleDescription("Edit Menu");
        menuBar.add(editMenu);
        
        menuItemSelectAll = new JMenuItem("Select All", KeyEvent.VK_A);
        menuItemSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        menuItemSelectAll.getAccessibleContext().setAccessibleDescription("Select all text in the current document");
        menuItemSelectAll.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		output.selectAll();
        	}
        });
        
        editMenu.add(menuItemSelectAll);
        
        
        
      //Build third menu in the menu bar.
        reviewMenu = new JMenu("Review");
        reviewMenu.setMnemonic(KeyEvent.VK_R);
        reviewMenu.getAccessibleContext().setAccessibleDescription("Review Menu");
        menuBar.add(reviewMenu);
        
        menuItemSpellCheck = new JMenuItem("Spell Check", KeyEvent.VK_L);
        menuItemSpellCheck.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        menuItemSpellCheck.getAccessibleContext().setAccessibleDescription("Spell check the current document");
        
        reviewMenu.add(menuItemSpellCheck);
          
        
        return menuBar;
    }
    
    public void createPopUpWindow(String command) {
    	ActionListener openButtonPressed = new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			documentName = popUpWindowTextArea.getText();

    			if (command.equals("Open")) {
    				openDocument();
    			}
    			else if (command.equals("Save As")) {
    				saveDocumentAs();
    			}
    			
    			popUpWindow.dispose();
    		}
    	};

    	class demoKL implements KeyListener {
    		public void keyTyped(KeyEvent e) {
    			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
    				documentName = popUpWindowTextArea.getText();
    				documentName = documentName.substring(0, documentName.length() - 1);

    				if (command.equals("Open")) {
        				openDocument();
        			}
    				else if (command.equals("Save As")) {
        				saveDocumentAs();
        			}

    				popUpWindow.dispose();
    			}
    		}
    		public void keyPressed(KeyEvent e) {}
    		public void keyReleased(KeyEvent e) {}
    	}

    	popUpWindow = new JDialog(frame, "Type in file name");
    	popUpWindow.setSize(218, 70);
    	popUpWindow.setVisible(true);
    	popUpWindow.setResizable(false);
    	popUpWindow.setAlwaysOnTop(true);
    	popUpWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    	Container c1 = popUpWindow.getContentPane();
		c1.setSize(200, 70);
		popUpWindowTextArea = new JTextArea();
		popUpWindowTextArea.setWrapStyleWord(true);
		popUpWindowTextArea.setLineWrap(true);
		popUpWindowTextArea.setLocation(0, 0);
		popUpWindowTextArea.setBounds(0, 0, 150, 70);
		popUpWindowTextArea.setRows(2);
		demoKL kl = new demoKL();
		popUpWindowTextArea.addKeyListener(kl);
		c1.add(popUpWindowTextArea, BorderLayout.WEST);
		
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
			pw.close();
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

    }
    
    public void saveDocumentAs() {
    	//TODO
    	
    }
    
    public void newDocument() {
    	documentName = "Document Editor GUI";
    	output.setText("");
    	
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
