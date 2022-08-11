package nextstep.auth;

import nextstep.auth.authentication.UserDetailService;
import nextstep.auth.authentication.filter.chain.BasicAuthenticationFilter;
import nextstep.auth.authentication.filter.chain.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.filter.nonChain.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.filter.nonChain.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private static final String LOGIN_FORM = "/login/form";
    private static final String LOGIN_TOKEN = "/login/token";
    private UserDetailService userDetailService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthConfig(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(userDetailService)).addPathPatterns(LOGIN_FORM);
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailService, jwtTokenProvider)).addPathPatterns(LOGIN_TOKEN);
        registry.addInterceptor(new BasicAuthenticationFilter(userDetailService)).excludePathPatterns(LOGIN_FORM, LOGIN_TOKEN);
        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider)).excludePathPatterns(LOGIN_FORM, LOGIN_TOKEN);
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
