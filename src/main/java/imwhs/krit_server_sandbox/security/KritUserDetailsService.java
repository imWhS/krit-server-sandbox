package imwhs.krit_server_sandbox.security;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.exception.account.AccountNotFoundException;
import imwhs.krit_server_sandbox.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * AccountRepository를 통해 Spring Security가 인증에 사용할 사용자 정보를 조회하고 제공합니다.
 */
@RequiredArgsConstructor
@Service
public class KritUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmailAndDeletedAtIsNull(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        System.out.println(username + " 이메일 주소를 사용 중인 계정(" + account.getHandle() + ")을 찾았어요.");

        UserDetails userDetails = User.builder()
                .username(account.getEmail())
                .password(account.getEncodedPassword())
                .roles("USER")
                .build();

        return userDetails;
    }

}
