package imwhs.krit_server_sandbox.security;

import imwhs.krit_server_sandbox.domain.Account;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class KritUserDetails implements UserDetails {

    private final Account account;

    @Override
    public @Nullable String getPassword() {
        return account.getEncodedPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    public Long getId() {
        return account.getId();
    }

    public String getEmail() {
        return account.getEmail();
    }

    public String getHandle() {
        return account.getHandle();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

}
