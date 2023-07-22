package nextstep.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nextstep.api.auth.aop.principal.AuthenticationPrincipalArgumentResolver;
import nextstep.api.auth.support.JwtTokenProvider;

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
