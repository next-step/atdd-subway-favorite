package nextstep.auth.config;

import nextstep.auth.application.GithubClient;
import nextstep.auth.application.OAuth2Client;
import nextstep.auth.domain.UserCreator;
import nextstep.auth.domain.UserGetter;
import nextstep.auth.infra.UserCreatorImpl;
import nextstep.auth.infra.UserGetterImpl;
import nextstep.properties.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private final AppProperties appProperties;

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

    @Bean
    public UserCreator userCreator() {
        return new UserCreatorImpl(appProperties.getUserService().getCreateUrl(), restTemplate());
    }

    @Bean
    public UserGetter userGetter() {
        return new UserGetterImpl(appProperties.getUserService().getMeUrl(), restTemplate());
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
