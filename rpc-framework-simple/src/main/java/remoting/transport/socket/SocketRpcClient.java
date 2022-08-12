package remoting.transport.socket;

import enumeration.RpcErrorMessageEnum;
import enumeration.RpcResponseCode;
import exception.RpcException;
import extension.ExtensionLoader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceDiscovery;
import remoting.dto.RpcRequest;
import remoting.dto.RpcResponse;
import remoting.transport.RpcRequestTransport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 基于 Socket 传输 RpcRequest
 */
@AllArgsConstructor
@Slf4j
public class SocketRpcClient implements RpcRequestTransport {
    //public static final Logger logger = LoggerFactory.getLogger(SocketRpcClient.class);
//    private String host;
//    private int port;
//    /**
//     * 客户端发送请求给服务器
//     * @param rpcRequest 发送的请求
//     * @return 返回的是对象，因为发送的是对象
//     */
//    public Object sendRpcRequest(RpcRequest rpcRequest){
//        try (Socket socket = new Socket(host, port)) {
//            //为什么是socket
//            ObjectOutputStream objectOutputStream =new ObjectOutputStream(socket.getOutputStream());
//            // 将对象以二进制格式写入之后怎么传数据呢？
//            //使用socket传数据的
//            objectOutputStream.writeObject(rpcRequest);
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            
//            //服务器发过来的响应对象
//            RpcResponse rpcResponse  = (RpcResponse)  objectInputStream.readObject();
//            if(rpcResponse==null){
//                logger.error("调用服务失败，serviceName:{}",rpcRequest.getInterfaceName());
//                throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,"interfaceName:" + rpcRequest.getInterfaceName());
//            }
//            if(rpcResponse.getCode()==null||!rpcResponse.getCode().equals(RpcResponseCode.SUCCESS.getCode())){
//                logger.error("调用服务失败，serviceName:{},RpcResponse:{}",rpcRequest.getInterfaceName(),rpcResponse);
//                throw  new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,"interfaceName:"+rpcRequest.getInterfaceName());
//            }
//            
//            return rpcResponse.getData();
//            
//            
//        } catch (IOException | ClassNotFoundException e) {
//            throw new RpcException("调用服务失败:", e);
//        }
//        /**
//         * 如果没有建立socket成功就返回null，服务器接收null就不会显示客户端连接
//         */
//    }




    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient() {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Send data to the server through the output stream
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Read RpcResponse from the input stream
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("调用服务失败:", e);
        }
    }
    
}
