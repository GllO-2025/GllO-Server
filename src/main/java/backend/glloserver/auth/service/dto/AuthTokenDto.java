package backend.glloserver.auth.service.dto;

public record AuthTokenDto(String accessToken, String refreshToken) {
}
