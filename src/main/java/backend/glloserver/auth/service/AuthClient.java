package backend.glloserver.auth.service;


import backend.glloserver.member.domain.AuthProvider;

public interface AuthClient {
    String getUserInfo(AuthProvider provider,String accessToken);
}
