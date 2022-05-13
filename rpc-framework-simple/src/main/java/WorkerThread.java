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
 * todo 继承Runnable是干啥的
 * 
 * todo 总结序列化
 */
@AllArgsConstructor
public class WorkerThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);
    private Socket socket;
    /**
     * todo service是干嘛的呢
     */
    private Object service;
    
    
    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            //获取客户端的请求
            //rpcRequest就是客户端需要调用的方法
            RPCRequest rpcRequest = (RPCRequest) objectInputStream.readObject();
            //反射
            //获取客户端要访问的方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
            
            //result 是服务器返回给客户端的结果
            Object result = method.invoke(service,rpcRequest.getParameters());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        }catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
