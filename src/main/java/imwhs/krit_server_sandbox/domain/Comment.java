package imwhs.krit_server_sandbox.domain;

import imwhs.krit_server_sandbox.exception.account.AccountUnauthorizedException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 작성자
     *
     * <ul>
     *     <li> author를 참조할 때만 Account를 조회하도록 지연 로딩 설정합니다. </li>
     * </ul>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Account author;

    /**
     * 게시물
     *
     * <ul>
     *     <li> post를 참조할 때만 Post를 조회하도록 지연 로딩 설정합니다. </li>
     * </ul>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /**
     * 본문
     */
    @Column(length = 200, nullable = false)
    private String body; // content가 낫나?

    private LocalDateTime deletedAt;

    private Long deletedBy;

    /**
     * 논리적 삭제 여부를 나타냅니다.
     * @return 논리적 삭제 여부
     */
    public boolean isSoftDeleted() {
        return deletedAt != null;
    }

    /**
     * 논리적으로 삭제합니다.
     * @param actorId 삭제를 요청한 계정의 ID
     */
    public void softDelete(Long actorId) {
        if (actorId == null) {
            throw new IllegalArgumentException("댓글 삭제를 요청한 계정의 ID가 유효하지 않아요.");
        }

        if (!Objects.equals(actorId, this.author.getId())) {
            throw new AccountUnauthorizedException();
        }

        // 이미 논리적 삭제된 상태여도 멱등하게 처리합니다.
        if (deletedAt != null || deletedBy != null) { return; }

        deletedAt = LocalDateTime.now();
        deletedBy = actorId;
    }

    public Comment updateBody(String body) {
        if (isSoftDeleted()) {
            throw new IllegalArgumentException("논리적 삭제 처리된 댓글이어서 본문을 업데이트할 수 없어요.");
        }

        validateBody(body);
        this.body = body;
        return this;
    }

    public static Comment create(Account author, Post post, String body) {
        validateAuthor(author);
        validatePost(post);
        validateBody(body);
        Comment comment = new Comment();
        comment.author = author;
        comment.post = post;
        comment.body = body;
        return comment;
    }

    private static void validateAuthor(Account author) {
        if (author == null) {
            throw new IllegalArgumentException("댓글의 작성자가 유효하지 않아요.");
        }
    }

    private static void validatePost(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("댓글의 게시물이 유효하지 않아요.");
        }
    }

    private static void validateBody(String body) {
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("댓글의 본문이 유효하지 않아요.");
        }
    }
}
