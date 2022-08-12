package remoting.handler;

import enumeration.RpcResponseCode;
import exception.RpcException;
import factory.SingletonFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import provider.impl.ZkServiceProviderImpl;
import remoting.dto.RpcRequest;
import remoting.dto.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 客户端信息处理线程
 * @author yu
 * todo 继承Runnable是干啥的
 * 
 * todo 总结序列化
 */
@Slf4j
public class RpcRequestHandler  {
    //private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);
    private final ServiceProvider serviceProvider;
    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    /**
     * 处理rpcRequest：调用对应的方法，然后返回方法
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }
//    public Object handle(RpcRequest rpcRequest, Object service){
//         Object result = null;
//         try {
//             result = invokeTargetMethod(rpcRequest,service);
//             
//             //rpcRequest中的interfaceName是要调用的接口名字
//             logger.info("service:{} successful invoke method:{}",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
//             
//         } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//             logger.error("occur exception", e);
//         }
//         return result;
//     }

    /**
     * 获取方法执行结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
//    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
////        Class<?>cls = Class.forName(rpcRequest.getInterfaceName());
////
////        if(!cls.isAssignableFrom(service.getClass())){
////            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_CLASS);
////        }
//        //根据方法名字和参数类型找到service中的方法
//        Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
//
//        if (method==null){
//            return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
//        }
//
//        //然后根据方法的参数调用service
//        return method.invoke(service,rpcRequest.getParameters());
//
//    }
}
