package remoting.transport.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import config.CustomShutdownHook;
import config.RpcServiceConfig;
import factory.SingletonFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.ServiceProvider;
import provider.impl.ZkServiceProviderImpl;
import registry.ServiceRegistry;
import remoting.handler.RpcRequestHandler;
import utils.concurrent.threadpool.ThreadPoolFactoryUtil;

@Slf4j
public class SocketRpcServer {
//    private static final int CORE_POOL_SIZE = 10;
//    private static final int MAXIMUM_POOL_SIZE_SIZE = 100;
//    private static final int KEEP_ALIVE_TIME = 1;
//    private static final int BLOCKING_QUEUE_CAPACITY = 100;
//    private RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
//    private final ServiceRegistry serviceRegistry;
//    private ExecutorService threadPool;
//    private static final Logger logger = LoggerFactory.getLogger(SocketRpcServer.class);
    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;
    public SocketRpcServer() {
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            server.bind(new InetSocketAddress(host, PORT));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }

//    /**
//     * 线程池
//     * todo 重新看一下
//     */
//    public SocketRpcServer(ServiceRegistry serviceRegistry) {
//        //线程池的参数
//        this.serviceRegistry = serviceRegistry;
//        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
//        ThreadFactory threadFactory = Executors.defaultThreadFactory();
//        this.threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue, threadFactory);
//    }
//
//    /**
//     * 服务端主动注册服务
//     *TODO
//     *     2. 修改为扫描注解注册
//     */
//    public void start(int port){
//        //1.创建 ServerSocket 对象并且绑定一个端口
//        try(ServerSocket server = new ServerSocket(port);) {
//            logger.info("server start...");
//            Socket socket;
//            //2.通过 accept()方法监听客户端请求
//            while ((socket = server.accept()) != null){
//                logger.info("client connected");
//                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket,rpcRequestHandler,serviceRegistry));
//            }
//            threadPool.shutdown();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
