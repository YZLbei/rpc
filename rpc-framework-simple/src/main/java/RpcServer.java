import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServer {
    private ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    /**
     * 线程池
     * todo 重新看一下
     */
    public RpcServer(){
        //线程池的参数
        int corePoolSize = 10;
        int maxPoolSize = 100;
        int keepAliveTime =1;
        BlockingQueue<Runnable>wordQueue = new LinkedBlockingQueue<Runnable>(1000);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.threadPool = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime,TimeUnit.MINUTES,wordQueue,threadFactory);
    }

    /**
     * 服务端主动注册服务
     *todo 修改为注解然后扫描
     */
    public void register(Object service,int port){
        //1.创建 ServerSocket 对象并且绑定一个端口
        try(ServerSocket server = new ServerSocket(port);) {
            logger.info("server start...");
            Socket socket;
            //2.通过 accept()方法监听客户端请求
            while ((socket = server.accept()) != null){
                logger.info("client connected");
                threadPool.execute(new WorkerThread(socket,service));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
