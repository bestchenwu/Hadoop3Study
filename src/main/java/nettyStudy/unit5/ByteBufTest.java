package nettyStudy.unit5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledHeapByteBuf;

import java.util.Arrays;

/**
 * {@link ByteBuf}的测试<br/>
 * 参考文档:https://blog.csdn.net/thinking_fioa/article/details/80795673<br/>
 * 以及https://www.cnblogs.com/duanxz/p/3724448.html
 *
 * @author chenwu on 2020.11.26
 */
public class ByteBufTest {

    /**
     * 测试支撑数组模式
     *
     * @author chenwu on 2020.11.26
     */
    public void testHeapByteBuf(){
        //创建java堆缓冲区模式
        ByteBuf byteBuf = Unpooled.copiedBuffer("hi heap byte buf".getBytes());
        //判断是否是java堆缓冲区模式 数组直接创建在堆内存上
        if(byteBuf.hasArray()){
            byte[] array = byteBuf.array();
            //计算第一个可读字节的便宜量
            int offset = byteBuf.arrayOffset()+byteBuf.readerIndex();
            //计算总共可以读的字节数
            int length = byteBuf.readableBytes();
            handArray(array,offset,length);
        }
    }

    private void handArray(byte[] array,int offset,int length){
        //拷贝的范围是[from,to)
        byte[] copyArray = Arrays.copyOfRange(array, offset, offset + length);
        String copyString = new String(copyArray);
        System.out.println("copyString:"+copyString);
    }

    public static void main(String[] args) {
        ByteBufTest byteBufTest = new ByteBufTest();
        byteBufTest.testHeapByteBuf();
    }
}
