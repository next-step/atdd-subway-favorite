package nextstep.subway.config;

import nextstep.subway.auth.application.AuthenticationProvider;
import nextstep.subway.auth.application.SecurityContextPersistenceHandler;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.resolver.AuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserDetailsService userDetailsService;
    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityContextPersistenceHandler persistenceHandler;

    public WebMvcConfig(UserDetailsService userDetailsService, AuthenticationProvider authenticationProvider, JwtTokenProvider jwtTokenProvider, SecurityContextPersistenceHandler persistenceHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationProvider = authenticationProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.persistenceHandler = persistenceHandler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(authenticationProvider)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(authenticationProvider, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor(persistenceHandler));
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider, userDetailsService, persistenceHandler));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
