package imwhs.krit_server_sandbox.exception.account;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import imwhs.krit_server_sandbox.exception.BaseException;

public class AccountDuplicateHandleException extends BaseException {

    public AccountDuplicateHandleException(String handle) {
        super(ErrorType.ACCOUNT_DUPLICATE_HANDLE, "이미 " + handle + " 공개 식별자(핸들)를 사용하고 있는 계정이 있어요.");
    }

}
