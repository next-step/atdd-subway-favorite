package nextstep.auth;

import nextstep.auth.authentication.*;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.interceptor.ChainedInterceptor;
import nextstep.auth.interceptor.UnchainedInterceptor;
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
        registry.addInterceptor(new UnchainedInterceptor(new UsernamePasswordAuthentication(userDetailsService))).addPathPatterns("/login/form");
        registry.addInterceptor(new UnchainedInterceptor(new TokenAuthentication(userDetailsService, jwtTokenProvider))).addPathPatterns("/login/token");
        registry.addInterceptor(new ChainedInterceptor(new BasicAuthentication(userDetailsService)));
        registry.addInterceptor(new ChainedInterceptor(new BearerTokenAuthentication(jwtTokenProvider)));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
