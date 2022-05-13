import dto.RPCRequest;
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
 * todo �̳�Runnable�Ǹ�ɶ��
 * 
 * todo �ܽ����л�
 */
@AllArgsConstructor
public class WorkerThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);
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
            RPCRequest rpcRequest = (RPCRequest) objectInputStream.readObject();
            //����
            //��ȡ�ͻ���Ҫ���ʵķ���
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
            
            //result �Ƿ��������ظ��ͻ��˵Ľ��
            Object result = method.invoke(service,rpcRequest.getParameters());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        }catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
