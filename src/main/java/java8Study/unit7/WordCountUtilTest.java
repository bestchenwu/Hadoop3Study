package java8Study.unit7;

import static java8Study.unit7.WordCountUtil.*;
import org.junit.Test;

public class WordCountUtilTest {

    @Test
    public void testWordCounter() {

        int wordCounter = WordCountUtil.countWord(WORD);
        System.out.println("wordCounter=" + wordCounter);
    }
}
