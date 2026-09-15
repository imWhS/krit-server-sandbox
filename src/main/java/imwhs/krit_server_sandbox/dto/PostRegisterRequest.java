package imwhs.krit_server_sandbox.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRegisterRequest(
        @NotNull
        Long authorId,
        @Size(max = 100)
        String title,
        @NotBlank
        String content
) { }
