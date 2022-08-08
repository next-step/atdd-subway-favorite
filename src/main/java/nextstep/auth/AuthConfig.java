package nextstep.auth;

import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.filters.BasicAuthenticationFilter;
import nextstep.auth.filters.BearerTokenAuthenticationFilter;
import nextstep.auth.filters.TokenAuthenticationFilter;
import nextstep.auth.filters.UsernamePasswordAuthenticationFilter;
import nextstep.auth.filters.converter.TokenAuthenticationConverter;
import nextstep.auth.filters.provider.DefaultAuthenticationProvider;
import nextstep.auth.filters.provider.JwtAuthenticationProvider;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final DefaultAuthenticationProvider defaultAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final TokenAuthenticationConverter tokenAuthenticationConverter;

    public AuthConfig(JwtTokenProvider jwtTokenProvider,
                      DefaultAuthenticationProvider defaultAuthenticationProvider,
                      JwtAuthenticationProvider jwtAuthenticationProvider,
                      TokenAuthenticationConverter tokenAuthenticationConverter) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.defaultAuthenticationProvider = defaultAuthenticationProvider;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());

        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(defaultAuthenticationProvider))
                .addPathPatterns("/login/form");

        registry.addInterceptor(new TokenAuthenticationFilter(defaultAuthenticationProvider, jwtTokenProvider, tokenAuthenticationConverter))
                .addPathPatterns("/login/token");

        registry.addInterceptor(new BasicAuthenticationFilter(defaultAuthenticationProvider));

        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtAuthenticationProvider));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
