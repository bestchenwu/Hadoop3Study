package kafkaStudy.unit6;

import org.apache.kafka.common.protocol.ApiKeys;
import org.apache.kafka.common.requests.AbstractRequest;
import org.apache.kafka.common.requests.RequestHeader;
import org.apache.kafka.common.requests.RequestUtils;
import org.apache.kafka.common.requests.ResponseHeader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * 发送自定义消息
 *
 * @author chenwu on 2020.10.29
 */
public class SocketService {

    public ByteBuffer send(String host,int port,AbstractRequest request,ApiKeys apiKeys) throws IOException{
        Socket socket = connect(host,port);
        ByteBuffer response = null;
        try{
            response = send(request, apiKeys, socket);
        }finally {
            socket.close();
        }
        return response;
    }

    private ByteBuffer send(AbstractRequest request, ApiKeys apiKey,Socket socket) throws IOException{
        RequestHeader header = new RequestHeader(apiKey,request.version(),"client-id",0);
        ByteBuffer buffer = request.serialize(header);
        byte[] response = sendAndWaitForResponse(socket, buffer.array());
        ByteBuffer responseBuffer = ByteBuffer.wrap(response);
        ResponseHeader.parse(responseBuffer,request.version());
        return responseBuffer;
    }

    private byte[] sendAndWaitForResponse(Socket socket,byte[] request) throws IOException{
        sendRequest(socket,request);
        return getResponse(socket);
    }

    private void sendRequest(Socket socket,byte[] request) throws IOException{
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(request.length);
        outputStream.write(request);
        outputStream.flush();
    }

    private byte[] getResponse(Socket socket) throws IOException {
        DataInputStream dis = null;
        byte[] result = null;
        try {
            dis = new DataInputStream(socket.getInputStream());
            result = new byte[dis.readInt()];
            dis.readFully(result);
        } finally {
            if (dis != null) {
                dis.close();
            }
        }
        return result;
    }

    private Socket connect(String host, int port) throws IOException {
        return new Socket(host, port);
    }
}
