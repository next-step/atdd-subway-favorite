package nextstep.member;

import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.security.application.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final JwtTokenProvider<LoginMember> jwtTokenProvider;

    public AuthConfig(final JwtTokenProvider<LoginMember> jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider));
    }
}
