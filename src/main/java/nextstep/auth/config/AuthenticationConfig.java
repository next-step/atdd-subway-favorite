package nextstep.auth.config;

import nextstep.auth.application.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {

    private final AuthService authService;

    public AuthenticationConfig(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(final List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationArgumentResolver(authService);
    }
}
