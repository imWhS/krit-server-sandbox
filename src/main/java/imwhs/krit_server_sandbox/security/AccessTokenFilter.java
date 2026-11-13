package imwhs.krit_server_sandbox.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("FilterChain의 AccessTokenFilter가 호출됐어요. | 요청 URL: " + requestURI);

        String authorizationHeader = request.getHeader("Authorization");
        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            // HTTP 요청의 Authorization 헤더에 액세스 토큰 관련 값이 없다면,
            // 더 이상 진행하지 않고 FilterChain의 다음 필터로 요청 처리를 이어갑니다.
            System.out.println("HTTP 요청의 Authorization 헤더에 액세스 토큰 관련 값이 없어서 SecurityContext를 설정하지 않아요.");
            filterChain.doFilter(request, response);
            return;
        }

        // HTTP 요청의 Authorization 헤더에서 액세스 토큰 값을 추출합니다.
        String accessToken = authorizationHeader.substring(7);

        try {
            // 액세스 토큰 값에서 이메일 주소를 추출합니다.
            String email = jwtTokenProvider.extractEmailFromAccessToken(accessToken);
            System.out.println("액세스 토큰에서 이메일 주소를 추출했어요: " + email);

            // 해당 이메일 주소를 사용 중인 계정의 정보를 포함하는 UserDetails를 조회합니다.
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // UserDetails를 이용해 Authentication을 생성합니다.
            UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                    userDetails,
                    null, // AuthenticationManager.authenticate()로 계정 인증을 하는 게 아니어서 암호는 생략해도 됩니다.
                    userDetails.getAuthorities());

            // SecurityContext를 생성한 후 Authentication을 설정합니다.
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationToken);

            // 현재 HTTP 요청 스레드에 SecurityContext를 설정합니다.
            SecurityContextHolder.setContext(securityContext);
            System.out.println(authenticationToken.getName() + " JWT 인증 완료");
        } catch (JwtException | IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println("JWT 검증을 실패했어요. 요청 처리를 종료할게요.");
            return;
        }

        // FilterChain의 다음 필터로 요청 처리를 이어갑니다.
        filterChain.doFilter(request, response);
    }

}
