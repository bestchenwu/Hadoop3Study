package nettyStudy.unit9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.junit.Test;

import java.util.List;

public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private int fixedLength;

    public FixedLengthFrameDecoder(int fixedLength){
        this.fixedLength = fixedLength;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        while(byteBuf.readableBytes()>=fixedLength){
            ByteBuf buf = byteBuf.readBytes(fixedLength);
            list.add(buf);
        }
    }
}
