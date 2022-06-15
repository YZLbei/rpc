import registry.DefaultServiceRegistry;
import transport.socket.SocketRpcServer;

public class RpcFrameworkSimpleServerMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        
        HelloService helloService2 = new HelloServiceImpl2();
        
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        defaultServiceRegistry.registry(helloService);
        defaultServiceRegistry.registry(helloService2);
        SocketRpcServer rpcServer = new SocketRpcServer(defaultServiceRegistry);
        rpcServer.start(9999);
    
        
        //  �޸�ʵ�ַ�ʽ��ͨ��map���service���ֻ��ע��һ��service
//        System.out.println("����Ĳ���ִ��");
//        rpcServer.register(new HelloServiceImpl(), 9999);
    }
}
