package imwhs.krit_server_sandbox.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PostUpdateRequest(

        @Size(max = 100, message = "제목은 최대 100자 이하의 길이여야 해요.")
        String title,

        @NotBlank(message = "본문은 필수 항목이에요.")
        String content,

        Set<String> tagNames

) { }
