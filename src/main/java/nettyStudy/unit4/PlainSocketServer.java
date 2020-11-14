package nettyStudy.unit4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 普通使用socket来实现server的模式
 *		
 * @author chenwu on 2020.11.12
 */
public class PlainSocketServer {

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        final ServerSocket serverSocket = new ServerSocket(port);
        while(true){
            final Socket clientSocket = serverSocket.accept();
            try{
                //获取客户端发送的消息
                InputStream inputStream = clientSocket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //因为服务端用了readLine方法 所以要一直等待客户端输入完整的一行,所以客户端需要加\n
                String clientStr = bufferedReader.readLine();
                System.out.println("get client:"+clientStr);
                //给客户端写消息
                OutputStream outputStream = clientSocket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(outputStream);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write("hi raoshanshan!,you are :"+clientStr);
                bw.write("\n");
                bw.flush();
                bufferedReader.close();
                bw.close();
                clientSocket.close();
            }catch(IOException e){
                e.printStackTrace();
                try {
                	 serverSocket.close();
                }catch(IOException e1) {
                	e1.printStackTrace();
                }
            }

        }
    }
}
