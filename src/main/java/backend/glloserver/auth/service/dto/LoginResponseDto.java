package backend.glloserver.auth.service.dto;

public record LoginResponseDto(String sub, String email, String name) {
}
