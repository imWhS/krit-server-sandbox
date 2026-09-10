package imwhs.krit_server_sandbox.repository;

import imwhs.krit_server_sandbox.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
