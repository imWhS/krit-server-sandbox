package imwhs.krit_server_sandbox.repository;

import imwhs.krit_server_sandbox.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByHandleAndDeletedAtIsNull(String handle);

    boolean existsByHandleAndDeletedAtIsNull(String handle);

    boolean existsByEmailAndDeletedAtIsNull(String email);

    Optional<Account> findByIdAndDeletedAtIsNull(Long id);

    Optional<Account> findByEmailAndDeletedAtIsNull(String email);

    boolean existsByIdAndDeletedAtIsNull(Long id);

}
