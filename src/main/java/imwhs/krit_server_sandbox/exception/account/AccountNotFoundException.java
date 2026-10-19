package imwhs.krit_server_sandbox.exception.account;

import lombok.Getter;

public class AccountNotFoundException extends RuntimeException {

    @Getter
    private final String code = "ACCOUNT_NOT_FOUND";

    public AccountNotFoundException(Long id) {
        super("ID가 " + id + "인 계정을 찾을 수 없어요.");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

}
