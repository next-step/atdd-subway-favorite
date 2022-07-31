package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.interceptors.BasicAuthenticationInterceptor;
import nextstep.auth.authentication.interceptors.BearerTokenAuthenticationInterceptor;
import nextstep.auth.authentication.interceptors.UsernamePasswordAuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.authentication.interceptors.TokenAuthenticationInterceptor;
import nextstep.member.application.LoginMemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final LoginMemberService loginMemberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public AuthConfig(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider, final ObjectMapper objectMapper) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationInterceptor(loginMemberService)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(loginMemberService, jwtTokenProvider, objectMapper)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationInterceptor(loginMemberService));
        registry.addInterceptor(new BearerTokenAuthenticationInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
