package imwhs.krit_server_sandbox.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRegisterRequest(
        @NotNull(message = "작성자의 ID는 필수 항목이에요.")
        Long authorId,

        @NotNull(message = "게시물의 ID는 필수 항목이에요.")
        Long postId,

        @Size(max = 200, message = "본문은 최대 200자 이하의 길이여야 해요.")
        @NotBlank(message = "본문은 필수 항목이에요.")
        String body
) { }
