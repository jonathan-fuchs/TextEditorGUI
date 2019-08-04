import java.io.*;
import java.util.*;


/**
 * Class that adds a text-to-database function to the spell checking program
 * 
 * <p>Class functionality includes:<ul>
 * <li> Prompting user to specify the number of columns and rows he or she wants in the table
 * <li> Asking the user for the column names
 * <li> Prompting the user to input the data starting with each column and finishing with the last row
 * <li> We have additions that deal with spacing if the column titles are long
 * <li> After all the data is entered, the user is given a notification that his or her table is created as a "Database.txt" file 
 * </ul></p>
 * 
 * 
 * @author Melissa Wu
 *
 */
public class DataToTextFile {
	private int columnNum;
	private int rowNum;
	
	/**
	 * Inserts data into 
	 */
	
	public void insert() {
	ArrayList<String> ColumnNames = new ArrayList<String>();

	List<List<String>> listOfLists = new ArrayList<List<String>>();

	Scanner sc = new Scanner(System.in);
	System.out.println("How many columns do you want in your table");
	columnNum = sc.nextInt();
	
	System.out.println("How many data rows do you want in your table");
	rowNum = sc.nextInt();
	
	/*
	 * Asks for column names
	 */
	
	int counter = 1;

	for (int i = 0; i < columnNum; i++, counter++) { 
		System.out.print("Enter the name of column " + counter + ":");
		String name = "";
		while (sc.hasNextLine()) {
			name = sc.nextLine();	
			if (!name.equals("")) {
				break;
			}			
		}
		ColumnNames.add(name);	
	}

	/*
	 * Asks for row data
	 */

	for (int k = 1; k <= rowNum; k++) {
		ArrayList<String> temp = new ArrayList<String>();
		listOfLists.add(temp);
		for (int j = 1; j <= columnNum; j++) {
			String fn = "";
			System.out.print("Enter data for column " + j + ", row " + k + ":");
			while (sc.hasNextLine()) {
				fn=sc.nextLine();
				temp.add(fn);
				if (!fn.equals("")) {
					break;
				}	
			}
		}
	}
	
	try
	  {
	   File f=new File("Database.txt");
	   PrintWriter pw=new PrintWriter(new FileOutputStream(f));

	   /*
	    * Problem with appending all the values!
	    */
	   
	   for (String col : ColumnNames) {
		   pw.print(col);
		   pw.print("\t");
	   }
	   pw.print("\n");
	   
	   for (List<String> element : listOfLists) {
		   for (String value : element) {
			   pw.print(value);
			   pw.print("\t");
		   }		  
		   pw.print("\n");
	   }
	   pw.flush();
	   pw.close();
	   
	  }
	  catch(Exception e){}  
	 }

	
	/**
	 * Displays data in the text file
	 */
	
	public void display() {
		try {
		
		BufferedReader br = new BufferedReader(new FileReader("Database.txt"));
		String s = "";
			while( (s = br.readLine()) != null ) {
				String data[] = new String[columnNum];
				data=s.split(",");
				for(int i = 0; i < columnNum; i++) {
					System.out.print(data[i] + " ");
				}
			}
		}
		catch(Exception e) {}
	}

}
