public class RpcFrameworkSimpleMain {
    public static void main(String[] args) {
        //rpcClientProxy是代理客户端发出请求，绑定地址和端口号
        RpcClientProxy rpcClientProxy  = new RpcClientProxy("127.0.0.1",9999);
        /**
         * // 2022/5/14 为什么HelloService是被代理的类 
         * helloSerice是接口，就是客户端想要调用的方法
         */
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String Hello = helloService.Hello(new Hello("111","222"));
        System.out.println(Hello);
    }
}
