package hadoopCommonStudy.unit4_rpc;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIQueryServer {

    public static void main(String[] args) {
        try{
            RMIQueryServiceImpl impl = new RMIQueryServiceImpl();
            LocateRegistry.createRegistry(12090);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("queryFileStatus",impl);
//            Naming.bind(String.format("rmi://%s:12090/queryFileStatus",host),impl);
            System.out.println("server ready");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
