package backend.glloserver.global.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@Import({TestClockConfig.class})
@TestConfiguration
public class TestConfig {

}
