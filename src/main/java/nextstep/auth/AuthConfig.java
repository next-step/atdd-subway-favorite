package nextstep.auth;

import nextstep.auth.authentication.BasicAuthenticationFilter;
import nextstep.auth.authentication.BearerTokenAuthenticationFilter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.filter.AuthenticationFilter;
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
        registry.addInterceptor(new BasicAuthenticationFilter(loginService));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider, loginService));
    }

    private AuthenticationFilter tokenFilter() {
        return new AuthenticationFilter(new TokenAuthenticationInterceptor(jwtTokenProvider), loginService);
    }

    private AuthenticationFilter usernamePasswordFilter() {
        return new AuthenticationFilter(new UsernamePasswordAuthenticationFilter(), loginService);
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
