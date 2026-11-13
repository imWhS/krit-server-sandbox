package imwhs.krit_server_sandbox.security.annotation;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Security Context에 Authentication으로 설정된 KritUserDetails를 통해 인증된 사용자의 계정 ID를 파라미터에 주입합니다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "id") // KritUserDetails.getId()로 KritUserDetails.id에 접근합니다.
public @interface AuthenticatedKritAccountId {
}
