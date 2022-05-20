import dto.RPCRequest;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 JDK��̬����
 */
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;


    /**
     * todo @param proxy ����Ķ��󣬾���rpcClient?
     * @param method �ͻ���Ҫ���õķ���
     * @param args �ͻ��˵��÷����Ĳ���
     * @return ���ص��Ǵ����෢��������
     * todo ����������ô���õ�
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCRequest rpcRequest = RPCRequest.builder()
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
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz}, RpcClientProxy.this);
    }
    
    
}
