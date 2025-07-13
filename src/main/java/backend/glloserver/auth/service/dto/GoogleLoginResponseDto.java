package backend.glloserver.auth.service.dto;

public record GoogleLoginResponseDto(String sub, String email, String name) {
}
