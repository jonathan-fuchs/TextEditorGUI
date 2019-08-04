import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class vowelConsonantAnalysisTest {
    
    @Test
    void testGetVowelCountInWord1() {
        vowelConsonantAnalysis vowel = new vowelConsonantAnalysis();
        vowel.vowelConsontantCounts("apple");
        assertEquals(2, vowel.getTotalVowelCount());
    }
    
    @Test
    void testGetConsonantInWord2() {
        vowelConsonantAnalysis consonant = new vowelConsonantAnalysis();
        consonant.vowelConsontantCounts("testing");
        assertEquals(5, consonant.getTotalConsonantCount());
    }
    
    @Test
    void testAverageVowelWord3() {
        vowelConsonantAnalysis test = new vowelConsonantAnalysis();
        ArrayList<Integer> array = new ArrayList<Integer>();
        array.add(1);
        array.add(2);
        array.add(3);
        test.calculateAverageVowelandConsonantCounts(array);
        assertEquals(2, test.getAverageCounts());
    }
    
    @Test
    void testAverageVowelWord4() {
        vowelConsonantAnalysis test = new vowelConsonantAnalysis();
        ArrayList<Integer> vowels = new ArrayList<Integer>();
        vowels.add(15);
        vowels.add(10);
        vowels.add(4);
        vowels.add(11);
        test.calculateAverageVowelandConsonantCounts(vowels);
        assertEquals(10, test.getAverageCounts());
    }
}
