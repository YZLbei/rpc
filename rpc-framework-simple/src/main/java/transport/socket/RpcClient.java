package transport.socket;

import dto.RpcRequest;
import dto.RpcResponse;
import enumeration.RpcErrorMessageEnum;
import enumeration.RpcResponseCode;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcClient {
    public static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    /**
     * 客户端发送请求给服务器
     * @param rpcRequest 发送的请求
     * @param host 主机
     * @param port 端口号
     * @return 返回的是对象，因为发送的是对象
     */
    public Object sendRpcRequest(RpcRequest rpcRequest, String host, int port){
        try (Socket socket = new Socket(host, port)) {
            //为什么是socket
            ObjectOutputStream objectOutputStream =new ObjectOutputStream(socket.getOutputStream());
            // 将对象以二进制格式写入之后怎么传数据呢？
            //使用socket传数据的
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            
            //服务器发过来的响应对象
            RpcResponse rpcResponse  = (RpcResponse)  objectInputStream.readObject();
            if(rpcResponse==null){
                logger.error("调用服务失败，serviceName:{}",rpcRequest.getInterfaceName());
                throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,"interfaceName:" + rpcRequest.getInterfaceName());
            }
            if(rpcResponse.getCode()==null||!rpcResponse.getCode().equals(RpcResponseCode.SUCCESS.getCode())){
                logger.error("调用服务失败，serviceName:{},RpcResponse:{}",rpcRequest.getInterfaceName(),rpcResponse);
                throw  new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,"interfaceName:"+rpcRequest.getInterfaceName());
            }
            
            return rpcResponse.getData();
            
            
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("调用服务失败:", e);
        }
        /**
         * 如果没有建立socket成功就返回null，服务器接收null就不会显示客户端连接
         */
    }
    
}
