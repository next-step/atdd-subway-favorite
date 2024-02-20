package nextstep.subway;

import nextstep.subway.auth.AuthConfig;
import nextstep.subway.auth.client.github.config.GithubClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AuthConfig.class)
@EnableConfigurationProperties(GithubClientProperties.class)
public class SubwayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubwayApplication.class, args);
    }

}
