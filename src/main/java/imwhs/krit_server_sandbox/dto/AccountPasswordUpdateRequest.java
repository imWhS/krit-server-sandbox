package imwhs.krit_server_sandbox.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountPasswordUpdateRequest(
        @Size(min = 8)
        @NotBlank
        String password
) {}
