package backend.glloserver.member.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NicknameRequest(@NotBlank(message = "닉네임을 입력해주세요.")
                              @Size(max = 10, message = "닉네임의 최대 길이는 10자 미만입니다.")
                              String nickname) {
}
