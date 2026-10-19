package imwhs.krit_server_sandbox.exception.account;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountNotFoundException extends RuntimeException {

    private final String errorCode = "ACCOUNT_NOT_FOUND";

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public AccountNotFoundException(Long id) {
        super("ID가 " + id + "인 계정을 찾을 수 없어요.");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

}
