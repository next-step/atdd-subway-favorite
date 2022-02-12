package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.session.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.authentication.token.TokenAuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private TokenAuthenticationConverter tokenAuthenticationConverter;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public AuthConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenAuthenticationConverter = new TokenAuthenticationConverter(OBJECT_MAPPER);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsService, tokenAuthenticationConverter, jwtTokenProvider, OBJECT_MAPPER)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
