package nettyStudy.common;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * {@link ByteBuf}的常用工具类
 *
 * @author chenwu on 2020.11.27
 */
public class CommonByteBufUtil {

    /**
     * 解析ByteBuf获得其字符串的表示形式
     *
     * @param byteBuf
     * @return {@link String}
     * @author chenwu on 2020.11.27
     */
    public static String decodeByteBuf(ByteBuf byteBuf) {
        if (byteBuf == null) {
            return "";
        }
        if (byteBuf.hasArray()) {
            //堆缓冲区模式
            int offset = byteBuf.arrayOffset() + byteBuf.readerIndex();
            int length = byteBuf.readableBytes();
            String result = handle(byteBuf.array(), offset, length);
            return result;
        } else {
            //直接缓冲区模式
            int length = byteBuf.readableBytes();
            byte[] dataArray = new byte[length];
            byteBuf.getBytes(byteBuf.readerIndex(), dataArray);
            String result = handle(dataArray, 0, length);
            return result;
        }
    }

    /**
     * 将数组转换为字符串
     *
     * @param array
     * @param offset
     * @param length
     * @return {@link String}
     * @author chenwu on 2020.11.27
     */
    private static String handle(byte[] array, int offset, int length) {
        byte[] dest = Arrays.copyOfRange(array, offset, offset + length);
        String result = new String(dest);
        return result;
    }
}
