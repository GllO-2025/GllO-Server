package backend.glloserver.auth.service;

import backend.glloserver.auth.service.dto.*;
import backend.glloserver.auth.service.dto.ios.AppleLoginRequest;
import backend.glloserver.member.domain.AuthProvider;
import backend.glloserver.member.repository.MemberRepository;
import backend.glloserver.member.repository.entity.MemberEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthClient authClient;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplePublicKeyProvider applePublicKeyProvider;

    //    @WriterDatabase
    @Transactional
    public AuthInfoDto googleLogin(GoogleLoginRequest request) {
        AuthProvider provider = AuthProvider.GOOGLE;
        String loginId = authClient.getUserInfo(provider,request.accessToken());
        log.info("loginId: {}", loginId);
        MemberEntity member = memberRepository.findByLoginId(loginId)
                .orElseGet(() -> signup(provider, loginId));
        return login(member);
    }

    @Transactional
    public AuthInfoDto appleLogin(AppleLoginRequest request) {
        AuthProvider provider = AuthProvider.APPLE;
        applePublicKeyProvider.verifyIdentifyToken(request.tokenId());
        String loginId= authClient.getUserInfo(provider,request.tokenId());
        MemberEntity member=memberRepository.findByLoginId(loginId)
                .orElseGet(() -> signup(provider, loginId));
        return login(member);
    }

    private MemberEntity signup(AuthProvider provider, String loginId) {
        String password = passwordEncoder.encode(UUID.randomUUID().toString());
        MemberEntity member = createMember(provider, loginId, password);
        return memberRepository.save(member);
    }

    private MemberEntity createMember(AuthProvider provider, String loginId, String password) {
        return MemberEntity.builder()
                .provider(provider)
                .loginId(loginId)
                .password(password)
                .build();
    }

    private AuthInfoDto login(MemberEntity member) {
        AuthMemberDto authMember = new AuthMemberDto(member);
        AuthTokenDto authToken = jwtTokenProvider.createAuthToken(member.getId().toString());
        log.info("authToken: {}", authToken);
        return new AuthInfoDto(authMember, authToken);
    }

}
