public class RpcFrameworkSimpleMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService,9999);

        // TODO �޸�ʵ�ַ�ʽ��ͨ��map���service���ֻ��ע��һ��service
        System.out.println("����Ĳ���ִ��");
        rpcServer.register(new HelloServiceImpl(), 9999);
    }
}
