package dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * ���紫���ʵ���࣬�ͻ��˴������������ʵ���ഫ����ͻ��ˣ�����Ҫ���÷����ĸ�����Ϣ
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
