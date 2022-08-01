package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.interceptors.BasicAuthenticationInterceptor;
import nextstep.auth.authentication.interceptors.BearerTokenAuthenticationInterceptor;
import nextstep.auth.authentication.interceptors.UsernamePasswordAuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.authentication.interceptors.TokenAuthenticationInterceptor;
import nextstep.auth.userdetails.UserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final UserDetailsService UserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public AuthConfig(UserDetailsService UserDetailsService, JwtTokenProvider jwtTokenProvider, final ObjectMapper objectMapper) {
        this.UserDetailsService = UserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationInterceptor(UserDetailsService)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(UserDetailsService, jwtTokenProvider, objectMapper)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationInterceptor(UserDetailsService));
        registry.addInterceptor(new BearerTokenAuthenticationInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
