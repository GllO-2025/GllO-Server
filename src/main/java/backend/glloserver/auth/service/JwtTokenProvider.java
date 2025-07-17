package backend.glloserver.auth.service;

import backend.glloserver.auth.exception.AuthErrorCode;
import backend.glloserver.auth.service.dto.AuthTokenDto;
import backend.glloserver.global.exception.CustomException;
import backend.glloserver.member.exception.MemberErrorCode;
import backend.glloserver.member.repository.MemberRepository;
import backend.glloserver.member.repository.entity.MemberEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.time.Clock;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    private final String accessSecretKey;
    private final String refreshSecretKey;
    private final Duration accessTokenExpired;
    private final Duration refreshTokenExpired;
    private final Clock clock;
    private final MemberRepository memberRepository;

    public JwtTokenProvider(@Value("${security.jwt.token.access-secret-key}") String accessSecretKey,
                            @Value("${security.jwt.token.refresh-secret-key}") String refreshSecretKey,
                            @Value("${security.jwt.token.access-token-expired}") Duration accessTokenExpired,
                            @Value("${security.jwt.token.refresh-token-expired}") Duration refreshTokenExpired,
                            Clock clock, MemberRepository memberRepository) {
        this.accessSecretKey = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
        this.refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
        this.accessTokenExpired = accessTokenExpired;
        this.refreshTokenExpired = refreshTokenExpired;
        this.clock = clock;
        this.memberRepository = memberRepository;
    }

    public AuthTokenDto createAuthToken(String payload) {
        return new AuthTokenDto(createToken(payload, accessSecretKey, accessTokenExpired),
                createToken(payload, refreshSecretKey, refreshTokenExpired));
    }

    private String createToken(String payload, String secretKey, Duration expired) {
        try {
            String token = Jwts.builder()
                    .setSubject(payload)
                    .setExpiration(calculateExpiredAt(expired))
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
            log.info("토큰 생성 성공: {}", token);
            return token;
        } catch (Exception e) {
            log.error("토큰 생성 실패: {}", e.getMessage(), e);
            throw e;
        }
    }


    private Date calculateExpiredAt(Duration expired) {
        Date now = Date.from(clock.instant());
        return new Date(now.getTime() + expired.toMillis());
    }

    public void validateAccessToken(String token) {
        getClaimsToken(token, accessSecretKey).getSubject();
    }

    public Long getMemberIdByAccessToken(String token) {
        String memberId = getClaimsToken(token, accessSecretKey).getSubject();
        return Long.valueOf(memberId);
    }

    public MemberEntity findMemberByAccessToken(String token) {
        Long memberId = getMemberIdByAccessToken(token);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.NOT_FOUND));
    }

    private Claims getClaimsToken(String token, String accessSecretKey) {
        try {
            return getClaims(token, accessSecretKey);
        } catch (ExpiredJwtException e) {
            throw new CustomException(AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    private Claims getClaims(String token, String key) {
        return Jwts.parser()
                .setSigningKey(key)
                .setClock(() -> Date.from(clock.instant()))
                .parseClaimsJws(token)
                .getBody();
    }

    //Apple id_token 검증
    public Claims parseRsaToken(String token, PublicKey publicKey){
        try{
            return Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token)
                    .getBody();
        }catch(ExpiredJwtException e){
            throw new CustomException(AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        }catch(JwtException | IllegalArgumentException e) {
            throw new CustomException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public Map<String,String> parseHeaders(String token) throws JsonProcessingException {
        String header=token.split("\\.")[0];
        return new ObjectMapper().readValue(decodeHeader(header),Map.class);
    }

    public String decodeHeader(String token) {
        return new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
    }


}
