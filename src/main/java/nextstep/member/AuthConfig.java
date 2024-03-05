package nextstep.member;

import java.util.List;
import nextstep.member.application.GithubClient;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.OAuth2Client;
import nextstep.member.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.properties.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private JwtTokenProvider jwtTokenProvider;
    private AppProperties appProperties;

    public AuthConfig(JwtTokenProvider jwtTokenProvider, AppProperties appProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.appProperties = appProperties;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider));
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
