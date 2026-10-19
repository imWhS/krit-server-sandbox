package imwhs.krit_server_sandbox.exception.account;

import lombok.Getter;

@Getter
public class AccountDuplicateEmailException extends RuntimeException {

    private final String code = "ACCOUNT_DUPLICATE_EMAIL";

    public AccountDuplicateEmailException(String email) {
        super("이미 " + email + " 이메일 주소를 사용하고 있는 계정이 있어요.");
    }

}
