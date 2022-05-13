package hadoopCommonStudy.unit4_rpc;

import common.CommonDateUtil;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 必须继承UnicastRemoteObject，以允许JVM创建远程的存根/代理。
 *
 * @author chenwu on 2022.5.13
 */
public class RMIQueryServiceImpl extends UnicastRemoteObject implements RMIQueryService {

    public RMIQueryServiceImpl() throws RemoteException{
        super();
    }

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

    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }

    @Override
    public int multi(int a, int b) throws RemoteException {
        return a*b;
    }
}
