package hadoopCommonStudy.unit4_rpc;

import java.lang.reflect.Proxy;

public class DPMain {

    public static PDQueryStatus create(PDQueryStatusImpl impl){
        Class<?>[] interfaces = new Class[]{PDQueryStatus.class};
        DPInvocationHandler handler = new DPInvocationHandler(impl);
        //interfaces是动态代理类需要实现的代理接口
        //handler是实现了代理对象的调用转发功能
        Object result = Proxy.newProxyInstance(impl.getClass().getClassLoader(), interfaces, handler);
        return (PDQueryStatus)result;
    }
}
