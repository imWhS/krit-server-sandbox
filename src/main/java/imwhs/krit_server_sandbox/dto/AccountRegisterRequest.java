package imwhs.krit_server_sandbox.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AccountRegisterRequest {

    @NotBlank(message = "핸들은 필수 항목이에요.")
    @Pattern(
            regexp = "^[a-z0-9_.]{6,20}$",
            message = "핸들은 최소 6자부터 최대 20자 이하 길이의 영어 알파벳 소문자, 숫자, 밑줄(_), 마침표(.)만 사용해서 설정할 수 있어요.")
    private String handle;

    @Email(message = "올바른 이메일 주소 형식이 아니에요.")
    @NotBlank(message = "이메일 주소는 필수 항목이에요.")
    private String email;

    @NotBlank(message = "암호는 필수 항목이에요.")
    @Size(min = 8, message = "암호는 최소 8자 이상의 길이로 설정되어야 해요.")
    private String password;

}
