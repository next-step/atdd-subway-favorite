package nextstep.auth;

import nextstep.auth.authentication.BasicAuthenticationInterceptor;
import nextstep.auth.authentication.BearerTokenAuthenticationInterceptor;
import nextstep.auth.authentication.UsernamePasswordAuthenticationInterceptorNon;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.authentication.TokenAuthenticationInterceptorNon;
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
        registry.addInterceptor(new UsernamePasswordAuthenticationInterceptorNon(loginMemberService)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptorNon(loginMemberService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationInterceptor(loginMemberService));
        registry.addInterceptor(new BearerTokenAuthenticationInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
