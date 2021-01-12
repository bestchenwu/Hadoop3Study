package java8Study.unit7;

import java.util.stream.Collector;

public class WordCounter {

    private int wordCount;
    private boolean lastSpace;

    public WordCounter() {
        this.wordCount = 0;
        this.lastSpace = false;
    }

    public WordCounter(int wordCount, boolean lastSpace) {
        this.wordCount = wordCount;
        this.lastSpace = lastSpace;
    }

    public WordCounter acculamtor(Character character) {
        if (Character.isWhitespace(character)) {
            return lastSpace ? this : new WordCounter(wordCount, true);
        } else {
            return lastSpace ? new WordCounter(wordCount + 1, false) : this;
        }
    }

    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(this.wordCount + wordCounter.wordCount, wordCounter.lastSpace);
    }

    public int getCounter() {
        return this.wordCount;
    }

    //public static void main(String[] args) {
//        IntStream.range(0,WORD)
//    }
}
