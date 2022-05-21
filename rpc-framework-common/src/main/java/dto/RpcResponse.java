package dto;

import enumeration.RpcResponseCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 715745410605631233L;
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
    
    public static<T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCode.SUCCESS.getCode());
        if (null !=data){
            response.setData(data);
        }
        return response;
    }
    
    public  static <T> RpcResponse<T> fail(RpcResponseCode rpcResponseCode){
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(rpcResponseCode.getCode());
        response.setMessage(rpcResponseCode.getMessage());
        return response;
    }
    
}
