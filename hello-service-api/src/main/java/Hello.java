
import lombok.*;


import java.io.Serializable;

/**
 * ���л�
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
