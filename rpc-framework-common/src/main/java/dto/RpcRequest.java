package dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 网络传输的实体类，客户端代理服务器将该实体类传输给客户端，包含要调用方法的各种信息
 */
@Data
@Builder
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[]paramTypes;
}
