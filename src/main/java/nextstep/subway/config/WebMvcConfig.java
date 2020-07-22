package nextstep.subway.config;

import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.interceptor.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authentication.interceptor.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authentication.converter.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.authentication.converter.TokenAuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.resolver.AuthenticationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private SessionAuthenticationConverter sessionAuthenticationConverter;
    private TokenAuthenticationConverter tokenAuthenticationConverter;

    public WebMvcConfig(CustomUserDetailsService userDetailsService,
                        JwtTokenProvider jwtTokenProvider,
                        SessionAuthenticationConverter sessionAuthenticationConverter,
                        TokenAuthenticationConverter tokenAuthenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService, sessionAuthenticationConverter)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(jwtTokenProvider, userDetailsService, tokenAuthenticationConverter)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationArgumentResolver());
    }
}
