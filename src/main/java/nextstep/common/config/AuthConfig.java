package nextstep.common.config;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.presentation.AuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider));
    }
}
