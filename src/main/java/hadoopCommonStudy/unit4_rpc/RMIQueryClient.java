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
            Registry registry = LocateRegistry.getRegistry("192.168.111.66",12090);
            RMIQueryService service = (RMIQueryService)registry.lookup("queryFileStatus");
            //RMIQueryService service = (RMIQueryService)Naming.lookup("rmi://192.168.111.66:12090/queryFileStatus");
            //RMIFileStatus fileStatus = service.getFileStatus("D:\\newJob.txt");
//            RMIFileStatus fileStatus = service.getFileStatus("/home/openmldb/bashrc.txt");
//            System.out.println("status:"+fileStatus);
            //获取加和乘的运算法则
            int a = 2,b = 3;
            int res1 = service.add(a,b);
            System.out.println("res1="+res1);
            int res2 = service.multi(a,b);
            System.out.println("res2="+res2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
