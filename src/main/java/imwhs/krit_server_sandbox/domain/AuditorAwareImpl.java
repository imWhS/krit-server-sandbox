package imwhs.krit_server_sandbox.domain;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("admin"); // TODO: Spring Security 인증 구현 후 SecurityContextHolder 통해 세션의 사용자 정보로 설정
    }

}
