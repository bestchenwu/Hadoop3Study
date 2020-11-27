package nettyStudy.unit5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import nettyStudy.common.CommonByteBufUtil;

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

    /**
     * 测试直接缓冲区
     *
     * @author chenwu on 2020.11.27
     */
    public void testDirectBuffer(){
        ByteBuf directBuf = Unpooled.directBuffer(100);
        directBuf.writeBytes("hi direct byte buf".getBytes());
        if(!directBuf.hasArray()){
            int length = directBuf.readableBytes();
            byte[] copiedArray = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(),copiedArray);
            handArray(copiedArray,0,length);
        }
    }

    /**
     * 一个或者多个bytebuf的组合视图
     *
     * @author chenwu on 2020.11.27
     */
    public void testCompositeBuf(){
        CompositeByteBuf messageBuf  = Unpooled.compositeBuffer();
        ByteBuf head = Unpooled.buffer();
        head.writeBytes("I am head".getBytes());
        ByteBuf body = Unpooled.directBuffer(100);
        body.writeBytes("I am body".getBytes());
        messageBuf.addComponents(head,body);
        for(ByteBuf byteBuf : messageBuf){
            System.out.println("message:"+ CommonByteBufUtil.decodeByteBuf(byteBuf));
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
        byteBufTest.testCompositeBuf();
    }
}
