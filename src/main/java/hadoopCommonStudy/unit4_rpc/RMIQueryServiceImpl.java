package hadoopCommonStudy.unit4_rpc;

import common.CommonDateUtil;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Date;

public class RMIQueryServiceImpl implements RMIQueryService {

    @Override
    public RMIFileStatus getFileStatus(String fileName) throws RemoteException {
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
