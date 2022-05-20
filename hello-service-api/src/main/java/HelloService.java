/**
 * 客户端想要远程访问的方法
 * 
 * 客户端可见的是接口
 * 实现类在服务器
 */
public interface HelloService {
    String Hello(Hello hello);
}
