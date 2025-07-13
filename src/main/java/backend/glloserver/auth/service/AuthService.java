package backend.glloserver.auth.service;

import backend.glloserver.auth.service.dto.AuthInfoDto;
import backend.glloserver.auth.service.dto.AuthMemberDto;
import backend.glloserver.auth.service.dto.AuthTokenDto;
import backend.glloserver.auth.service.dto.GoogleLoginRequest;
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

    //    @WriterDatabase
    @Transactional
    public AuthInfoDto googleLogin(GoogleLoginRequest request) {
        String loginId = authClient.getUserInfo(request.accessToken());
        log.info("loginId: {}", loginId);
        AuthProvider provider = AuthProvider.GOOGLE;
        MemberEntity member = memberRepository.findByLoginId(loginId)
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
