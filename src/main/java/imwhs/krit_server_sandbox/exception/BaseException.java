package imwhs.krit_server_sandbox.exception;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorType errorType;

    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

}
