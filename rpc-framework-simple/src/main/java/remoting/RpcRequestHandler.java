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
 * 客户端信息处理线程
 * todo 继承Runnable是干啥的
 * 
 * todo 总结序列化
 */
@AllArgsConstructor
public class RpcRequestHandler  {
    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);
    
     public Object handle(RpcRequest rpcRequest,Object service){
         Object result = null;
         try {
             result = invokeTargetMethod(rpcRequest,service);
             
             //rpcRequest中的interfaceName是要调用的接口名字
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
        //根据方法名字和参数类型找到service中的方法
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());

        if (method==null){
            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
        }

        //然后根据方法的参数调用service
        return method.invoke(service,rpcRequest.getParameters());

    }
}
