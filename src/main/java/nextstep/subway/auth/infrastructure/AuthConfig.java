package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.auth.ui.*;
import nextstep.subway.auth.ui.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.convert.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.convert.TokenAuthenticationConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final UserDetailService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SessionAuthenticationConverter sessionAuthenticationConverter;
    private final TokenAuthenticationConverter tokenAuthenticationConverter;

    public AuthConfig(UserDetailService userDetailsService, JwtTokenProvider jwtTokenProvider,
                      SessionAuthenticationConverter sessionAuthenticationConverter, TokenAuthenticationConverter tokenAuthenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(sessionAuthenticationConverter, userDetailsService)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(tokenAuthenticationConverter, userDetailsService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
