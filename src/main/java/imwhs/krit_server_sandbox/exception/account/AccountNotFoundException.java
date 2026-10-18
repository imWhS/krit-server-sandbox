package imwhs.krit_server_sandbox.exception.account;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("ID가 " + id + "인 계정을 찾을 수 없어요.");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

}
