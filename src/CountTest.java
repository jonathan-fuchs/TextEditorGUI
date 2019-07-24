import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CountTest {

	@Test
	void testIOInterface() {
		
		IOInterface test = new IOInterface("TestingCount.txt");
		
		fail("Not yet implemented");
	}

	@Test
	void testCheckDocument() {
		IOInterface test = new IOInterface("TestingCount.txt");
		
		test.getCharCounts();
		// Answer = 114
		test.getNumCorrectWords();
		// Answer = 18
		test.getNumTotalWords();
		// Answer = 23
		test.getPercCorrectWords();
		// Answer = 0.78
		
		
		fail("Not yet implemented");
	}

}
