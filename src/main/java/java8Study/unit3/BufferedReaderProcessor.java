package java8Study.unit3;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 函数式接口表示只包含一个抽象方法的接口
 */
@FunctionalInterface
public interface BufferedReaderProcessor {

    public String process(BufferedReader br) throws IOException;
}
