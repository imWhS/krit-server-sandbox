package imwhs.krit_server_sandbox.repository;

import imwhs.krit_server_sandbox.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
