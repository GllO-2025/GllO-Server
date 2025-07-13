package backend.glloserver.member.domain;

public enum AuthProvider {

    GOOGLE, APPLE;

    public String buildLoginId(String loginId) {
        return this.name() + loginId;
    }
}
