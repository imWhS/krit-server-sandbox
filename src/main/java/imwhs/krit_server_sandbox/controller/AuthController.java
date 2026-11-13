package imwhs.krit_server_sandbox.controller;

import imwhs.krit_server_sandbox.dto.auth.LoginRequest;
import imwhs.krit_server_sandbox.dto.auth.LoginResponse;
import imwhs.krit_server_sandbox.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v0/auth")
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 가입된 계정으로 로그인 합니다.
     * 로그인은 사용자 계정 인증 및 토큰 발급 과정으로 구성됩니다.
     * @param request 로그인 요청 인스턴스
     * @return 로그인 정보를 담은 응답 인스턴스
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // 로그인 요청 데이터의 이메일 주소로 AuthenticationManager를 통해 사용자 계정 인증을 시도합니다.
        UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(
                request.email(),
                request.password());
        Authentication authenticated = authenticationManager.authenticate(unauthenticated);

        // AuthenticationManager를 통한 사용자 계정 인증에 성공한 경우, 액세스 토큰을 생성합니다.
        String accessToken = jwtTokenProvider.generateAccessToken(authenticated.getName());

        // 로그인 완료 HTTP 응답을 생성합니다.
        return ResponseEntity.ok(new LoginResponse(accessToken));
    }

}
