import lombok.*;
import java.io.Serializable;

/**
 * Ϊʲô���л�����Ϊ��ʵ�����
 * ʵ���࣬�ͻ��˿ɼ�
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
public class Hello implements Serializable {
    private String message;
    private String description;
}

