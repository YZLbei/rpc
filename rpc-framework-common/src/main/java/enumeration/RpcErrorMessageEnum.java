package enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum RpcErrorMessageEnum {
    SERVICE_INVOCATION_FAILURE("�������ʧ��"),
    SERVICE_CAN_NOT_BE_NULL("ע��ķ�����Ϊ��");
    private final String message;
}
