package enumeration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCode {
    SUCCESS(200,"���÷����ɹ�"),
    FAIL(500,"���÷���ʧ��"),
    NOT_FOUND_METHOD(500,"û���ҵ�����"),
    NOT_FOUND_CLASS(500,"δ�ҵ�ָ������");
    private final int code;
    private final String message;
    
}
