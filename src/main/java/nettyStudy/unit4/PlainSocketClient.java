package nettyStudy.unit4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class PlainSocketClient {

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        for (int i = 0; i < 5; i++) {
            Socket socket = new Socket("127.0.0.1", port);
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream.write(("hello " + i).getBytes());
            //从服务端获取到的结果
            System.out.println("server:" + reader.readLine());
            outputStream.close();
            socket.close();
        }
    }

}
