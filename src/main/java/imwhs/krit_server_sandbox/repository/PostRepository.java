package imwhs.krit_server_sandbox.repository;

import imwhs.krit_server_sandbox.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByIdAndDeletedAtIsNull(Long id);

    @Query("""
            SELECT p
            FROM Post p
            JOIN FETCH p.author
            WHERE
                p.id = :id AND
                p.deletedAt IS null
    """)
    Optional<Post> findWithAuthorByIdDeletedAtIsNull(@Param("id") Long id);

    @Query(
            value = """
            SELECT p
            FROM Post p
            JOIN FETCH p.author
            WHERE p.deletedAt IS null
            """,
            countQuery = """
            SELECT COUNT(p)
            FROM Post p
            WHERE p.deletedAt IS null
            """
    )
    Page<Post> findAllWithAuthorByDeletedAtIsNull(Pageable pageable);

}
