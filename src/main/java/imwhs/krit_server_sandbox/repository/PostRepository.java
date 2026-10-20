package imwhs.krit_server_sandbox.repository;

import imwhs.krit_server_sandbox.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    List<Post> findAllByDeletedAtIsNull();

    @Query("""
            SELECT p
            FROM Post p
            JOIN FETCH p.author
            WHERE p.deletedAt IS null
            """)
    List<Post> findAllWithAuthorByDeletedAtIsNull();

}
