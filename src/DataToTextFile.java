import java.io.*;
import java.util.*;

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
		System.out.println("Enter the name of column " + counter + ":");
		String name =sc.nextLine();
		sc.hasNextLine();
		ColumnNames.add(name);
		
	}

	/*
	 * Asks for row data
	 */
	
	for (int k = 1; k <= rowNum; k++) {
		ArrayList<String> temp = new ArrayList<String>();
		listOfLists.add(temp);
		for (int j = 1; j <= columnNum; j++) {
		  System.out.println("Enter data for column " + j + ", row " + k + ":");
		  String fn=sc.nextLine();
		  sc.hasNextLine();
		  temp.add(fn);

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
