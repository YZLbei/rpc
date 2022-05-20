import lombok.*;
import java.io.Serializable;

/**
 * todo��Ϊʲô���л�
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

