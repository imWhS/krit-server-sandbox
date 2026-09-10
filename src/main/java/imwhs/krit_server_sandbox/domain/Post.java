package imwhs.krit_server_sandbox.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Post extends BaseEntity {

    /**
     * 식별자 및 데이터베이스 기본 키(PK)
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Account author;

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

    private LocalDateTime deletedAt;

    private Long deletedBy;

    /**
     * 논리적으로 삭제합니다.
     * @param requesterId 삭제를 요청한 계정의 ID
     */
    public void softDelete(Long requesterId) {
        // 이미 논리적 삭제된 상태여도 멱등하게 처리합니다.
        if (deletedAt != null || deletedBy != null) { return; }
        deletedAt = LocalDateTime.now();
        deletedBy = requesterId;
    }

}
