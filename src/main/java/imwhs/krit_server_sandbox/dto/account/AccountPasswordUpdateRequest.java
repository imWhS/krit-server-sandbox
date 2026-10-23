package imwhs.krit_server_sandbox.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountPasswordUpdateRequest(
        @Size(min = 8, message = "암호는 최소 8자 이상의 길이여야 해요.")
        @NotBlank(message = "암호는 필수 항목이에요.")
        String password
) {}
