package imwhs.krit_server_sandbox.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Spring MVC 및 Service, Repository 계층에서 발생한 예외를 공통으로 처리하기 위한 핸들러 메서드를 정의합니다.
 * 핸들러 메서드는 예외 발생 원인 및 메시지 등을 이용해 상태 코드와 함께 HTTP 응답 객체를 반환합니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException e) {
        return new ErrorResponse("RESOURCE_NOT_FOUND", e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponse("INVALID_ARGUMENT", e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIllegalStateException(IllegalStateException e) {
        return new ErrorResponse("INVALID_STATE", e.getMessage(), LocalDateTime.now());
    }

    /**
     * 요청 데이터에 대한 객체 및 필드 단위의 Bean Validation(검증)을 실패해서 발생한 예외를 처리합니다.
     * 실패한 검증 수가 2개 이상이면 가장 먼저 검증에 실패한 객체 또는 필드에 정의된 메시지를 사용해 공통 HTTP 응답 객체를 생성합니다.
     * 추출 가능한 메시지가 없거나 null이면 기본 오류 메시지를 사용합니다.
     * @param e 검증 실패 정보가 포함된 예외 객체
     * @return 공통 HTTP 응답 객체. HTTP 응답 본문(Body)로 사용합니다.
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

        return new ErrorResponse("VALIDATION_FAILED", message, LocalDateTime.now());
    }

}
