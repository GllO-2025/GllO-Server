package backend.glloserver.global.code.exception;

import backend.glloserver.global.ApiResponse;
import backend.glloserver.global.status.ErrorStatus;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonException {


    //유효성 검사 @Valid @RequestBody 검증 실패 //단일 파라미터 검증 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String,String> errors=e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation->violation.getPropertyPath().toString(),
                        violation->violation.getMessage()
                        ));

        log.error("Validation Error:{}",errors);
        return CustomException.createErrorResponse(ErrorStatus.COMMON_BAD_REQUEST,errors);
    }


    // 유효성 검사 @Validated 검증 실패 //DTO 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage()
                ));
        log.error("Validation Error: {}", errors);
        return CustomException.createErrorResponse(ErrorStatus.COMMON_BAD_REQUEST, errors);
    }


    //파싱 실패시 발생하는 예외 //잘못된 포맷인 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) cause;
            if (invalidFormatException.getTargetType().isEnum()) {
                return CustomException.createErrorResponse(ErrorStatus.USER_INVALID_PROVIDER, null);
            }
        }

        return CustomException.createErrorResponse(ErrorStatus.COMMON_BAD_REQUEST, null);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {

        log.error("Custom Error: {} - Code: {} - Status: {}",
                ex.getMessage(),
                ex.getClass().getSimpleName(),
                ex.getErrorStatus().getHttpStatus());

        return CustomException.createErrorResponse(ex.getErrorStatus(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleUnexpectedException(Exception ex) {
        log.error("Unexpected Error: ", ex);
        return CustomException.createErrorResponse(ErrorStatus.COMMON_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
