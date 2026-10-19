package imwhs.krit_server_sandbox.exception.account;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import lombok.Getter;

@Getter
public class AccountDuplicateHandleException extends RuntimeException {

    private final ErrorType errorType = ErrorType.ACCOUNT_DUPLICATE_HANDLE;

    public AccountDuplicateHandleException(String handle) {
        super("이미 " + handle + " 공개 식별자(핸들)를 사용하고 있는 계정이 있어요.");
    }

}
