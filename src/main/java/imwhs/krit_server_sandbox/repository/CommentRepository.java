package imwhs.krit_server_sandbox.repository;

import imwhs.krit_server_sandbox.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndDeletedAtIsNull(Long id);

    @Query("""
            SELECT c
            FROM Comment c
            JOIN FETCH c.author
            WHERE
                c.id = :id AND
                c.deletedAt IS null
            """)
    Optional<Comment> findWithAuthorByIdAndDeletedAtIsNull(@Param("id") Long id);

    @Query(value = """
            SELECT c
            FROM Comment c
            JOIN FETCH c.author
            WHERE
                c.post.id = :id AND
                c.deletedAt IS null
            """,
            countQuery = """
            SELECT COUNT(c)
            FROM Comment c
            WHERE 
                c.post.id = :id AND 
                c.deletedAt IS null
            """
    )
    Page<Comment> findWithAuthorByPostIdAndDeletedAtIsNull(@Param("id") Long id, Pageable pageable);

}
