import registry.DefaultServiceRegistry;
import remoting.socket.RpcServer;

public class RpcFrameworkSimpleServerMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        defaultServiceRegistry.registry(helloService);
        RpcServer rpcServer = new RpcServer(defaultServiceRegistry);
        rpcServer.start(9999);

        // TODO 修改实现方式，通过map存放service解决只能注册一个service
//        System.out.println("后面的不会执行");
//        rpcServer.register(new HelloServiceImpl(), 9999);
    }
}
