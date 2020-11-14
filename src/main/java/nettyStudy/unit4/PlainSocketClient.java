package nettyStudy.unit4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class PlainSocketClient {

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        for (int i = 0; i < 5; i++) {
            Socket socket = new Socket("127.0.0.1", port);
            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write("this is "+i);
            bw.write("\n");
            bw.flush();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));            
            //从服务端获取到的结果
            System.out.println("server:" + reader.readLine());
            socket.close();
            bw.close();
            reader.close();
        }
    }

}
