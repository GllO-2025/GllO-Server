package backend.glloserver.auth.service.dto;

public record LoginResponse (Long id, String nickname) {

    public LoginResponse(AuthMemberDto authMember) {
        this(authMember.id(), authMember.nickname());
    }
}
