package imwhs.krit_server_sandbox.exception.account;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountDuplicateEmailException extends RuntimeException {

    private final String errorCode = "ACCOUNT_DUPLICATE_EMAIL";

    private final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public AccountDuplicateEmailException(String email) {
        super("이미 " + email + " 이메일 주소를 사용하고 있는 계정이 있어요.");
    }

}
