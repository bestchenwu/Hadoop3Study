package java8Study.unit7;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WordCounterTest {

    private int countWord(Stream<Character> characterStream) {
        WordCounter result = characterStream.reduce(new WordCounter(0, false), WordCounter::acculamtor, WordCounter::combine);
        return result.getCounter();
    }

    public static void main(String[] args) {
        WordCounterTest wordCounterTest = new WordCounterTest();
        int length = WordCountUtil.WORD.length();
//        Stream<Character> characterStream = IntStream.rangeClosed(0, length-1).mapToObj(i -> WordCountUtil.WORD.charAt(i));
        //使用自定义的拆分器
        WordCounterSpliterator spliterator = new WordCounterSpliterator(WordCountUtil.WORD);
        Stream<Character> characterStream = StreamSupport.stream(spliterator, true);
        int result = wordCounterTest.countWord(characterStream);
        System.out.println("counter:" + result);
    }
}
