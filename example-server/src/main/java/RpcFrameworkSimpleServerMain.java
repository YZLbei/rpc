import registry.DefaultServiceRegistry;
import remoting.socket.RpcServer;

public class RpcFrameworkSimpleServerMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        defaultServiceRegistry.registry(helloService);
        RpcServer rpcServer = new RpcServer(defaultServiceRegistry);
        rpcServer.start(9999);

        // TODO �޸�ʵ�ַ�ʽ��ͨ��map���service���ֻ��ע��һ��service
//        System.out.println("����Ĳ���ִ��");
//        rpcServer.register(new HelloServiceImpl(), 9999);
    }
}
