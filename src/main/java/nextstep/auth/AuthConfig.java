package nextstep.auth;

import nextstep.auth.authentication.filter.checking.BasicAuthenticationFilter;
import nextstep.auth.authentication.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.filter.processing.UsernamePasswordAuthenticationProcessingFilter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.authentication.filter.processing.TokenAuthenticationProcessingFilter;
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
        registry.addInterceptor(new UsernamePasswordAuthenticationProcessingFilter(loginMemberService)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationProcessingFilter(loginMemberService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(loginMemberService));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
