package imwhs.krit_server_sandbox.dto;

import imwhs.krit_server_sandbox.domain.Account;

public record AccountResponse(
    Long id,
    String handle,
    String email,
    String name,
    String imageUrl
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getHandle(),
                account.getEmail(),
                account.getName(),
                account.getImageUrl()
                );
    }
}