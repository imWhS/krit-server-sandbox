package imwhs.krit_server_sandbox.dto;

import imwhs.krit_server_sandbox.domain.Account;
import lombok.Setter;

@Setter
public class AccountResponse {

    Long id;
    String handle;
    String email;
    String name;
    String imageUrl;

    public AccountResponse(Account account) {
        if (account == null || account.getId() == null) {
            throw new IllegalArgumentException("Account가 유효하지 않아서 응답 객체를 생성할 수 없어요.");
        }
        this.id = account.getId();
        this.handle = account.getHandle();
        this.email = account.getEmail();
        this.name = account.getName();
        this.imageUrl = account.getImageUrl();
    }

}
