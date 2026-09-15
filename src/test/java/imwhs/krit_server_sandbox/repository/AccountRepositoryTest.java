package imwhs.krit_server_sandbox.repository;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.domain.AuditorAwareImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@DataJpaTest
@Import(AuditorAwareImpl.class) // @DataJpaTest가 JPA 컴포넌트만 스프링 컨테이너에 띄우기에, 작업자 정보 주입을 위해 별도로 컨테이너에 포함
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("계정을 저장하면 JPA Auditing에 의해 생성 시점과 작업자(생성자)가 자동 주입되어야 한다.")
    void jpaAuditingTest() {
        // given
        Account account = Account.create("heextory", "heextory@icloud.com", "encodedPassword");

        // when
        accountRepository.save(account);

        // then
        Optional<Account> foundAccount = accountRepository.findById(account.getId());
        Assertions.assertThat(foundAccount.isPresent()).isTrue();
        Assertions.assertThat(foundAccount.get().getCreatedAt()).isNotNull();
        Assertions.assertThat(foundAccount.get().getUpdatedAt()).isNotNull();
        Assertions.assertThat(foundAccount.get().getCreatedBy()).isNotNull();
        Assertions.assertThat(foundAccount.get().getUpdatedBy()).isNotNull();
        Assertions.assertThat(foundAccount.get().getDeletedBy()).isNull();
        Assertions.assertThat(foundAccount.get().getDeletedAt()).isNull();
    }

}
