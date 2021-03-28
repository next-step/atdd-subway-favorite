package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.auth.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.auth.ui.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.converter.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.converter.TokenAuthenticationConverter;
import nextstep.subway.auth.ui.persistence.SessionSecurityContextInterceptor;
import nextstep.subway.auth.ui.persistence.TokenSecurityContextInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private UserDetailService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private SessionAuthenticationConverter sessionAuthenticationConverter;
    private TokenAuthenticationConverter tokenAuthenticationConverter;

    public AuthConfig(UserDetailService userDetailsService, JwtTokenProvider jwtTokenProvider, SessionAuthenticationConverter sessionAuthenticationConverter, TokenAuthenticationConverter tokenAuthenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(sessionAuthenticationConverter, userDetailsService)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(tokenAuthenticationConverter, userDetailsService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextInterceptor());
        registry.addInterceptor(new TokenSecurityContextInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
