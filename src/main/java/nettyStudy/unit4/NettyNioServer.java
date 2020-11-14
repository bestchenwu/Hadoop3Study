package nettyStudy.unit4;

import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 使用netty来实现NioServer
 * 
 * @author sweet
 */
public class NettyNioServer {

	public static void main(String[] args) {
		final ByteBuf buf = Unpooled.copiedBuffer("hi \r\n",Charset.forName("UTF-8"));
		EventLoopGroup serverLoopGroup = new NioEventLoopGroup();
		ServerBootstrap serverBootStrap = new ServerBootstrap();
		serverBootStrap.group(serverLoopGroup).channel(NioServerSocketChannel.class)
			.localAddress(8599);
	}
}
