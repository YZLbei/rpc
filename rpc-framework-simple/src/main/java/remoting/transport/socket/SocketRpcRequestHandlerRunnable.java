package remoting.transport.socket;

import factory.SingletonFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceRegistry;
import remoting.dto.RpcRequest;
import remoting.dto.RpcResponse;
import remoting.handler.RpcRequestHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable {
    //private static final Logger logger = LoggerFactory.getLogger(SocketRpcRequestHandlerRunnable.class);
    private Socket socket;
    private RpcRequestHandler rpcRequestHandler;
    //private ServiceRegistry serviceRegistry;


    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }


    @Override
    public void run() {
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = rpcRequestHandler.handle(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("occur exception:", e);
        }
    }

    // TODO: 2022/8/12 看一下有啥不同 
//    @Override
//    public void run() {
//        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
//            //获取客户端的请求
//            //rpcRequest就是客户端需要调用的方法
//            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
//            //反射
//            //获取客户端要访问的方法
////            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
////            
////            //result 是服务器返回给客户端的结果
////            Object result = method.invoke(service,rpcRequest.getParameters());
//            String interfaceName = rpcRequest.getInterfaceName();
//            Object service = serviceRegistry.getService(interfaceName);
//            Object result = rpcRequestHandler.handle(rpcRequest,service);
//            
//            
//            objectOutputStream.writeObject(RpcResponse.success(result));
//            objectOutputStream.flush();
//        }catch (IOException | ClassNotFoundException e) {
//            logger.error("occur exception:", e);
//        }
//    }
}
