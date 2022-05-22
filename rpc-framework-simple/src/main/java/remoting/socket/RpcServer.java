package remoting.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import enumeration.RpcErrorMessageEnum;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceRegistry;
import remoting.RpcRequestHandler;

public class RpcServer {
    private static final int CORE_POOL_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private RpcRequestHandler rpcRequestHandler = new RpcRequestHandler();
    private final ServiceRegistry serviceRegistry;
    private ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    /**
     * �̳߳�
     * todo ���¿�һ��
     */
    public RpcServer(ServiceRegistry serviceRegistry) {
        //�̳߳صĲ���
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    /**
     * ���������ע�����
     *TODO
     *     2. �޸�Ϊɨ��ע��ע��
     */
    public void start(int port){
        //1.���� ServerSocket �����Ұ�һ���˿�
        try(ServerSocket server = new ServerSocket(port);) {
            logger.info("server start...");
            Socket socket;
            //2.ͨ�� accept()���������ͻ�������
            while ((socket = server.accept()) != null){
                logger.info("client connected");
                threadPool.execute(new RpcRequestHandlerRunnable(socket,rpcRequestHandler,serviceRegistry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}