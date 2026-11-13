package imwhs.krit_server_sandbox.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String encodedSecretKey) {
        byte[] secretKeyBytes = Decoders.BASE64.decode(encodedSecretKey);
        this.secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String generateAccessToken(String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .claim("type", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(15, ChronoUnit.MINUTES)))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * JWT 형식의 액세스 토큰을 디코딩한 후 서명 및 만료 여부를 검증하고, 이메일 주소를 추출합니다.
     * @param accessToken 액세스 토큰
     * @return 이메일 주소
     */
    public String extractEmailFromAccessToken(String accessToken) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        if (!"access".equals(claims.get("type", String.class))) {
            throw new JwtException("액세스 토큰이 아니어서 이메일 주소를 추출할 수 없어요.");
        }

        String email = claims.getSubject();

        if (Objects.isNull(email) || email.isBlank()) {
            throw new JwtException("액세스 토큰의 이메일 주소가 유효하지 않아요.");
        }

        return email;
    }

}
