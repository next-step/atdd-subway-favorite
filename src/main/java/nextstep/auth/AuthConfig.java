package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.authentication.interceptor.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.interceptor.SessionSecurityContextInterceptor;
import nextstep.auth.authorization.interceptor.TokenSecurityContextInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper;

    public AuthConfig(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService, new SessionAuthenticationConverter())).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsService, new TokenAuthenticationConverter(objectMapper), jwtTokenProvider, objectMapper)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextInterceptor());
        registry.addInterceptor(new TokenSecurityContextInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
