public class RpcFrameworkSimpleMain {
    public static void main(String[] args) {
        //rpcClientProxy�Ǵ���ͻ��˷������󣬰󶨵�ַ�Ͷ˿ں�
        RpcClientProxy rpcClientProxy  = new RpcClientProxy("127.0.0.1",9999);
        /**
         * // 2022/5/14 ΪʲôHelloService�Ǳ�������� 
         * helloSerice�ǽӿڣ����ǿͻ�����Ҫ���õķ���
         */
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String Hello = helloService.Hello(new Hello("111","222"));
        System.out.println(Hello);
    }
}
