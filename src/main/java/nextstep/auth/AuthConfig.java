package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.interceptor.BasicAuthenticationFilter;
import nextstep.auth.interceptor.BearerTokenAuthenticationFilter;
import nextstep.auth.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.interceptor.UsernamePasswordAuthenticationFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.user.UserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(userDetailsService))
            .addPathPatterns("/login/form");
        registry.addInterceptor(
                new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, objectMapper))
            .addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(userDetailsService));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider, userDetailsService));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
