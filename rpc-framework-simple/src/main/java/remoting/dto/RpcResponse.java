package remoting.dto;

import enumeration.RpcResponseCode;
import enums.RpcResponseCodeEnum;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 715745410605631233L;
    private String requestId;
    /**
     * ��Ӧ��
     */
    private Integer code;

    /**
     * ��Ӧ��Ϣ
     */
    private String message;
    
    /**
     * ��Ӧ����
     */
    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> RpcResponse<T> fail(RpcResponseCodeEnum rpcResponseCodeEnum) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(rpcResponseCodeEnum.getCode());
        response.setMessage(rpcResponseCodeEnum.getMessage());
        return response;
    }
    
}
