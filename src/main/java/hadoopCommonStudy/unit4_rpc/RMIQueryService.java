package hadoopCommonStudy.unit4_rpc;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 远程查询文件状态
 *
 * @author chenwu on 2022.5.12
 */
public interface RMIQueryService extends Remote, Serializable {

    public RMIFileStatus getFileStatus(String fileName) throws RemoteException;

    public int add(int a,int b) throws RemoteException;

    public int multi(int a,int b) throws RemoteException;

}
