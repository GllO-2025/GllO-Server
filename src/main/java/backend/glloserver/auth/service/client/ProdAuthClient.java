package backend.glloserver.auth.service.client;

import backend.glloserver.auth.exception.GoogleLoginExceptionHandler;
import backend.glloserver.auth.service.dto.GoogleLoginResponseDto;
import backend.glloserver.auth.service.AuthClient;
import backend.glloserver.member.domain.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class ProdAuthClient implements AuthClient {

    private static final String BEARER_HEADER_FORMAT = "Bearer %s";
    private static final String GET_GOOGLE_USER_INFO_URI = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final RestClient restClient;

    @Override
    public String getUserInfo(String accessToken) {
        return getGoogleUserInfo(accessToken);
    }

    protected String getGoogleUserInfo(String accessToken) {
        GoogleLoginResponseDto responseDto = restClient.get()
                .uri(GET_GOOGLE_USER_INFO_URI)
                .header(HttpHeaders.AUTHORIZATION, createAuthorization(accessToken))
                .retrieve()
                .onStatus(new GoogleLoginExceptionHandler())
                .body(GoogleLoginResponseDto.class);

        return AuthProvider.GOOGLE.buildLoginId(responseDto.sub()); // Google의 'sub'은 고유 ID
    }

    private String createAuthorization(String accessToken) {
        return BEARER_HEADER_FORMAT.formatted(accessToken);
    }
}
