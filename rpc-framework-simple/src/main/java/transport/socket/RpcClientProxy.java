package transport.socket;

import dto.RpcRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 JDK动态代理
 */
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {
    private final static Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    private String host;
    private int port;


    /**
     *  @param proxy 代理的对象，就是rpcClient?  不是的，代理的对象是接口是HelloService
     * @param method 客户端要调用的方法
     * @param args 客户端调用方法的参数
     * @return 返回的是代理类发出的请求
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("Call invoke method and invoked method:{}",method.getName());
        RpcRequest rpcRequest = RpcRequest.builder()
                .methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .build();
        //return rpcRequest;
        //获取代理类的，也就是客户端的四个参数
        RpcClient rpcClient = new RpcClient();
        //通过代理类发送请求
        return rpcClient.sendRpcRequest(rpcRequest,host,port);
    }

    /**
     * getProxy
     * 生成代理对象
     * 
     * getProxy生成的不是RpcClient对象,而应该是代理RpcClient的RpcClientProxy对象
     */
    public<T>T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz}, this);
    }
    
    
}
