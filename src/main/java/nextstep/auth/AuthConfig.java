package nextstep.auth;

import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.filter.AuthenticationFilter;
import nextstep.auth.filter.AuthorizationFilter;
import nextstep.auth.filter.BasicAuthorizationStrategy;
import nextstep.auth.filter.BearerAuthorizationStrategy;
import nextstep.auth.filter.TokenAuthenticationStrategy;
import nextstep.auth.filter.UsernamePasswordAuthenticationStrategy;
import nextstep.auth.member.UserDetailService;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthConfig(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(usernamePasswordFilter()).addPathPatterns("/login/form");
        registry.addInterceptor(tokenFilter()).addPathPatterns("/login/token");
        registry.addInterceptor(basicFilter());
        registry.addInterceptor(bearerFilter());
    }


    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    private AuthorizationFilter bearerFilter() {
        return new AuthorizationFilter(new BearerAuthorizationStrategy(jwtTokenProvider, userDetailService));
    }

    private AuthorizationFilter basicFilter() {
        return new AuthorizationFilter(new BasicAuthorizationStrategy(userDetailService));
    }

    private AuthenticationFilter tokenFilter() {
        return new AuthenticationFilter(new TokenAuthenticationStrategy(jwtTokenProvider), userDetailService);
    }

    private AuthenticationFilter usernamePasswordFilter() {
        return new AuthenticationFilter(new UsernamePasswordAuthenticationStrategy(), userDetailService);
    }

}
