package nextstep;

import nextstep.member.application.GithubConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({GithubConfigProperties.class})
@SpringBootApplication
public class SubwayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubwayApplication.class, args);
    }

}
