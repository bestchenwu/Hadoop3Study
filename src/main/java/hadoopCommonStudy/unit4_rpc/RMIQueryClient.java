package hadoopCommonStudy.unit4_rpc;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI查询客户端
 */
public class RMIQueryClient {

    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.getRegistry("192.168.111.66");
            RMIQueryService service = (RMIQueryService)registry.lookup("queryFileStatus");
            //RMIQueryService service = (RMIQueryService)Naming.lookup("rmi://192.168.111.66:12090/queryFileStatus");
            //RMIFileStatus fileStatus = service.getFileStatus("D:\\newJob.txt");
            RMIFileStatus fileStatus = service.getFileStatus("/home/openmldb/bashrc.txt");
            System.out.println("status:"+fileStatus);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
