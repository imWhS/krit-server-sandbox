package imwhs.krit_server_sandbox.exception.account;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import imwhs.krit_server_sandbox.exception.BaseException;

public class AccountUnauthorizedException extends BaseException {

    public AccountUnauthorizedException() {
        super(
                ErrorType.ACCOUNT_UNAUTHORIZED,
                "요청을 처리하기 위한 권한이 없어요.");
    }

    public AccountUnauthorizedException(String message) {
        super(ErrorType.ACCOUNT_NOT_FOUND, message);
    }

}
