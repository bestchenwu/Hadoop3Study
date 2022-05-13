package hadoopCommonStudy.unit4_rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;

public class DPInvocationHandler implements InvocationHandler {

    private PDQueryStatusImpl dpqs;

    public DPInvocationHandler(PDQueryStatusImpl dpqs){
        this.dpqs = dpqs;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = null;
        //实现附加功能,在调用之前输出调用参数
        String msg = MessageFormat.format("calling method {0}{1}", method.getName(), Arrays.toString(args));
        System.out.println(msg);
        //调用转发
        ret = method.invoke(dpqs,args);
        return ret;
    }
}
