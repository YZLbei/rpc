import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import enumeration.RpcErrorMessageEnum;
import exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServer {
    private ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    /**
     * �̳߳�
     * todo ���¿�һ��
     */
    public RpcServer(){
        //�̳߳صĲ���
        int corePoolSize = 10;
        int maxPoolSize = 100;
        int keepAliveTime =1;
        BlockingQueue<Runnable>wordQueue = new LinkedBlockingQueue<Runnable>(1000);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime,TimeUnit.MINUTES,wordQueue,threadFactory);
    }

    /**
     * ���������ע�����
     *TODO 1.����һ�� hashmap �����ص�service
     *     2. �޸�Ϊɨ��ע��ע��
     */
    public void register(Object service,int port){
        if(service==null){
            throw  new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_NULL);
        }
        //1.���� ServerSocket �����Ұ�һ���˿�
        try(ServerSocket server = new ServerSocket(port);) {
            logger.info("server start...");
            Socket socket;
            //2.ͨ�� accept()���������ͻ�������
            while ((socket = server.accept()) != null){
                logger.info("client connected");
                threadPool.execute(new ClientMessageHandlerThread(socket,service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
