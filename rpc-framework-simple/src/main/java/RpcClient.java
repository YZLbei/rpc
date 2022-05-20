import dto.RPCRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class RpcClient {
    public static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    /**
     * 客户端发送请求给服务器
     * @param rpcRequest 发送的请求
     * @param host 主机
     * @param port 端口号
     * @return 返回的是对象，因为发送的是对象
     */
    public Object sendRpcRequest(RPCRequest rpcRequest,String host,int port){
        try (Socket socket = new Socket(host, port)) {
            //todo 为什么是socket
            ObjectOutputStream objectOutputStream =new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        /**
         * 如果没有建立socket成功就返回null，服务器接收null就不会显示客户端连接
         */
        return null;
    }
    
}
