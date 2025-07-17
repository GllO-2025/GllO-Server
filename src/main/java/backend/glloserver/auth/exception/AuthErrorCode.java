package backend.glloserver.auth.exception;

import backend.glloserver.global.exception.ErrorMessage;
import backend.glloserver.global.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorResponse {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "만료된 토큰입니다."),
    INVALID_PASSWORD(HttpStatus.NOT_FOUND, "가입하지 않은 회원입니다."),
    DUPLICATED_MEMBER(HttpStatus.CONFLICT, "이미 가입한 회원입니다."),
    GOOGLE_LOGIN_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "구글 로그인에 실패했습니다."),
    APPLE_LOGIN_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "애플 로그인에 실패했습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALILD_IDTOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 id token 입니다."),
    INVALID_IDTOKEN_ISS(HttpStatus.UNAUTHORIZED, "TOKEN ID의 iss가 유효하지 않습니다."),
    INVALID_IDTOKEN_AUDIENCE(HttpStatus.UNAUTHORIZED, "TOKEN ID의 aud가 유효하지 않습니다."),
    INVALID_IDTOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "TOKEN ID의 signature가 유효하지 않습니다."),
    IDTOKEN_PARSING_ERROR(HttpStatus.BAD_REQUEST,"TOKEN ID 파싱에 실패했습니다."),
    REFRESH_REUSE_EXCEPTION(HttpStatus.UNAUTHORIZED, "이미 사용한 토큰입니다. 다시 로그인 해주세요.");

    private final HttpStatus status;
    private final String message;

    @Override
    public ErrorMessage getErrorMessage() {
        return new ErrorMessage(this.message);
    }
}