package nextstep.auth;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.ui.LoginMemberForFavoritePrincipalArgumentResolver;
import nextstep.auth.ui.LoginMemberPrincipalArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberPrincipalArgumentResolver(jwtTokenProvider));
        argumentResolvers.add(new LoginMemberForFavoritePrincipalArgumentResolver(jwtTokenProvider));
    }
}
