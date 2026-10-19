package imwhs.krit_server_sandbox.exception.account;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import lombok.Getter;

@Getter
public class AccountDuplicateEmailException extends RuntimeException {

    private final ErrorType errorType = ErrorType.ACCOUNT_DUPLICATE_EMAIL;

    public AccountDuplicateEmailException(String email) {
        super("이미 " + email + " 이메일 주소를 사용하고 있는 계정이 있어요.");
    }

}
