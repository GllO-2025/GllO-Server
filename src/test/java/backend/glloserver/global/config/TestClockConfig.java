package backend.glloserver.global.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@TestConfiguration
public class TestClockConfig {
    @Bean
    @Primary
    public Clock testClock() {
        return new TestClock(Instant.parse("2000-04-07T02:00:00Z"), ZoneOffset.UTC);
    }
}
