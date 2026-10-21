package imwhs.krit_server_sandbox.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    ACCOUNT_UNAUTHORIZED(HttpStatus.FORBIDDEN),
    ACCOUNT_DUPLICATE_EMAIL(HttpStatus.CONFLICT),
    ACCOUNT_DUPLICATE_HANDLE(HttpStatus.CONFLICT),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus httpStatus;

}
