package imwhs.krit_server_sandbox.exception.account;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import imwhs.krit_server_sandbox.exception.BaseException;

public class AccountDuplicateEmailException extends BaseException {

    public AccountDuplicateEmailException(String email) {
        super(ErrorType.ACCOUNT_DUPLICATE_EMAIL, "이미 " + email + " 이메일 주소를 사용하고 있는 계정이 있어요.");
    }

}
