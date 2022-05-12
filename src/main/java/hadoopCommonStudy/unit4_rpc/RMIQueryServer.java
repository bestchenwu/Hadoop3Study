package hadoopCommonStudy.unit4_rpc;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIQueryServer {

    public static void main(String[] args) {
        try{
            RMIQueryServiceImpl impl = new RMIQueryServiceImpl();
            LocateRegistry.createRegistry(12090);
            Naming.bind("rmi://192.168.111.116:12090/queryFileStatus",impl);
            System.out.println("server ready");
            while(true){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
