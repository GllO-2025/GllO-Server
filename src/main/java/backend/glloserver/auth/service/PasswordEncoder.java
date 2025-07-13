package backend.glloserver.auth.service;

public interface PasswordEncoder {
    String encode(String rawPassword);
}
