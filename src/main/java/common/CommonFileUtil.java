package common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 通用文件工具类
 *
 * @author chenwu on 2019.12.10
 */
public class CommonFileUtil {

    /**
     * 读取文件,并根据是否去重汇总
     *
     * @param inputFileName
     * @param isDistinct
     * @return List<String>
     * @throws IOException
     */
    public static List<String> readFile(String inputFileName, boolean isDistinct) throws IOException {
        FileReader fileReader = new FileReader(inputFileName);
        BufferedReader br = new BufferedReader(fileReader);
        if (isDistinct) {
            Set<String> set = br.lines().collect(Collectors.toSet());
            List<String> list = new ArrayList<>(set);
            br.close();
            fileReader.close();
            return list;
        } else {
            List<String> list = br.lines().collect(Collectors.toList());
            br.close();
            fileReader.close();
            return list;
        }
    }

    /**
     * 向指定文件写入list
     *
     * @param outputFileName
     * @param list
     * @throws IOException
     * @author chenwu on 2019.12.10
     */
    public static void writeFile(String outputFileName, List<String> list) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFileName, false);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        list.stream().forEach((item) -> {
                    try {
                        bufferedWriter.write(item+"\n");
                    } catch (IOException e) {
                        //ignore the IOException
                    }
                }
        );
        bufferedWriter.flush();
        bufferedWriter.close();
        fileWriter.close();
    }
}
