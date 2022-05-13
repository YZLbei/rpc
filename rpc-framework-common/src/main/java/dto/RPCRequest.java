package dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * todo 是干嘛的
 * todo 序列化
 */
@Data
@Builder
public class RPCRequest implements Serializable {
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[]paramTypes;
}
