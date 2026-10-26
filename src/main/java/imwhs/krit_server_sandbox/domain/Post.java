package imwhs.krit_server_sandbox.domain;

import imwhs.krit_server_sandbox.exception.account.AccountUnauthorizedException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseEntity {

    /**
     * 식별자 및 데이터베이스 기본 키(PK)
     */
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
     * 게시물의 태그
     *
     * <ul>
     *     <li> PostTag는 Post와 강하게 연관 관계를 갖기 때문에, Post의 영속 상태를 PostTag에 전이합니다. </li>
     *     <li> tags에서 특정 PostTag가 제외되어 Post와 연관 관계를 가지지 않는 PostTag는 고아로 취급해 삭제합니다. </li>
     * </ul>
     */
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> tags = new HashSet<>();

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
     *
     * <ul>
     *     <li> 대용량 텍스트를 저장하기 위해 VARCHAR 대신 TEXT 타입을 사용합니다. </li>
     * </ul>
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

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
     * @param actorId 삭제를 요청한 Account의 ID
     */
    public void softDelete(Long actorId) {
        if (actorId == null) {
            throw new IllegalArgumentException("게시물 삭제를 요청한 계정의 ID가 유효하지 않아요.");
        }

        if (!Objects.equals(actorId, this.author.getId())) {
            throw new AccountUnauthorizedException();
        }

        // 이미 논리적 삭제된 상태여도 멱등하게 처리합니다.
        if (deletedAt != null || deletedBy != null) { return; }

        deletedAt = LocalDateTime.now();
        deletedBy = actorId;
    }

    public Post updateTitle(String title) {
        if (isSoftDeleted()) {
            throw new IllegalArgumentException("논리적 삭제 처리된 게시물이어서 제목을 업데이트할 수 없어요.");
        }

        String normalized = validateAndNormalizeTitle(title);
        this.title = normalized;
        return this;
    }

    public Post updateContent(String content) {
        if (isSoftDeleted()) {
            throw new IllegalArgumentException("논리적 삭제 처리된 게시물이어서 본문을 업데이트할 수 없어요.");
        }

        this.content = content;
        return this;
    }

    /**
     * Tag를 답니다.
     * @param tag Post에 달 Tag
     */
    public void addTag(Tag tag) {
        PostTag postTag = PostTag.create(this, tag);
        tags.add(postTag);
    }

    public static Post create(Account author, String title, String content) {
        validateAuthor(author);
        String normalizedTitle = validateAndNormalizeTitle(title);
        Post post = new Post();
        post.author = author;
        post.title = normalizedTitle;
        post.content = content;
        return post;
    }

    private static void validateAuthor(Account author) {
        if (author == null) {
            throw new IllegalArgumentException("게시물의 작성자가 유효하지 않아요.");
        }
    }

    private static String validateAndNormalizeTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("게시물의 제목이 유효하지 않아요.");
        }

        String stripped = title.strip();

        if (100 < stripped.length()) {
            throw new IllegalArgumentException("게시물의 제목은 최대 100자 이하 길이의 문자열만 사용할 수 있어요.");
        }

        return stripped;
    }

}
