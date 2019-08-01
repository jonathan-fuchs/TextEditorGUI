
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class SpellingAnalysisTest {

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void testTotalWordsMisspelled() {
		SpellingAnalysis test = new SpellingAnalysis();
		test.totalWordsMisspelled();
		// test with "the cat went ouut of the dor"
		fail("Not yet implemented");
	}

}
