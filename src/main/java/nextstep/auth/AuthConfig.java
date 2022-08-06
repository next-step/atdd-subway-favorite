package nextstep.auth;

import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.intercpetor.*;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.LoginMemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private LoginMemberService loginMemberService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthConfig(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(loginMemberService))
                .addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(loginMemberService, jwtTokenProvider))
                .addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(loginMemberService));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider));
        registry.addInterceptor(new AuthenticationCheckFilter());
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
