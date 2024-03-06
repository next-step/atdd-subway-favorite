package nextstep.auth.config;

import nextstep.auth.application.GithubClient;
import nextstep.member.application.OAuth2Client;
import nextstep.properties.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private AppProperties appProperties;

    public AppConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean("githubClient")
    public OAuth2Client githubClient() {
        return new GithubClient(appProperties.getGithub().getClientId(),
            appProperties.getGithub().getClientSecret(),
            appProperties.getGithub().getAccessTokenUri(),
            appProperties.getGithub().getUserInfoUri()
        );
    }
}
