package nextstep.auth;

import nextstep.auth.authentication.interceptor.AuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationInterceptor tokenAuthenticationInterceptor;
    private AuthenticationInterceptor SessionAuthenticationInterceptor;

    public AuthConfig(JwtTokenProvider jwtTokenProvider, AuthenticationInterceptor tokenAuthenticationInterceptor, AuthenticationInterceptor sessionAuthenticationInterceptor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenAuthenticationInterceptor = tokenAuthenticationInterceptor;
        SessionAuthenticationInterceptor = sessionAuthenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(SessionAuthenticationInterceptor).addPathPatterns("/login/session");
        registry.addInterceptor(tokenAuthenticationInterceptor).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
