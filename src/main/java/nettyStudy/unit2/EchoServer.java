package nettyStudy.unit2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private int port;

    public EchoServer(int port){
        this.port = port;
    }

    public void start() throws Exception{
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
            .childHandler(new ChannelInitializer<SocketChannel>(){
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(serverHandler);
                }
            });
            //异步的绑定服务器,调用sync()方法阻塞等待直到绑定完成
            ChannelFuture channelFuture = b.bind().sync();
            //获取Channel的closeFuture 并阻塞当前线程直到它完成
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }
}
