package hadoopCommonStudy.unit4_rpc;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIQueryServer {

    public static void main(String[] args) {
        String host = args[0];
        try{
            RMIQueryServiceImpl impl = new RMIQueryServiceImpl();
            LocateRegistry.createRegistry(12090);
            Naming.bind(String.format("rmi://%s:12090/queryFileStatus",host),impl);
            System.out.println("server ready");
            while(true){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
