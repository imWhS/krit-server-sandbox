package imwhs.krit_server_sandbox.domain;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * JPA Auditing에게 현재 작업자 정보를 제공합니다.
 */
@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(1L); // TODO: Spring Security 인증 구현 후 SecurityContextHolder 통해 세션의 사용자 정보로 설정
    }

}
