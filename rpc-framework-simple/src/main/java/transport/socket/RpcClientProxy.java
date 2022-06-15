package transport.socket;

import dto.RpcRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 JDK��̬����
 */
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {
    private final static Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    private String host;
    private int port;


    /**
     *  @param proxy ����Ķ��󣬾���rpcClient?  ���ǵģ�����Ķ����ǽӿ���HelloService
     * @param method �ͻ���Ҫ���õķ���
     * @param args �ͻ��˵��÷����Ĳ���
     * @return ���ص��Ǵ����෢��������
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
        //��ȡ������ģ�Ҳ���ǿͻ��˵��ĸ�����
        RpcClient rpcClient = new RpcClient();
        //ͨ�������෢������
        return rpcClient.sendRpcRequest(rpcRequest,host,port);
    }

    /**
     * getProxy
     * ���ɴ������
     * 
     * getProxy���ɵĲ���RpcClient����,��Ӧ���Ǵ���RpcClient��RpcClientProxy����
     */
    public<T>T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz}, this);
    }
    
    
}
