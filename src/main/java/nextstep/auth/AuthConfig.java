package nextstep.auth;

import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.filter.AuthenticationFilter;
import nextstep.auth.filter.AuthorizationFilter;
import nextstep.auth.filter.BasicFilter;
import nextstep.auth.filter.BearerFilter;
import nextstep.auth.filter.LoginService;
import nextstep.auth.filter.TokenAuthenticationInterceptor;
import nextstep.auth.filter.UsernamePasswordAuthenticationFilter;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthConfig(LoginService loginService, JwtTokenProvider jwtTokenProvider) {
        this.loginService = loginService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(usernamePasswordFilter()).addPathPatterns("/login/form");
        registry.addInterceptor(tokenFilter()).addPathPatterns("/login/token");
        registry.addInterceptor(basicFilter());
        registry.addInterceptor(bearerFilter());
    }


    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    private AuthorizationFilter bearerFilter() {
        return new AuthorizationFilter(new BearerFilter(jwtTokenProvider, loginService));
    }

    private AuthorizationFilter basicFilter() {
        return new AuthorizationFilter(new BasicFilter(loginService));
    }

    private AuthenticationFilter tokenFilter() {
        return new AuthenticationFilter(new TokenAuthenticationInterceptor(jwtTokenProvider), loginService);
    }

    private AuthenticationFilter usernamePasswordFilter() {
        return new AuthenticationFilter(new UsernamePasswordAuthenticationFilter(), loginService);
    }

}
