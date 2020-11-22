package nettyStudy.unit4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyNioClient {

    public static void main(String[] args) {
        Bootstrap clientBootStrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        clientBootStrap.group(eventLoopGroup).channel(NioSocketChannel.class);
        clientBootStrap.remoteAddress("127.0.0.1", 8599);
        clientBootStrap.handler(
                new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                System.out.println("client get message:" + msg.toString(CharsetUtil.UTF_8));
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ctx.writeAndFlush(Unpooled.copiedBuffer(("hello,I am client").getBytes()));
                            }
                        });
                    }
                }

        );
        try {
            ChannelFuture channelFuture = clientBootStrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                eventLoopGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }
}
