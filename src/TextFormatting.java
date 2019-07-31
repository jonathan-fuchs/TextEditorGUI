
public class TextFormatting {
	private int lineCharLength = 0;
	private int charsPerLine = 80;
	
	/**
	 * Insert line breaks after designated number of characters, with 80 characters as the default
	 * @param charsPerLine
	 */
	public void addLineBreaks () {
		int lengthOfLine;
		if (this.charsPerLine < 6) {
			lengthOfLine = 80;
		}
		else {
			lengthOfLine = this.charsPerLine;
		}
		
		if (this.lineCharLength >= lengthOfLine) {
			System.out.println();
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
