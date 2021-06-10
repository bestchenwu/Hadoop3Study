package zookeeperStudy.unit7;

import org.apache.zookeeper.server.LogFormatter;
import org.junit.Test;

/**
 * 使用zookeeper自带的日志格式化工具类查看事务日志文件
 */
public class LogFormatterTest {

    @Test
    public void testLogDump() throws Exception {
        String log = "D:\\softWare\\apache-zookeeper-3.5.8-bin\\dataDir\\version-2\\log.b9";
        LogFormatter.main(new String[]{log});
    }
}
