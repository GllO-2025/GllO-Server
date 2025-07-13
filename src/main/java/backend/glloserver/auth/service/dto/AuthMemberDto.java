package backend.glloserver.auth.service.dto;

import backend.glloserver.member.repository.entity.MemberEntity;

public record AuthMemberDto(Long id, String nickname) {

    public AuthMemberDto(MemberEntity member) {
        this(member.getId(), member.getNickname());
    }
}