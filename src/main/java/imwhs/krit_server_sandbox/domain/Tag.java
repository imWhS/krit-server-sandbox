package imwhs.krit_server_sandbox.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 이름
     *
     * <ul>
     *     <li> 최대 40자 길이의 문자열을 사용할 수 있습니다.</li>
     * </ul>
     */
    @Column(length = 40, nullable = false)
    private String name;

    public static Tag create(String name) {
        validateName(name);
        Tag tag = new Tag();
        tag.name = name;
        return tag;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("태그의 이름이 유효하지 않아요.");
        }
    }

}
