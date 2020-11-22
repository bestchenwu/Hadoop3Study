package nettyStudy.unit4;

import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 使用netty来实现NioServer
 * 
 * @author sweet
 */
public class NettyNioServer {

	public static void main(String[] args) {
		final ByteBuf buf = Unpooled.copiedBuffer("hi client \r\n",Charset.forName("UTF-8"));
		//非阻塞模式
		EventLoopGroup serverLoopGroup = new NioEventLoopGroup();
		ServerBootstrap serverBootStrap = new ServerBootstrap();
		serverBootStrap.group(serverLoopGroup).channel(NioServerSocketChannel.class)
				.localAddress(8599);
		serverBootStrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){

					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
						ByteBuf in = (ByteBuf)msg;
						System.out.println("Server received:"+in.toString(CharsetUtil.UTF_8));
						//将接受到的消息写给发送者
						ctx.write(in);
					}

					@Override
					public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
						//将消息冲刷到远程节点,并且关闭channel
						ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
					}

					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
					}
				});
			}
		});
		try{
			ChannelFuture future = serverBootStrap.bind().sync();
			future.channel().closeFuture().sync();
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally {
			try{
				serverLoopGroup.shutdownGracefully().sync();
			}catch (InterruptedException e){
				e.printStackTrace();
			}

		}



	}
}
