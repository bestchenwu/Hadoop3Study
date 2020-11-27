package nettyStudy.unit5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import nettyStudy.common.CommonByteBufUtil;

/**
 * {@link io.netty.buffer.ByteBuf}的操作类
 *
 * @author chenwu on 2020.11.27
 */
public class ByteBufOperTest {

    /**
     * 测试随机访问{@link ByteBuf}
     *
     * @param byteBuf
     * @author chenwu on 2020.11.27
     */
    public void randomAccessByteBuf(ByteBuf byteBuf){
        for(int i = 0;i<byteBuf.capacity();i++){
            byte b = byteBuf.getByte(i);
            System.out.print((char)b);
        }
    }

    /**
     * 测试byteBuf的读写(读一半的内容)
     *
     * @param byteBuf
     * @author chenwu on 2020.11.27
     */
    public void readWriteTest(ByteBuf byteBuf){
        int readLength = byteBuf.readableBytes();
        int actualReadLength = readLength/2;
        ByteBuf readBuf = byteBuf.readBytes(actualReadLength);
        System.out.println("read content:"+ CommonByteBufUtil.decodeByteBuf(readBuf));
        System.out.println("readIndex:"+byteBuf.readerIndex());
        System.out.println("can read length:"+byteBuf.readableBytes());
        System.out.println("writeIndex:"+byteBuf.writerIndex());
        System.out.println("can write:"+byteBuf.capacity());
    }

    public static void main(String[] args) {
        ByteBufOperTest byteBufOperTest = new ByteBufOperTest();
        ByteBuf byteBuf = Unpooled.directBuffer(100);
        String content = "I am direct buffer";
        System.out.println("content length:"+content.length());
        byteBuf.writeBytes(content.getBytes());
        byteBufOperTest.readWriteTest(byteBuf);
        //byteBufOperTest.randomAccessByteBuf(byteBuf);
    }

}
