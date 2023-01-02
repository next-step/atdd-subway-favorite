package nextstep.member;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.ui.AuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    public AuthConfig(JwtTokenProvider jwtTokenProvider, AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(authenticationPrincipalArgumentResolver);
    }
}
