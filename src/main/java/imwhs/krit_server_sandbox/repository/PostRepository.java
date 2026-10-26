package imwhs.krit_server_sandbox.repository;

import imwhs.krit_server_sandbox.domain.Post;
import imwhs.krit_server_sandbox.dto.post.PostBaseInfo;
import imwhs.krit_server_sandbox.dto.post.PostTagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByIdAndDeletedAtIsNull(Long id);

    @Query("""
            SELECT p
            FROM Post p
            JOIN FETCH p.author
            LEFT JOIN FETCH p.tags t
            LEFT JOIN FETCH t.tag
            WHERE
                p.id = :id AND
                p.deletedAt IS null
            """)
    Optional<Post> findWithAuthorTagsByIdDeletedAtIsNull(@Param("id") Long id); // -> findWithAuthorByIdDeletedAtIsNull에서 변경

    @Query(
            value = """
            SELECT new imwhs.krit_server_sandbox.dto.post.PostBaseInfo(
                p.id,
                p.title,
                p.content,
                a.name
            )
            FROM Post p
            JOIN p.author a
            WHERE
                p.deletedAt IS null
            """,
            countQuery = """
                SELECT COUNT(p)
                FROM Post p
                WHERE
                    p.deletedAt IS null
            """
    )
    Page<PostBaseInfo> findAllBaseInfosByDeletedAtIsNull(Pageable pageable);

    @Query("""
        SELECT new imwhs.krit_server_sandbox.dto.post.PostTagDto(
            pt.post.id,
            t.name
        )
        FROM PostTag pt
        JOIN pt.tag t
        WHERE
            pt.post.id IN :postIds
    """)
    List<PostTagDto> findAllTagNamesByPostIds(@Param("postIds") Set<Long> postIds);

    @Query("""
        SELECT new imwhs.krit_server_sandbox.dto.post.PostBaseInfo(
            p.id,
            p.title,
            p.content,
            a.name
        )
        FROM Post p
        JOIN p.author a
        WHERE
            p.deletedAt IS null
        ORDER BY p.id DESC
    """)
    Slice<PostBaseInfo> findFirstBaseInfosByDeletedAtIsNull(Pageable pageable);

    @Query("""
        SELECT new imwhs.krit_server_sandbox.dto.post.PostBaseInfo(
            p.id,
            p.title,
            p.content,
            a.name
        )
        FROM Post p
        JOIN p.author a
        WHERE
            p.id < :cursor AND
            p.deletedAt IS null
    """)
    Slice<PostBaseInfo> findNextBaseInfosByCursorDeletedAtIsNull(@Param("cursor") Long cursor, Pageable pageable);

}
