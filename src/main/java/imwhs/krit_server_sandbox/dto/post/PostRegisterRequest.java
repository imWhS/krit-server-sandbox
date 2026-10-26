package imwhs.krit_server_sandbox.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PostRegisterRequest(

        @NotNull(message = "작성자의 ID는 필수 항목이에요.")
        Long authorId,

        @Size(max = 100, message = "제목은 최대 100자 이하 길이의 문자열만 사용할 수 있어요.")
        String title,

        @NotBlank(message = "본문은 필수 항목이에요.")
        String content,

        Set<String> tagNames

) { }
