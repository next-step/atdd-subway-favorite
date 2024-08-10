package nextstep.auth;

import java.util.List;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.ui.AuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;

    public AuthConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider));
    }
}
