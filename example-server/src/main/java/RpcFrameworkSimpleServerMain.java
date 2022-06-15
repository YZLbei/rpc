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
    
        
        //  修改实现方式，通过map存放service解决只能注册一个service
//        System.out.println("后面的不会执行");
//        rpcServer.register(new HelloServiceImpl(), 9999);
    }
}
