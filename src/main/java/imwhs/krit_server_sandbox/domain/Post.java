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
     * <ul>
     *     <li> 최대 100자 이하 길이의 문자열을 사용할 수 있습니다. </li>
     * </ul>
     */
    @Column(length = 100)
    private String title;

    /**
     * 본문
     * <ul>
     *     <li> 대용량 텍스트를 저장하기 위해 VARCHAR 대신 TEXT 타입을 사용합니다. </li>
     * </ul>
     */
    @Column(columnDefinition = "TEXT", nullable = false)
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

    public Post updateTitle(String title) {
        validateTitle(title);
        this.title = title;
        return this;
    }

    public Post updateContent(String content) {
        this.content = content;
        return this;
    }

    public static Post create(Account author, String title, String content) {
        validateAuthor(author);
        validateTitle(title);
        Post post = new Post();
        post.author = author;
        post.title = title;
        post.content = content;
        return post;
    }

    private static void validateAuthor(Account author) {
        if (author == null) {
            throw new IllegalStateException("게시물의 작성자가 유효하지 않아요.");
        }
    }

    private static void validateTitle(String title) {
        if (title != null && 100 < title.length()) {
            throw new IllegalArgumentException("게시물의 제목은 최대 100자 이하 길이의 문자열만 사용할 수 있어요.");
        }
    }

}
