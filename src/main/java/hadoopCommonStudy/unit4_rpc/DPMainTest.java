package hadoopCommonStudy.unit4_rpc;

import org.junit.Test;

public class DPMainTest {

    @Test
    public void testProxy(){
        PDQueryStatus pdQueryStatus = DPMain.create(new PDQueryStatusImpl());
        RMIFileStatus fileStatus = pdQueryStatus.getFileStatus("D:\\newJob.txt");
        System.out.println("fileStatus="+fileStatus);
    }
}
