package nextstep.auth;

import nextstep.auth.authentication.*;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.interceptor.ChainedAuthorizationInterceptor;
import nextstep.auth.interceptor.UnchainedAuthenticationInterceptor;
import nextstep.auth.service.UserDetailsService;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthentication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    public AuthConfig(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UnchainedAuthenticationInterceptor(new UsernamePasswordAuthentication(userDetailsService))).addPathPatterns("/login/form");
        registry.addInterceptor(new UnchainedAuthenticationInterceptor(new TokenAuthentication(userDetailsService, jwtTokenProvider))).addPathPatterns("/login/token");
        registry.addInterceptor(new ChainedAuthorizationInterceptor(new BasicAuthentication(userDetailsService)));
        registry.addInterceptor(new ChainedAuthorizationInterceptor(new BearerTokenAuthentication(jwtTokenProvider)));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
