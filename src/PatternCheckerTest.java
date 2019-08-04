import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PatternCheckerTest {
	
	@Test 
	void isIntegerTest() {
		String testInput1 = "1000";
		String testInput2 = "10.00";
		String testInput3 = "Tomatoes";
		String testInput4 = "10000000000000000000";
		assertEquals( true, PatternChecker.isInteger(testInput1),  "Check that isInteger detects integers" );
		assertEquals( false, PatternChecker.isInteger(testInput2),  "Check that isInteger fails with decimal numbers" );
		assertEquals( false, PatternChecker.isInteger(testInput3),  "Check that isInteger fails with non-number strings" );
		assertEquals( false, PatternChecker.isInteger(testInput4),  "Check that isInteger fails with really big integers" );
	}
		
	@Test
	void bigIntegerTest() {
		String testInput1 = "10000000000000000000";
		String testInput2 = "10.00";
		String testInput3 = "Baloney Sandwich";
		assertEquals( true, PatternChecker.isBigInteger(testInput1),  "Check that isBigInteger detects integers" );
		assertEquals( false, PatternChecker.isBigInteger(testInput2),  "Check that isBigInteger fails with decimal numbers" );
		assertEquals( false, PatternChecker.isBigInteger(testInput3),  "Check that isBigInteger fails with non-number strings" );
	}

	@Test
	void bigDecimalTest() {
		String testInput1 = "10.00";
		String testInput2 = "10000000000000000000";
		String testInput3 = "Mayonaise";
		assertEquals( true, PatternChecker.isBigDecimal(testInput1),  "Check that isBigDecimal detects decimal numbers" );
		assertEquals( true, PatternChecker.isBigDecimal(testInput2),  "Check that isBigDecimal detects non-decimal numbers" );
		assertEquals( false, PatternChecker.isBigDecimal(testInput3),  "Check that isBigDecimal fails with non-number strings" );
	}
	
	@Test
	void detectPunctuationTest() {
		String testInput1 = "No punctuation";
		String testInput2 = "I have a period at the end.";
		String testInput3 = "I have a comma, did you notice?";
		String testInput4 = "Repeated punctuation... How fun.";
		assertEquals( -1, PatternChecker.detectPunctuation(testInput1),  "Check that detectPunctuation does not give false positives" );
		assertEquals( testInput2.length() - 1, PatternChecker.detectPunctuation(testInput2),  "Check that detectPunctuation detects periods" );
		assertEquals( 14, PatternChecker.detectPunctuation(testInput3),  "Check that detectPunctuation detects the first punctuation mark" );
		assertEquals( 20, PatternChecker.detectPunctuation(testInput4),  "Check that detectPunctuation detects the first punctuation mark when that punctuation mark is used multiple times" );
	}
	
	@Test 
	void detectNonPunctuationNonSpaceTest() {
		String testInput1 = ",.!?\\\\-;: ";
		String testInput2 = " No punctuation";
		assertEquals( -1, PatternChecker.detectNonPunctuationNonSpace(testInput1),  "Check that detectNonPunctuation does not give false positives" );
		assertEquals( 1, PatternChecker.detectNonPunctuationNonSpace(testInput2),  "Check that detectNonPunctuation skips spaces" );
	}
	
	@Test
	void detectNonSpacesTest() {
		String testInput1 = " 	\t\n";
		String testInput2 = ",.!?\\\\-;: ";
		String testInput3 = " No punctuation";
		assertEquals( -1, PatternChecker.detectNonSpaces(testInput1),  "Check that detectNonSpaces does not give false positives" );
		assertEquals( 0, PatternChecker.detectNonSpaces(testInput2),  "Check that detectNonSpaces detects punctuation" );
		assertEquals( 1, PatternChecker.detectNonSpaces(testInput3),  "Check that detectNonSpaces skips spaces" );
		
	}
	
	@Test
	void detectSpacesTest() {
		String testInput1 = "No-Spaces";
		String testInput2 = ",.!?\\\\-;: ";
		String testInput3 = " 	\t\n";
		assertEquals( -1, PatternChecker.detectSpaces(testInput1),  "Check that detectSpaces does not give false positives" );
		assertEquals( testInput2.length() - 1, PatternChecker.detectSpaces(testInput2),  "Check that detectSpaces skips punctuation" );
		assertEquals( 0, PatternChecker.detectSpaces(testInput3),  "Check that detectSpaces detects spaces" );
	}
	
	@Test
	void detectSentenceEndingPunctuationTest() {
		String testInput1 = "No punctuation";
		String testInput2 = "I have a period at the end.";
		String testInput3 = "I have a comma, did you notice?";
		String testInput4 = "The last test case; testing is fun!?!?";
		assertEquals( -1, PatternChecker.detectSentenceEndingPunctuation(testInput1),  "Check that detectSentenceEndingPunctuation does not give false positives" );
		assertEquals( testInput2.length() - 1, PatternChecker.detectSentenceEndingPunctuation(testInput2),  "Check that detectSentenceEndingPunctuation detects periods" );
		assertEquals( testInput3.length() - 1, PatternChecker.detectSentenceEndingPunctuation(testInput3),  "Check that detectSentenceEndingPunctuation skips commas but detects question marks" );
		assertEquals( testInput4.length() - 4, PatternChecker.detectSentenceEndingPunctuation(testInput4),  "Check that detectSentenceEndingPunctuation detects the first punctuation mark in a string of qualifying punctuation marks" );
	}
	
}
