package imwhs.krit_server_sandbox.exception.account;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import lombok.Getter;

@Getter
public class AccountNotFoundException extends RuntimeException {

    private final ErrorType errorType = ErrorType.ACCOUNT_NOT_FOUND;

    public AccountNotFoundException(Long id) {
        super("ID가 " + id + "인 계정을 찾을 수 없어요.");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

}
