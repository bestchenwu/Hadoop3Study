package nettyStudy.unit7;

import io.netty.channel.*;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ScheduledFuture;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * 测试{@link io.netty.channel.EventLoop}
 *
 * @author chenwu on 2020.12.16
 */
public class EventLoopTest {

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        LocalServerChannel channel = new LocalServerChannel();
        EventLoopGroup eventLoopGroup = new DefaultEventLoopGroup(2);
        Executor executor = Executors.newFixedThreadPool(3);
        channel.unsafe().register(new DefaultEventLoop(eventLoopGroup),new DefaultChannelPromise(channel));
        ScheduledFuture<?> scheduledFuture = channel.eventLoop().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+" print every 2 seconds");
            }
        }, 0l, 2l, TimeUnit.SECONDS);
        Thread.sleep(10*1000);
        scheduledFuture.cancel(true);
        if(scheduledFuture.isCancelled()){
            System.out.println("scheduledFuture is canceled");
        }
    }
}
