package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.interceptor.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Import({AuthenticationConverterConfig.class})
@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private static final String SESSION_LOGIN_URL = "/login/session";
    private static final String TOKEN_LOGIN_URL = "/login/token";
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationConverter tokenAuthenticationConverter;
    private final AuthenticationConverter sessionAuthenticationConverter;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public AuthConfig(
            final CustomUserDetailsService userDetailsService,
            final AuthenticationConverter tokenAuthenticationConverter,
            final AuthenticationConverter sessionAuthenticationConverter,
            final JwtTokenProvider jwtTokenProvider,
            final ObjectMapper objectMapper
    ) {
        this.userDetailsService = userDetailsService;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                new SessionAuthenticationInterceptor(userDetailsService, sessionAuthenticationConverter)
        ).addPathPatterns(SESSION_LOGIN_URL);
        registry.addInterceptor(
                new TokenAuthenticationInterceptor(userDetailsService, tokenAuthenticationConverter, jwtTokenProvider, objectMapper)
        ).addPathPatterns(TOKEN_LOGIN_URL);
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
