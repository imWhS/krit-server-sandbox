package imwhs.krit_server_sandbox.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@BatchSize(size = 10)
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
     *     <li> 최대 40자 길이의 문자열을 사용할 수 있습니다. </li>
     *     <i> 다른 Tag의 이름과 중복될 수 없습니다. </i>
     * </ul>
     */
    @Column(length = 40, unique = true, nullable = false)
    private String name;

    public static Tag create(String name) {
        String normalizedName = validateAndNormalizeName(name);
        Tag tag = new Tag();
        tag.name = normalizedName;
        return tag;
    }

    private static String validateAndNormalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("태그의 이름이 유효하지 않아요.");
        }

        String normalized = name.strip();

        if (40 < normalized.length()) {
            throw new IllegalArgumentException("계정의 이름은 최대 40자 이하 길이의 문자열만 사용할 수 있어요.");
        }

        return normalized;
    }

}
