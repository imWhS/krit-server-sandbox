package imwhs.krit_server_sandbox.repository;

import imwhs.krit_server_sandbox.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByName(String name);

    void deleteByName(String name);

    @Query("SELECT t " +
            "FROM Tag t " +
            "WHERE " +
            "   t.name IN :names")
    Set<Tag> findAllByNameIn(@Param("names") Set<String> names);

}
