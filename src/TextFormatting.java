import java.io.PrintWriter;

/**
 * Class that reads and updates the length of each line in user's document by character
 * and, before exceeding the length set by charsPerLine, adds a line break.
 * @author aschn
 *
 */
public class TextFormatting {
	private int lineCharLength = 0;
	private int charsPerLine = 80;
	
	/**
	 * Insert line breaks after designated number of characters, with 80 characters as the default
	 * @param charsPerLine
	 */
	public void addLineBreaks (PrintWriter pw) {
		
		if (this.lineCharLength >= this.charsPerLine) {
			pw.println();
			this.lineCharLength = 0;
		}
		
	}
	


	public int getCharsPerLine() {
		return charsPerLine;
	}



	public void setCharsPerLine(int charsPerLine) {
		this.charsPerLine = charsPerLine;
	}



	public int getLineCharLength() {
		return lineCharLength;
	}

	public void setLineCharLength(int lineCharLength) {
		this.lineCharLength = lineCharLength;
	}
	
	

}
