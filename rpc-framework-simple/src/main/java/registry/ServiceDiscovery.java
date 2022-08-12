package registry;

import dto.RpcRequest;
import extension.SPI;
import remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * service discovery
 *
 * @author shuang.kou
 * @createTime 2020年06月01日 15:16:00
 */
@SPI
public interface ServiceDiscovery {
    /**
     * 通过 rpcServiceName 查找服务
     *
     * @param rpcRequest rpc service pojo
     * @return service address
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
