package nextstep.auth;

import nextstep.auth.application.AuthManager;
import nextstep.auth.ui.AuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "nextstep.auth")
public class AuthConfig implements WebMvcConfigurer {
    private final AuthManager authManager;

    public AuthConfig(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(authManager));
    }
}
