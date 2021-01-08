package java8Study.unit7;

public class WordCountUtil {

    public static final String WORD =
            " Nel mezzo del cammin di nostra vita " +
                    "mi ritrovai in una selva oscura" +
                    " ché la dritta via era smarrita ";

    /**
     * 统计单词个数
     *
     * @param s
     * @return 单词个数
     * @author chenwu on 2021.1.18
     */
    public static int countWord(String s) {
        int counter = 0;
        boolean lastSpace = false;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) {
                    counter++;
                }
                lastSpace = false;
            }
        }
        return counter;
    }
}
