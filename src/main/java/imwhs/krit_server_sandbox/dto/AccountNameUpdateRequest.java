package imwhs.krit_server_sandbox.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountNameUpdateRequest(
        @Size(min = 1, max = 40, message = "이름은 최소 1자 이상, 최대 40자 이하의 길이여야 해요.")
        @NotBlank(message = "이름은 필수 항목이에요.")
        String name
) {}
