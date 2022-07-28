package nextstep.config;

import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.handler.BasicAuthVerificationHandler;
import nextstep.auth.handler.BearerTokenAuthVerificationHandler;
import nextstep.auth.handler.TokenAuthenticationHandler;
import nextstep.auth.handler.UsernamePasswordAuthenticationHandler;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.LoginMemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    private LoginMemberService loginMemberService;
    private JwtTokenProvider jwtTokenProvider;

    public AppConfig(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationHandler(loginMemberService)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationHandler(loginMemberService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthVerificationHandler(loginMemberService));
        registry.addInterceptor(new BearerTokenAuthVerificationHandler(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
