package nextstep.member;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.presentation.AuthenticationPrincipalArgumentResolver;
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
