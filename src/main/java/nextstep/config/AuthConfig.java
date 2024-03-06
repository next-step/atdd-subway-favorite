package nextstep.config;

import java.util.List;
import nextstep.member.application.UserAuthenticator;
import nextstep.member.infra.UserAuthenticatorImpl;
import nextstep.properties.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final AppProperties appProperties;

    public AuthConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(userAuthenticator()));
    }

    @Bean
    public UserAuthenticator userAuthenticator() {
        return new UserAuthenticatorImpl(appProperties.getAuthService().getTokenUrl());
    }
}
