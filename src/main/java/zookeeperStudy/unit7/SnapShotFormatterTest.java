package zookeeperStudy.unit7;

import org.apache.zookeeper.server.SnapshotFormatter;
import org.junit.Test;

/**
 * 利用zookeeper提供的SnapShotFormatter来获取数据快照内容
 *
 * @author chenwu on 2021.6.9
 */
public class SnapShotFormatterTest {

    @Test
    public void testDumpSnapShot() throws Exception {
        String snapshot = "D:\\softWare\\apache-zookeeper-3.5.8-bin\\dataDir\\version-2\\snapshot.b8";
        SnapshotFormatter.main(new String[]{snapshot});
    }
}
