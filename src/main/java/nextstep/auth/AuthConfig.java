package nextstep.auth;

import nextstep.auth.authentication.SessionAuthenticationConverter;
import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.TokenAuthenticationConverter;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.UserDetailsServiceImpl;
import nextstep.member.application.UserDetailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final UserDetailService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthConfig(UserDetailsServiceImpl userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                new SessionAuthenticationInterceptor(
                    userDetailsService,
                    new SessionAuthenticationConverter()
                )
            )
            .addPathPatterns("/login/session");
        registry.addInterceptor(
                new TokenAuthenticationInterceptor(
                    userDetailsService,
                    jwtTokenProvider,
                    new TokenAuthenticationConverter()
                )
            )
            .addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
