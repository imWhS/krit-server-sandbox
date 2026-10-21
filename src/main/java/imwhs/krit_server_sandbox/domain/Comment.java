package imwhs.krit_server_sandbox.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 댓글
     *
     * <p>
     *     Comment 1은 Account A만 작성할 수 있고, Comment 2는 Account B만 작성할 수 있는 것처럼
     *     하나의 Comment는 하나의 Account와 연관 관계를 가질 수 있기 때문에, Comment -> Account는 toOne 관계여야 합니다.
     *
     *     Account A는 Comment 1, Comment 2 처럼 여러 개의 댓글을 작성할 수 있는 것처럼
     *     하나의 Account는 여러 개의 Comment와 연관 관계를 가질 수 있기 때문에, Account -> Comment는 toMany 관계여야 합니다.
     *
     *     그래서 Comment - Account는 ManyToOne 관계를 가집니다.
     * </p>
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
     * <p>
     *     Comment 1은 Post A에만 달 수 있고, Comment 2는 Post B에만 달 수 있는 것처럼
     *     하나의 Comment는 하나의 Post와 연관 관계를 가질 수 있기 때문에, Comment -> Post는 toOne 관계여야 합니다.
     *
     *     Post A는 Comment 1, Comment 2 처럼 여러 개의 댓글이 달릴 수 있는 것처럼
     *     하나의 Post는 여러 개의 Comment와 연관 관계를 가질 수 있기 때문에, Post -> Comment는 toMany 관계여야 합니다.
     *
     *     그래서 Comment - Post는 ManyToOne 연관 관계를 가집니다.
     * </p>
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

    public static Comment create(Account author, Post post, String body) {
        validateBody(body);
        Comment comment = new Comment();
        comment.author = author;
        comment.post = post;
        comment.body = body;
        return comment;
    }

    private static void validateBody(String body) {
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("댓글의 본문이 유효하지 않아요.");
        }
    }

}
