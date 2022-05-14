public class RpcFrameworkSimpleMain {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy  = new RpcClientProxy("127.0.0.1",9999);
        /**
         * // TODO: 2022/5/14 为什么HelloService是被代理的类 
         * // TODO 看一下代理类的实现，为什么可以传入HelloService
         */
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String Hello = helloService.Hello(new Hello("111","222"));
        System.out.println(Hello);
        
    }
}
