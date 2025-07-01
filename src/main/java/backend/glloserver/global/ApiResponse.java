package backend.glloserver.global;

import backend.glloserver.global.code.BaseCode;
import backend.glloserver.global.code.BaseErrorCode;
import backend.glloserver.global.status.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess","code","message","result"})
public class ApiResponse<T> {

    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true,SuccessStatus._OK.getCode(),SuccessStatus._OK.getMessage(),result);
    }

    public static <T> ApiResponse<T> onSuccess(BaseCode code,T result) {
        return new ApiResponse<>(true,code.getReasonHttpStatus().getCode(),code.getReasonHttpStatus().getMessage(),result);
    }

    public static <T> ApiResponse<T> onFailure(BaseErrorCode code, T result) {
        return new ApiResponse<>(false,code.getReasonHttpStatus().getCode(),code.getReasonHttpStatus().getMessage(),result);
    }
}
