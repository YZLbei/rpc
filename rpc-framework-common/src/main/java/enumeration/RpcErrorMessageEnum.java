package enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum RpcErrorMessageEnum {
    SERVICE_INVOCATION_FAILURE("�������ʧ��"),
    SERVICE_CAN_NOT_BE_FOUND("û���ҵ�ָ���ķ���"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("ע��ķ���û��ʵ���κνӿ�");
    private final String message;
}
