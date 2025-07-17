package backend.glloserver.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginFailResponseDto(String msg, Long code) {
}
