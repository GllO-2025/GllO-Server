package backend.glloserver.global.code.exception;

import backend.glloserver.global.ApiResponse;
import backend.glloserver.global.status.ErrorStatus;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorStatus errorStatus;


    public CustomException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public static <T>ResponseEntity<ApiResponse<T>> createErrorResponse(ErrorStatus errorStatus,T data) {
        return ResponseEntity
                .status(errorStatus.getHttpStatus()) //HTTP 상태 코드
                .body(ApiResponse.onFailure(errorStatus,data)); //실패 응답 JSON
    }
}
