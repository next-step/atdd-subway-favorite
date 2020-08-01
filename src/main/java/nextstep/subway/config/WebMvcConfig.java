package nextstep.subway.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nextstep.subway.auth.application.AuthenticationResolver;
import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.interceptor.convert.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.convert.TokenAuthenticationConverter;
import nextstep.subway.member.application.CustomUserDetailsService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private UserDetailService userDetailService;
    private JwtTokenProvider jwtTokenProvider;
    private final SessionAuthenticationConverter sessionAuthenticationConverter;
    private final TokenAuthenticationConverter tokenAuthenticationConverter;

    public WebMvcConfig(CustomUserDetailsService userDetailService, JwtTokenProvider jwtTokenProvider,
        SessionAuthenticationConverter sessionAuthenticationConverter,
        TokenAuthenticationConverter tokenAuthenticationConverter) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
            new SessionAuthenticationInterceptor(userDetailService, sessionAuthenticationConverter))
            .addPathPatterns("/login/session");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(
            new TokenAuthenticationInterceptor(userDetailService, jwtTokenProvider, tokenAuthenticationConverter))
            .addPathPatterns("/login/token");
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationResolver());
    }
}
