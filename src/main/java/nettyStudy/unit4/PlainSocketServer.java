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
        ServerSocket serverSocket = new ServerSocket(port);
        while(true){
            final Socket clientSocket = serverSocket.accept();
            new Thread(){

                @Override
                public void run() {
                    try{
                        //获取客户端发送的消息
                        InputStream inputStream = clientSocket.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String clientStr = bufferedReader.readLine();
                        System.out.println("get client:"+clientStr);
                        //给客户端写消息
                        OutputStream outputStream = clientSocket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(outputStream);
                        PrintWriter pw = new PrintWriter(osw);
                        pw.println("hi raoshanshan!,you are :"+clientStr);
                        pw.flush();
                        bufferedReader.close();
                        pw.close();
                        clientSocket.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                }
            }.start();

        }
    }
}
