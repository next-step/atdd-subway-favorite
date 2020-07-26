package nextstep.subway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.interceptor.convert.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.convert.TokenAuthenticationConverter;
import nextstep.subway.member.application.CustomUserDetailsService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private final SessionAuthenticationConverter sessionAuthenticationConverter;
    private final TokenAuthenticationConverter tokenAuthenticationConverter;

    public WebMvcConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider,
        SessionAuthenticationConverter sessionAuthenticationConverter,
        TokenAuthenticationConverter tokenAuthenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
            new SessionAuthenticationInterceptor(userDetailsService, sessionAuthenticationConverter))
            .addPathPatterns("/login/session");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(
            new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, tokenAuthenticationConverter))
            .addPathPatterns("/login/token");
    }
}
