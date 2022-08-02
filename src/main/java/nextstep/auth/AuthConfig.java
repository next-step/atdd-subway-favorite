package nextstep.auth;

import java.util.List;
import nextstep.auth.authentication.interceptor.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authentication.interceptor.BasicAuthenticationFilter;
import nextstep.auth.authentication.interceptor.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.user.UserDetailsService;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.authentication.interceptor.TokenAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthConfig(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(userDetailsService)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationFilter(userDetailsService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(userDetailsService));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
