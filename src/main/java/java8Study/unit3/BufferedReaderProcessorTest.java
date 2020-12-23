package java8Study.unit3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferedReaderProcessorTest {

    public String processFile(BufferedReaderProcessor processor) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader("D:\\资料\\美囤妈妈\\BI\\study_git\\Hadoop3Study\\src\\main\\resources\\test.txt"))){
            return processor.process(br);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReaderProcessorTest bufferedReaderProcessorTest = new BufferedReaderProcessorTest();
        //一次输出一行
        //String output = bufferedReaderProcessorTest.processFile(br->br.readLine());
        //一次输出两行
        String output = bufferedReaderProcessorTest.processFile(br->br.readLine()+br.readLine());
        System.out.println(output);
    }
}
