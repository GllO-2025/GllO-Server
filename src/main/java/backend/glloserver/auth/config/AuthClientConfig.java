package backend.glloserver.auth.config;

import backend.glloserver.auth.service.AuthClient;
import backend.glloserver.auth.service.client.DevAuthClient;
import backend.glloserver.auth.service.client.ProdAuthClient;
import lombok.extern.slf4j.Slf4j;


import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class AuthClientConfig {

    @Bean
    @Profile("prod")
    public AuthClient prodAuthClient(){
        return new ProdAuthClient(createRestClient());
    }

    @Bean
    @Profile("default")
    public AuthClient devAuthClient(){
        log.warn("테스트 인증 환경 설정.");
        return new DevAuthClient(createRestClient());
    }

    private RestClient createRestClient() {
        return RestClient.builder()
                .build();
    }
}
