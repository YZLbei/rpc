package remoting.dto;

import lombok.*;

import java.io.Serializable;

/**
 * ���紫���ʵ���࣬�ͻ��˴������������ʵ���ഫ����ͻ��ˣ�����Ҫ���÷����ĸ�����Ϣ
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
