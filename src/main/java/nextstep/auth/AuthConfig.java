package nextstep.auth;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.authentication.BasicAuthenticationFilter;
import nextstep.auth.authentication.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.filter.nonChain.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.filter.nonChain.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private UserDetailService userDetailService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthConfig(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(userDetailService)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(userDetailService));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
