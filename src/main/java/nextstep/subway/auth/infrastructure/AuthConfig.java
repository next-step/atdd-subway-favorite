package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.ui.*;
import nextstep.subway.auth.ui.session.SessionAuthenticationInterceptor2;
import nextstep.subway.auth.ui.session.SessionSecurityContextPersistenceInterceptor2;
import nextstep.subway.auth.ui.token.TokenAuthenticationInterceptor2;
import nextstep.subway.auth.ui.token.TokenSecurityContextPersistenceInterceptor2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthConfig(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor2(userDetailsService)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor2(userDetailsService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor2());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor2(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
