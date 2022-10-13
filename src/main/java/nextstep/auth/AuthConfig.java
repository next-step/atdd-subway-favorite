package nextstep.auth;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.UsernamePasswordAuthFilter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.authentication.BasicAuthFilter;
import nextstep.auth.authentication.BearerTokenAuthFilter;
import nextstep.auth.user.UserDetailService;
import nextstep.auth.token.TokenAuthInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthFilter(userDetailService)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthInterceptor(userDetailService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthFilter(userDetailService));
        registry.addInterceptor(new BearerTokenAuthFilter(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
