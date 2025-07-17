package backend.glloserver.auth.service.dto.ios;

import backend.glloserver.auth.exception.AuthErrorCode;
import backend.glloserver.global.exception.CustomException;

import java.util.List;

public record ApplePublicKeyResponse(List<ApplePublicKey> keys) {

    public ApplePublicKey getMatchedKey(String kid, String alg) throws CustomException {
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findAny()
                .orElseThrow(() -> new CustomException(AuthErrorCode.INVALILD_IDTOKEN));
    }
}
