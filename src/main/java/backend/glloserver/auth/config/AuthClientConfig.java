package backend.glloserver.auth.config;

import backend.glloserver.auth.service.ApplePublicKeyProvider;
import backend.glloserver.auth.service.AuthClient;
import backend.glloserver.auth.service.client.DevAuthClient;
import backend.glloserver.auth.service.client.ProdAuthClient;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class AuthClientConfig {

    @Bean
    @Profile("prod")
    public AuthClient prodAuthClient(ApplePublicKeyProvider applePublicKeyProvider){
        return new ProdAuthClient(createRestClient(), applePublicKeyProvider);
    }

    @Bean
    @Profile("default")
    public AuthClient devAuthClient(ApplePublicKeyProvider applePublicKeyProvider){
        log.warn("테스트 인증 환경 설정.");
        return new DevAuthClient(createRestClient(),applePublicKeyProvider);
    }

    @Bean
    public RestClient createRestClient() {
        return RestClient.builder()
                .build();
    }
}
