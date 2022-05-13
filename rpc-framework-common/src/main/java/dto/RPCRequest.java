package dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * todo �Ǹ����
 * todo ���л�
 */
@Data
@Builder
public class RPCRequest implements Serializable {
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[]paramTypes;
}
