package hadoopCommonStudy.unit4_rpc;

import common.CommonDateUtil;

import java.io.File;
import java.util.Date;

public class PDQueryStatusImpl implements PDQueryStatus{

    @Override
    public RMIFileStatus getFileStatus(String fileName) {
        System.out.println("fileName="+fileName);
        File file = new File(fileName);
        long lastModified = file.lastModified();
        Date date = new Date();
        date.setTime(lastModified);
        String fileCreateTime = CommonDateUtil.parseDateToStringWithPrecisionToMesc(date);
        long fileLength = file.length();
        RMIFileStatus status = new RMIFileStatus();
        status.setFileLength(fileLength);
        status.setFileCreateTime(fileCreateTime);
        return status;
    }
}
