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

    // TODO: 2022/8/12 ��һ����ɶ��ͬ 
//    @Override
//    public void run() {
//        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
//            //��ȡ�ͻ��˵�����
//            //rpcRequest���ǿͻ�����Ҫ���õķ���
//            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
//            //����
//            //��ȡ�ͻ���Ҫ���ʵķ���
////            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
////            
////            //result �Ƿ��������ظ��ͻ��˵Ľ��
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
