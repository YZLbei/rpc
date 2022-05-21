import dto.RpcRequest;
import dto.RpcResponse;
import enumeration.RpcResponseCode;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
/**
 * �ͻ�����Ϣ�����߳�
 * todo �̳�Runnable�Ǹ�ɶ��
 * 
 * todo �ܽ����л�
 */
@AllArgsConstructor
public class ClientMessageHandlerThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientMessageHandlerThread.class);
    private Socket socket;
    /**
     * todo service�Ǹ������
     */
    private Object service;
    
    
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
            
            Object result = invokeTargetMethod(rpcRequest);
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        }catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    private Object invokeTargetMethod(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?>cls = Class.forName(rpcRequest.getInterfaceName());

        if(!cls.isAssignableFrom(service.getClass())){
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_CLASS);
        }

        //���ݷ������ֺͲ��������ҵ�service�еķ���
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());

        if (method==null){
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
        }

        //Ȼ����ݷ����Ĳ�������service
        return method.invoke(service,rpcRequest.getParameters());

    }
}
