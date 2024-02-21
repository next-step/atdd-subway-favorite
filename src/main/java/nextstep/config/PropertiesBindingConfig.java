package nextstep.config;

import nextstep.auth.application.dto.GithubClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({GithubClientProperties.class})
public class PropertiesBindingConfig {
}
