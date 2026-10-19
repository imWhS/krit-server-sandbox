package imwhs.krit_server_sandbox.exception.account;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import imwhs.krit_server_sandbox.exception.BaseException;

public class AccountNotFoundException extends BaseException {

    public AccountNotFoundException(Long id) {
        super(
                ErrorType.ACCOUNT_NOT_FOUND,
                "ID가 " + id + "인 계정을 찾을 수 없어요.");
    }

    public AccountNotFoundException(String message) {
        super(ErrorType.ACCOUNT_NOT_FOUND, message);
    }

}
