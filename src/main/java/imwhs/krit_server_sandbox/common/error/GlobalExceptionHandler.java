package imwhs.krit_server_sandbox.common.error;

import imwhs.krit_server_sandbox.exception.BaseException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        e.getErrorType().name(),
                        e.getMessage(),
                        LocalDateTime.now()),
                e.getErrorType().getHttpStatus());
    }

    @ExceptionHandler(value = ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleObjectOptimisticLockingFailureException(
            ObjectOptimisticLockingFailureException e) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        ErrorType.OPTIMISTIC_LOCK_CONFLICT.name(),
                        "요청을 처리하던 중에 다른 요청에 의해 정보가 수정됐어요. 수정된 정보를 확인하신 후 다시 시도해주세요.",
                        LocalDateTime.now()),
                ErrorType.OPTIMISTIC_LOCK_CONFLICT.getHttpStatus()
        );
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Throwable cause = e;

        // 데이터 무결성 예외가 발생한 원인이 된 예외를 찾습니다.
        while (cause != null) {
            // 데이터베이스에서 발생한 위배된 제약 조건에 대한 정보를 찾습니다.
            if (cause instanceof ConstraintViolationException violationException &&
                violationException.getKind() == ConstraintViolationException.ConstraintKind.UNIQUE) {
                String constraintName = violationException.getConstraintName();

                // 위배된 제약 조건 이름을 이용해 적절한 오류 메시지를 생성합니다.
                if (constraintName != null) {
                    // 제약 조건 이름이 PUBLIC.UK_ACCOUNT_HANDLE INDEX PUBLIC.UK_ACCOUNT_HANDLE_INDEX_E"라면,
                    // H2가 덧붙인 "INDEX PUBLIC.UK_ACCOUNT_HANDLE_INDEX_E"와 맨 앞의 "PUBLIC."를 제거해
                    // "UK_ACCOUNT_HANDLE"만 남깁니다.
                    String name = constraintName.split("\\s+INDEX\\s+", 2)[0];
                    name = name.substring(name.lastIndexOf('.') + 1);

                    if (name.equals("UK_ACCOUNT_HANDLE")) {
                        return new ResponseEntity<>(
                                new ErrorResponse(
                                        ErrorType.ACCOUNT_DUPLICATE_HANDLE.name(),
                                        "이미 해당 공개 식별자(핸들)을 사용하고 있는 계정이 있어요.",
                                        LocalDateTime.now()
                                ),
                                ErrorType.ACCOUNT_DUPLICATE_HANDLE.getHttpStatus()
                        );
                    } else if (name.equals("UK_ACCOUNT_EMAIL")) {
                        return new ResponseEntity<>(
                                new ErrorResponse(
                                        ErrorType.ACCOUNT_DUPLICATE_EMAIL.name(),
                                        "이미 해당 이메일 주소를 사용하고 있는 계정이 있어요.",
                                        LocalDateTime.now()
                                ),
                                ErrorType.ACCOUNT_DUPLICATE_EMAIL.getHttpStatus()
                        );
                    }
                }

                break;
            }

            cause = cause.getCause();
        }

        return new ResponseEntity<>(
                new ErrorResponse(
                        "DATA_INTEGRITY_VIOLATION",
                        "정보를 저장하는 과정 중 오류가 발생했어요.",
                        LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
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
