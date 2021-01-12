package java8Study.unit7;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * 单词拆分的分割器
 *
 * @author chenwu on 2021.1.12
 */
public class WordCounterSpliterator implements Spliterator<Character> {

    private final String word;
    private int currentPos;

    public WordCounterSpliterator(String word) {
        this.word = word;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(word.charAt(currentPos++));
        return currentPos < word.length();
    }

    @Override
    public Spliterator<Character> trySplit() {
        int size = word.length()-currentPos;
        if(size<10){
            return null;
        }
        int splitPos = currentPos+size/2;
        for(int i = splitPos;i<word.length();i++){
            if(Character.isWhitespace(word.charAt(i))){
                WordCounterSpliterator newSpliter = new WordCounterSpliterator(word.substring(currentPos,i));
                currentPos = i;
                return newSpliter;
            }
        }
        return null;
    }

    @Override
    public long estimateSize() {
        return word.length()-currentPos;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}
