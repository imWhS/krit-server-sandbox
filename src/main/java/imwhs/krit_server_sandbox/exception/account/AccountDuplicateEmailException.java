package imwhs.krit_server_sandbox.exception.account;

public class AccountDuplicateEmailException extends RuntimeException {

    public AccountDuplicateEmailException(String email) {
        super("이미 " + email + " 이메일 주소를 사용하고 있는 계정이 있어요.");
    }

}
