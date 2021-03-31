package nextstep.subway.auth.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.ui.AuthenticationInterceptor;
import nextstep.subway.auth.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.auth.ui.AuthenticatorStrategy;
import nextstep.subway.auth.ui.session.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.token.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private ObjectMapper objectMapper;
    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticatorStrategy authenticatorStrategy;

    public AuthConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper, AuthenticatorStrategy authenticatorStrategy) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.authenticatorStrategy = authenticatorStrategy;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(objectMapper, userDetailsService, authenticatorStrategy)).addPathPatterns("/login");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
