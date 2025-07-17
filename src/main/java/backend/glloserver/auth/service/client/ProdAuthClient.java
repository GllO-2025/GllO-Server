package backend.glloserver.auth.service.client;


import backend.glloserver.auth.exception.GoogleLoginExceptionHandler;
import backend.glloserver.auth.service.ApplePublicKeyProvider;
import backend.glloserver.auth.service.dto.LoginResponseDto;
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
    private final ApplePublicKeyProvider applePublicKeyProvider;

    @Override
    public String getUserInfo(AuthProvider provider, String token) {
        return switch (provider) {
            case GOOGLE -> getGoogleUserInfo(token);
            case APPLE -> getAppleUserInfo(token); //Apple은 idToken 사용
            default -> throw new IllegalArgumentException("지원하지 않는 로그인 타입입니다.");
        };
    }

    protected String getGoogleUserInfo(String accessToken) {
        LoginResponseDto responseDto = restClient.get()
                .uri(GET_GOOGLE_USER_INFO_URI)
                .header(HttpHeaders.AUTHORIZATION, createAuthorization(accessToken))
                .retrieve()
                .onStatus(new GoogleLoginExceptionHandler())
                .body(LoginResponseDto.class);

        return AuthProvider.GOOGLE.buildLoginId(responseDto.sub()); // Google의 'sub'은 고유 ID
    }

    protected String getAppleUserInfo(String idToken) {
        String sub=applePublicKeyProvider.getSubFromIdToken(idToken);
        return AuthProvider.APPLE.buildLoginId(sub); // Apple의 'sub'은 고유 ID
    }


    private String createAuthorization(String accessToken) {
        return BEARER_HEADER_FORMAT.formatted(accessToken);
    }

}
