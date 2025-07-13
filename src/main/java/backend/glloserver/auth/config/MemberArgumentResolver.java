package backend.glloserver.auth.config;

import backend.glloserver.auth.service.JwtTokenProvider;
import backend.glloserver.member.repository.entity.MemberEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@AllArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtHeaderExtractor jwtHeaderExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberEntity.class);
    }

    @Override
    public MemberEntity resolveArgument(MethodParameter parameter,
                                        ModelAndViewContainer mavContainer,
                                        NativeWebRequest webRequest,
                                        WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = jwtHeaderExtractor.extractAccessTokenFromHeader(request);
        return jwtTokenProvider.findMemberByAccessToken(token);
    }
}
