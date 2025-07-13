package backend.glloserver.auth.service;


public interface AuthClient {
    String getUserInfo(String accessToken);
}
