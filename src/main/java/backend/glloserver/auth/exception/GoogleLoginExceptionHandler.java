package backend.glloserver.auth.exception;

import backend.glloserver.auth.service.dto.GoogleLoginFailResponseDto;
import backend.glloserver.auth.service.dto.LoginFailResponseDto;
import backend.glloserver.global.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class GoogleLoginExceptionHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new CustomException(getGoogleLoginErrorCode(response));
    }

    private AuthErrorCode getGoogleLoginErrorCode(final ClientHttpResponse response) throws IOException {
        LoginFailResponseDto googleLoginFailResponse = objectMapper.readValue(
                response.getBody(), LoginFailResponseDto.class);
        log.error(googleLoginFailResponse.toString());
        return AuthErrorCode.GOOGLE_LOGIN_INTERNAL_SERVER_ERROR;
    }
}
