package imwhs.krit_server_sandbox.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AccountRegisterRequest {

    @NotBlank(message = "핸들은 필수 항목이에요.")
    private String handle;

    @Email(message = "올바른 이메일 주소 형식이 아니에요.")
    @NotBlank(message = "이메일 주소는 필수 항목이에요.")
    private String email;

    @NotBlank(message = "암호는 필수 항목이에요.")
    private String password;

}
