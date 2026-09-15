package imwhs.krit_server_sandbox.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record AccountImageUrlUpdateRequest(
        @URL
        @NotBlank
        String imageUrl
) {}
