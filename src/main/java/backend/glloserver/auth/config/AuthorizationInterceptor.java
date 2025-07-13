package backend.glloserver.auth.config;

import backend.glloserver.auth.service.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtHeaderExtractor jwtHeaderExtractor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String token = jwtHeaderExtractor.extractAccessTokenFromHeader(request);
        jwtTokenProvider.validateAccessToken(token);

        return true;
    }
}
