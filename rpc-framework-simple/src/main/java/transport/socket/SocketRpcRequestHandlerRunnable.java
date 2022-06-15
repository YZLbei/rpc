package transport.socket;

import dto.RpcRequest;
import dto.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceRegistry;
import transport.RpcRequestHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketRpcRequestHandlerRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SocketRpcRequestHandlerRunnable.class);
    private Socket socket;
    private RpcRequestHandler rpcRequestHandler;
    private ServiceRegistry serviceRegistry;


    public SocketRpcRequestHandlerRunnable(Socket socket, RpcRequestHandler rpcRequestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.rpcRequestHandler = rpcRequestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            //��ȡ�ͻ��˵�����
            //rpcRequest���ǿͻ�����Ҫ���õķ���
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            //����
            //��ȡ�ͻ���Ҫ���ʵķ���
//            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
//            
//            //result �Ƿ��������ظ��ͻ��˵Ľ��
//            Object result = method.invoke(service,rpcRequest.getParameters());
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            Object result = rpcRequestHandler.handle(rpcRequest,service);
            
            
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        }catch (IOException | ClassNotFoundException e) {
            logger.error("occur exception:", e);
        }
    }
}
