package nextstep.auth;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.BasicAuthenticationFilter;
import nextstep.auth.authentication.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
import nextstep.member.application.LoginMemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    private static final String LOGIN_FORM = "/login/form";
    private static final String LOGIN_TOKEN = "/login/token";

    private final LoginMemberService loginMemberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(loginMemberService)).addPathPatterns(LOGIN_FORM);
        registry.addInterceptor(new TokenAuthenticationInterceptor(loginMemberService, jwtTokenProvider)).addPathPatterns(LOGIN_TOKEN);
        registry.addInterceptor(new BasicAuthenticationFilter(loginMemberService)).excludePathPatterns(LOGIN_FORM, LOGIN_TOKEN);
        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider)).excludePathPatterns(LOGIN_FORM, LOGIN_TOKEN);
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
