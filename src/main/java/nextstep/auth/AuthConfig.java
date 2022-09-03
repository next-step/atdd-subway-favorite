package nextstep.auth;

import java.util.List;
import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.filter.BasicAuthenticationFilter;
import nextstep.auth.authentication.filter.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.filter.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.authentication.handler.DefaultSuccessHandler;
import nextstep.auth.authentication.handler.TokenSuccessHandler;
import nextstep.auth.authentication.provider.BasicAuthenticationProvider;
import nextstep.auth.authentication.provider.BearerAuthenticationProvider;
import nextstep.auth.authentication.provider.UserDetailsAuthenticationProvider;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
import nextstep.auth.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;

    private UserDetailsService userDetailsService;


    public AuthConfig(JwtTokenProvider jwtTokenProvider,
                      UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(userDetailsAuthenticationProvider(), defaultSuccessHandler()))
                .addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsAuthenticationProvider(), tokenAuthenticationSuccessHandler()))
                .addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(basicAuthenticationProvider(), defaultSuccessHandler()));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(bearerAuthenticationProvider(), defaultSuccessHandler()));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Bean
    AuthenticationManager userDetailsAuthenticationProvider() {
        return new UserDetailsAuthenticationProvider(userDetailsService);
    }

    @Bean
    AuthenticationManager basicAuthenticationProvider() {
        return new BasicAuthenticationProvider(userDetailsService);
    }

    @Bean
    AuthenticationManager bearerAuthenticationProvider() {
        return new BearerAuthenticationProvider(jwtTokenProvider);
    }

    @Bean
    AuthenticationSuccessHandler tokenAuthenticationSuccessHandler() {
        return new TokenSuccessHandler(jwtTokenProvider);
    }

    AuthenticationSuccessHandler defaultSuccessHandler() {
        return new DefaultSuccessHandler();
    }
}
