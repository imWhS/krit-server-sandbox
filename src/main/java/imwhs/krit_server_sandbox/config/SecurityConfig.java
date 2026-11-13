package imwhs.krit_server_sandbox.config;

import imwhs.krit_server_sandbox.security.JwtTokenProvider;
import imwhs.krit_server_sandbox.security.KritUserDetails;
import imwhs.krit_server_sandbox.security.KritUserDetailsService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import javax.crypto.SecretKey;
import java.util.Objects;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * username과 password 기반의 인증을 담당하는 AuthenticationManager를 Bean으로 등록합니다.
     * 인증 시 필요한 사용자 정보를 가져오기 위해 사용할 UserDetailsService와 암호를 검증하는 PasswordEncoder를 설정해야 합니다.
     * @param userDetailsService 사용자 정보를 가져오기 위해 사용할 UserDetailsService 구현체
     * @param passwordEncoder 암호를 검증할 PasswordEncoder 구현체
     * @return AuthenticationManager 인스턴스
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret}") String encodedSecretKey) {
        byte[] secretKeyBytes = Decoders.BASE64.decode(encodedSecretKey);
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey) // JWT의 서명을 검증할 때 사용할 비밀 키를 설정합니다.
                .macAlgorithm(MacAlgorithm.HS256) // 비밀 키로 JWT의 서명을 검증할 때 HS256 알고리즘을 사용하도록 설정합니다.
                .build();

        // 토큰 값에서 type claim을 꺼낸 후 access 여부를 검증하는 validator를 생성합니다.
        JwtClaimValidator<String> typeValidator = new JwtClaimValidator<>("type", "access"::equals);

        // 토큰 값의 claim을 검증할 기본 Jwt validator를 설정합니다.
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefault(), // exp, nbf 등의 기본 유효성 검사를 담당하는 validator를 생성 후 추가합니다.
                typeValidator // type의 access 여부를 검증하는 validator를 추가합니다.
        ));

        return jwtDecoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtTokenProvider jwtTokenProvider,
            UserDetailsService userDetailsService,
            KritUserDetailsService kritUserDetailsService) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())
                // 매 요청마다 토큰을 검증해서 인증 처리해야 하기 때문에 세션(JSESSIONID)을 생성해 인증 정보를 저장하거나, 복원하지 않습니다.
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v0/accounts").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v0/auth/login").permitAll()
                        .anyRequest().authenticated())
//                .addFilterBefore(
//                        // Tomcat FilterChain의 AuthenticationFilter 이전 위치에 HTTP 요청의 액세스 토큰을 검사하는 필터를 추가합니다.
//                        new AccessTokenFilter(
//                                jwtTokenProvider,
//                                userDetailsService),
//                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        // 인증 없이 인증을 요구하는 엔드포인트로 HTTP 요청한 경우, 401 UNAUTHORIZED 상태 코드를 응답합니다.
                        exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(token -> {
                            // 액세스 토큰에서 이메일 주소를 추출한 후, 이메일 주소에 해당하는 사용자 정보를
                            // UserDefault에 담아 JwtAuthenticationToken을 생성합니다.
                            String email = token.getSubject();

                            if (Objects.isNull(email) || email.isBlank()) {
                                throw new BadCredentialsException(
                                        "액세스 토큰에 유효한 이메일 주소가 없어서 인증을 더 이상 진행할 수 없어요.");
                            }

                            KritUserDetails userDetails = kritUserDetailsService.loadUserByUsername(email);
                            return new JwtAuthenticationToken(
                                    token,
                                    userDetails, // Security Context를 통해 스레드에서 사용자 정보에 접근할 수 있게 인증된 사용자를 principal로 설정합니다.
                                    userDetails.getAuthorities());
                        })));

        return http.build();
    }

}