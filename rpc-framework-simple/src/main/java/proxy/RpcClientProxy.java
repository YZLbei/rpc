package proxy;

import config.RpcServiceConfig;
import dto.RpcRequest;
import dto.RpcResponse;
import enums.RpcErrorMessageEnum;
import enums.RpcResponseCodeEnum;
import exception.RpcException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import transport.RpcClient;
import transport.netty.NettyRpcClient;
import transport.socket.SocketRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * jdk��̬����
 * ��̬������
 * ��һ����̬����������һ������ʱ����ʵ���ϵ����������invoke����
 * ������Ϊ��̬�����ͻ��˵���Զ�̷���������ñ��ط���һ�����м���̱����Σ�
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    //private final static Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    // TODO: 2022/8/12 �����õ� 
    private static final String INTERFACE_NAME = "interfaceName";
    /**
     * ������������������󡣲���������ʵ�ַ�ʽ��socket��netty
     */
    private final RpcRequestTransport rpcRequestTransport;
    private final RpcServiceConfig rpcServiceConfig;
//    private RpcClient rpcClient;
//    public RpcClientProxy(RpcClient rpcClient) {
//        this.rpcClient = rpcClient;
//    }
    public RpcClientProxy(RpcRequestTransport rpcRequestTransport, RpcServiceConfig rpcServiceConfig) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = rpcServiceConfig;
    }
    public RpcClientProxy(RpcRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = new RpcServiceConfig();
    }
    /**
     *  @param proxy ����Ķ��󣬾���rpcClient?  ���ǵģ�����Ķ����ǽӿ���HelloService
     * @param method �ͻ���Ҫ���õķ���
     * @param args �ͻ��˵��÷����Ĳ���
     * @return ���ص��Ǵ����෢��������
     */
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        logger.info("Call invoke method and invoked method:{}",method.getName());
//        RpcRequest rpcRequest = RpcRequest.builder()
//                .methodName(method.getName())
//                .parameters(args)
//                .interfaceName(method.getDeclaringClass().getName())
//                .paramTypes(method.getParameterTypes())
//                .build();
//        //return rpcRequest;
//        //��ȡ������ģ�Ҳ���ǿͻ��˵��ĸ�����
//        //SocketRpcClient rpcClient = new SocketRpcClient();
//        //ͨ�������෢������
//        return rpcClient.sendRpcRequest(rpcRequest);
//    }
    /**
     * ����ʹ�ô��������÷���ʱ��ʵ���ϻ���ô˷�����
     * ������������ͨ��getProxy�����õ��Ķ���
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("invoked method: [{}]", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();
        RpcResponse<Object> rpcResponse = null;
        if (rpcRequestTransport instanceof NettyRpcClient) {
            CompletableFuture<RpcResponse<Object>> completableFuture = (CompletableFuture<RpcResponse<Object>>) rpcRequestTransport.sendRpcRequest(rpcRequest);
            rpcResponse = completableFuture.get();
        }
        if (rpcRequestTransport instanceof SocketRpcClient) {
            rpcResponse = (RpcResponse<Object>) rpcRequestTransport.sendRpcRequest(rpcRequest);
        }
        this.check(rpcResponse, rpcRequest);
        return rpcResponse.getData();
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

    private void check(RpcResponse<Object> rpcResponse, RpcRequest rpcRequest) {
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
