public class RpcFrameworkSimpleMain {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy  = new RpcClientProxy("127.0.0.1",9999);
        /**
         * // TODO: 2022/5/14 ΪʲôHelloService�Ǳ�������� 
         * // TODO ��һ�´������ʵ�֣�Ϊʲô���Դ���HelloService
         */
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String Hello = helloService.Hello(new Hello("111","222"));
        System.out.println(Hello);
        
    }
}
