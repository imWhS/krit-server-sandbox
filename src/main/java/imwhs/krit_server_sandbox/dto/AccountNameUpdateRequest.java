package imwhs.krit_server_sandbox.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountNameUpdateRequest(
        @Size(min = 1, max = 40)
        @NotBlank
        String name
) {}
