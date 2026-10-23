package imwhs.krit_server_sandbox.dto.account;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record AccountImageUrlUpdateRequest(
        @URL
        @NotBlank(message = "이미지의 URL은 필수 항목이에요.")
        String imageUrl
) {}
