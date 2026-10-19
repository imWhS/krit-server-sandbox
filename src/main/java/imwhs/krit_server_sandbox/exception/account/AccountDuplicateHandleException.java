package imwhs.krit_server_sandbox.exception.account;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountDuplicateHandleException extends RuntimeException {

    private final String errorCode = "ACCOUNT_DUPLICATE_HANDLE";

    private final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public AccountDuplicateHandleException(String handle) {
        super("이미 " + handle + " 공개 식별자(핸들)를 사용하고 있는 계정이 있어요.");
    }

}
