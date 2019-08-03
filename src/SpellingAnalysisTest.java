
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class SpellingAnalysisTest {

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

//	@Test
//	void testTotalWordsMisspelled() {
//		SpellingAnalysis test = new SpellingAnalysis();
//		test.totalWordsMisspelled();
//		// test with "the cat went ouut of the dor"
//		fail("Not yet implemented");
//	}
	
	@Test
	void testGetSyllablesInWord1() {
		SpellingAnalysis test = new SpellingAnalysis();
		test.getSyllablesInWord("buggy");
		assertEquals(2, test.getSyllableCounts().get(0));
	}
	
	@Test
	void testGetSyllablesInWord2() {
		SpellingAnalysis test = new SpellingAnalysis();
		test.getSyllablesInWord("said");
		assertEquals(1, test.getSyllableCounts().get(0));
	}
	
	@Test
	void testGetSyllablesInWord3() {
		SpellingAnalysis test = new SpellingAnalysis();
		test.getSyllablesInWord("mississippi");
		assertEquals(4, test.getSyllableCounts().get(0));
	}
	
	@Test
	void testGetSyllablesInWord4() {
		SpellingAnalysis test = new SpellingAnalysis();
		test.getSyllablesInWord("captain");
		assertEquals(2, test.getSyllableCounts().get(0));
	}
	
	@Test
	void testGetSyllablesInWord5() {
		SpellingAnalysis test = new SpellingAnalysis();
		test.getSyllablesInWord("aptitude");
		assertEquals(4, test.getSyllableCounts().get(0));
	}

}
