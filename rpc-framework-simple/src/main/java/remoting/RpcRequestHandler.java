package remoting;

import dto.RpcRequest;
import dto.RpcResponse;
import enumeration.RpcResponseCode;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * �ͻ�����Ϣ�����߳�
 * todo �̳�Runnable�Ǹ�ɶ��
 * 
 * todo �ܽ����л�
 */
@AllArgsConstructor
public class RpcRequestHandler  {
    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);
    
     public Object handle(RpcRequest rpcRequest,Object service){
         Object result = null;
         try {
             result = invokeTargetMethod(rpcRequest,service);
             
             //rpcRequest�е�interfaceName��Ҫ���õĽӿ�����
             logger.info("service:{} successful invoke method:{}",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
             
         } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
             logger.error("occur exception", e);
         }
         return result;
     }
    
    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        Class<?>cls = Class.forName(rpcRequest.getInterfaceName());
//
//        if(!cls.isAssignableFrom(service.getClass())){
//            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_CLASS);
//        }
        //���ݷ������ֺͲ��������ҵ�service�еķ���
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());

        if (method==null){
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
        }

        //Ȼ����ݷ����Ĳ�������service
        return method.invoke(service,rpcRequest.getParameters());

    }
}
