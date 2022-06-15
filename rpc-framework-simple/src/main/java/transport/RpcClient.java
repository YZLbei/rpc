package transport;

import dto.RpcRequest;

/**
 * @Auther: YuZhenLong
 * @Date: 2022/6/15 19:20
 * @Description:
 */
public interface RpcClient {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
