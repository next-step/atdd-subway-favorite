package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.authentication.session.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.token.TokenAuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.LoginCheckInterceptor;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConverter sessionAuthenticationConverter;
    private final AuthenticationConverter tokenAuthenticationConverter;
    private final ObjectMapper objectMapper;

    public AuthConfig(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider,
                      AuthenticationConverter sessionAuthenticationConverter,
                      AuthenticationConverter tokenAuthenticationConverter, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService,
                sessionAuthenticationConverter)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider,
                tokenAuthenticationConverter, objectMapper)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new LoginCheckInterceptor(jwtTokenProvider))
                .addPathPatterns("/members/me", "/favorites/**");
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider, objectMapper));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
