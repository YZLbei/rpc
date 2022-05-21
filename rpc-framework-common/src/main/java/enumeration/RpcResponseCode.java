package enumeration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCode {
    SUCCESS(200,"调用方法成功"),
    FAIL(500,"调用方法失败"),
    NOT_FOUND_METHOD(500,"没有找到方法"),
    NOT_FOUND_CLASS(500,"未找到指定的类");
    private final int code;
    private final String message;
    
}
