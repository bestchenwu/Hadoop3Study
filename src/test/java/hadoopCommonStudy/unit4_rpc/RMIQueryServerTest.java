package hadoopCommonStudy.unit4_rpc;

import org.junit.Test;

public class RMIQueryServerTest {

    @Test
    public void testStringFormat() {
        String host = "192.168.11.111";
        String url = String.format("rmi://%s:12090/queryFileStatus", host);
        System.out.println("url=" + url);
    }

}
