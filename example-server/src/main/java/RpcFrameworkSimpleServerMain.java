import registry.DefaultServiceRegistry;
import transport.socket.RpcServer;

public class RpcFrameworkSimpleServerMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        
        HelloService helloService2 = new HelloServiceImpl2();
        
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        defaultServiceRegistry.registry(helloService);
        defaultServiceRegistry.registry(helloService2);
        RpcServer rpcServer = new RpcServer(defaultServiceRegistry);
        rpcServer.start(9999);
    
        
        //  �޸�ʵ�ַ�ʽ��ͨ��map���service���ֻ��ע��һ��service
//        System.out.println("����Ĳ���ִ��");
//        rpcServer.register(new HelloServiceImpl(), 9999);
    }
}
