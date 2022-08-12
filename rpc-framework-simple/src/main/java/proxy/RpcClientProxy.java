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
 * jdk动态代理
 * 动态代理类
 * 当一个动态代理对象调用一个方法时，它实际上调用了下面的invoke方法
 * 正是因为动态代理，客户端调用远程方法就像调用本地方法一样（中间过程被屏蔽）
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    //private final static Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    // TODO: 2022/8/12 干嘛用的 
    private static final String INTERFACE_NAME = "interfaceName";
    /**
     * 用于向服务器发送请求。并且有两种实现方式：socket和netty
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
     *  @param proxy 代理的对象，就是rpcClient?  不是的，代理的对象是接口是HelloService
     * @param method 客户端要调用的方法
     * @param args 客户端调用方法的参数
     * @return 返回的是代理类发出的请求
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
//        //获取代理类的，也就是客户端的四个参数
//        //SocketRpcClient rpcClient = new SocketRpcClient();
//        //通过代理类发送请求
//        return rpcClient.sendRpcRequest(rpcRequest);
//    }
    /**
     * 当您使用代理对象调用方法时，实际上会调用此方法。
     * 代理对象就是你通过getProxy方法得到的对象。
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
     * 生成代理对象
     * 
     * getProxy生成的不是RpcClient对象,而应该是代理RpcClient的RpcClientProxy对象
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
