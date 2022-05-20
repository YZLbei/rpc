import lombok.*;
import java.io.Serializable;

/**
 * todo：为什么序列化
 * 实体类，客户端可见
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

