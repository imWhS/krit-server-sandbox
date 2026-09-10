package imwhs.krit_server_sandbox.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Post extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    /**
     * 제목
     */
    @Column(length = 100, nullable = false)
    private String title;

    /**
     * 본문
     * <ul>
     *     <li> 대용량 텍스트를 저장하기 위해 VARCHAR 대신 TEXT 타입을 사용합니다. </li>
     * </ul>
     */
    @Column(columnDefinition = "TEXT")
    private String content;

}
