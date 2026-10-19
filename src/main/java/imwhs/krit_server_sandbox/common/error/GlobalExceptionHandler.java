package imwhs.krit_server_sandbox.common.error;

import imwhs.krit_server_sandbox.exception.account.AccountDuplicateEmailException;
import imwhs.krit_server_sandbox.exception.account.AccountDuplicateHandleException;
import imwhs.krit_server_sandbox.exception.account.AccountNotFoundException;
import imwhs.krit_server_sandbox.exception.post.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Spring MVC 및 Service, Repository 등 도메인 계층에서 발생한 예외를 공통으로 처리 후 HTTP 응답하기 위한 핸들러 메서드를 정의합니다.
 * 핸들러 메서드는 예외 발생 원인 및 메시지 등을 이용해 상태 코드와 함께 HTTP 응답 객체를 반환합니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AccountDuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleAccountDuplicateEmailException(AccountDuplicateEmailException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getErrorCode(), e.getMessage(), LocalDateTime.now()),
                e.getHttpStatus());
    }

    @ExceptionHandler(value = AccountDuplicateHandleException.class)
    public ResponseEntity<ErrorResponse> handleAccountDuplicateHandleException(AccountDuplicateHandleException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getErrorCode(), e.getMessage(), LocalDateTime.now()),
                e.getHttpStatus());
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getErrorCode(), e.getMessage(), LocalDateTime.now()),
                e.getHttpStatus());
    }

    @ExceptionHandler(value = PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getErrorCode(), e.getMessage(), LocalDateTime.now()),
                e.getHttpStatus());
    }

    /**
     * 메서드에 전달된 파라미터가 유효하지 않아서 발생한 예외를 처리합니다.
     * TODO: 현재는 클라이언트로부터 잘못된 HTTP 요청 데이터를 메서드의 파라미터로 전달받았을 때에만 명시적으로 IllegalArgumentException을
     *       throw 하고 있어서 상태 코드를 400 BAD REQUEST로 설정합니다. 서버 내부 문제로 인해 잘못된 값을 파라미터로 전달받았을 때에도
     *       throw 한다면, 500 INTERNAL SERVER ERROR와 같이 상황에 따라 다른 상태 코드를 설정할 수 있도록 예외 처리 구조를 변경해야 합니다.
     * @param e 예외 객체
     * @return 공통 HTTP 응답 객체. HTTP 응답 본문(Body)으로 사용합니다.
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponse("ILLEGAL_ARGUMENT", e.getMessage(), LocalDateTime.now());
    }

    /**
     * 클라이언트의 HTTP 요청 데이터에 대한 객체 및 필드 단위의 Bean Validation(검증)을 실패해서 발생한 예외를 처리합니다.
     * 실패한 검증 수가 2개 이상이면 검증 실패 메시지 중 하나를 사용해 공통 HTTP 응답 객체를 생성합니다.
     * 추출 가능한 메시지가 없거나 null이면 기본 오류 메시지를 사용합니다.
     * @param e 검증 실패 정보가 포함된 예외 객체
     * @return 공통 HTTP 응답 객체. HTTP 응답 본문(Body)으로 사용합니다.
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("요청으로 주신 데이터가 올바르지 않아요.");

        return new ErrorResponse("REQUEST_DATA_VALIDATION_FAILED", message, LocalDateTime.now());
    }

}
