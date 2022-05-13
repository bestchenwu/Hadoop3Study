package hadoopCommonStudy.unit4_rpc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 测试java的NIO
 *
 * @author chenwu on 2022.5.13
 */
public class SimpleNIOServer {

    public static void main(String[] args) throws IOException,InterruptedException {
        //创建通道
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(8088));
        //使用Selector 必须设置为非阻塞
        channel.configureBlocking(false);
        //创建选择器
        Selector selector = Selector.open();
        //注册到选择器
        channel.register(selector, SelectionKey.OP_ACCEPT);
        Thread thread = new Thread(){

            @Override
            public void run() {
                try{
                    while(true){
                        //获取已就绪事件数
                        int count = selector.select();
                        if(count==0){
                            continue;
                        }
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while(iterator.hasNext()){
                            SelectionKey key = iterator.next();
                            if(!key.isValid()){
                                continue;
                            }else if(key.isAcceptable()){
                                //处理接受事件
                                SocketChannel socketChannel = channel.accept();
                                socketChannel.configureBlocking(false);
                                socketChannel.register(selector,SelectionKey.OP_READ);
                                System.out.println("已注册:"+socketChannel);
                                //iterator.remove();
                            }else if(key.isReadable()){
                                //处理读取事件
                                SocketChannel channel = (SocketChannel)key.channel();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
                                channel.read(byteBuffer);
                                //将缓冲区转为可输出状态
                                byteBuffer.flip();
                                byte[] bytes = new byte[byteBuffer.remaining()];
                                byteBuffer.get(bytes);
                                String str = new String(bytes);
                                str="\r\nreceive:"+str;
                                System.out.println(str);
                                //将结果回写到通道中
                                channel.write(ByteBuffer.wrap(str.getBytes()));
                            }
                            iterator.remove();
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        thread.join();
    }
}
