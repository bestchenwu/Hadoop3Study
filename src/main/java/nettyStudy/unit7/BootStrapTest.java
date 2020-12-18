package nettyStudy.unit7;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端引导例子
 *
 * @author chenwu on 2020.12.18
 */
public class BootStrapTest {

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap clientBootStrap = new Bootstrap();
        clientBootStrap.group(eventLoopGroup).channel(NioSocketChannel.class).remoteAddress("127.0.0.1",8599);
        clientBootStrap.handler(new SimpleChannelInboundHandler<ByteBuf>() {

            @Override
            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                System.out.println("received data:"+msg.toString());
            }
        });
        try{
            ChannelFuture channelFuture = clientBootStrap.connect().sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("server bound");
                    }else{
                        System.out.println("server can't bound");
                        future.cause().printStackTrace();
                    }
                }
            });
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }

    }
}
