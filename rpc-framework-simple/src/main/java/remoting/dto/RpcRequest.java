package remoting.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 网络传输的实体类，客户端代理服务器将该实体类传输给客户端，包含要调用方法的各种信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[]paramTypes;
    private String requestId;
    private String version;
    private String group;
    public String getRpcServiceName() {
        return this.getInterfaceName() + this.getGroup() + this.getVersion();
    }
}
