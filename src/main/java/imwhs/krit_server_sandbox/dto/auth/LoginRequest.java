package imwhs.krit_server_sandbox.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Email(message = "이메일 주소가 올바른 형식인지 확인해주세요.")
        @NotBlank(message = "이메일 주소를 입력해주세요.")
        String email,

        @Size(min = 8, message = "암호는 최소 8자 이상의 길이예요.")
        @NotBlank(message = "암호를 입력해주세요.")
        String password) {

}
