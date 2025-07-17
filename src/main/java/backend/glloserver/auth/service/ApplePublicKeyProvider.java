package backend.glloserver.auth.service;

import backend.glloserver.auth.exception.AuthErrorCode;
import backend.glloserver.auth.service.dto.ios.ApplePublicKeyResponse;
import backend.glloserver.global.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class ApplePublicKeyProvider {

    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys"; //id_token의 유효성 검증을 위한 URL
    private final JwtTokenProvider jwtProvider;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final RestClient restClient;

    @Value("${apple.client-id}")
    private String appleClientId;

    @Value("${apple.issuer}")
    private String appleIssuer;

    public void verifyIdentifyToken(String idToken) {
        try {
            Claims claims = parseClaims(idToken);
            if (!appleIssuer.equals(claims.getIssuer())) {
                throw new CustomException(AuthErrorCode.INVALID_IDTOKEN_ISS);
            }
            if (!appleClientId.equals(claims.getAudience())) {
                throw new CustomException(AuthErrorCode.INVALID_IDTOKEN_AUDIENCE);
            }

        } catch (Exception e) {
            throw new CustomException(AuthErrorCode.INVALID_IDTOKEN_SIGNATURE);
        }
    }

    public Claims parseClaims(String idToken) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException {
        Map<String, String> headers = jwtProvider.parseHeaders(idToken);
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(headers, getAppleAuthPublicKey());
        return jwtProvider.parseRsaToken(idToken, publicKey);
    }

    //publicKey 가져온다
    private ApplePublicKeyResponse getAppleAuthPublicKey() {
        return restClient.get()
                .uri(APPLE_PUBLIC_KEYS_URL)
                .retrieve()
                .body(ApplePublicKeyResponse.class);
    }

    public String getSubFromIdToken(String idToken) {
        try {
            Claims claims = parseClaims(idToken);
            return claims.getSubject();
        } catch (Exception e) {
            throw new CustomException(AuthErrorCode.INVALID_IDTOKEN_SIGNATURE);
        }
    }

}
