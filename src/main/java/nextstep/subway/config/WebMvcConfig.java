package nextstep.subway.config;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.application.converter.SessionAuthenticationConverter;
import nextstep.subway.auth.application.converter.TokenAuthenticationConverter;
import nextstep.subway.auth.application.handler.AuthenticatedUserDetailsMethodArgumentResolver;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public WebMvcConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(new SessionAuthenticationConverter(), userDetailsService))
                .addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(new TokenAuthenticationConverter(), userDetailsService, jwtTokenProvider))
                .addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(userDetailsService, jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticatedUserDetailsMethodArgumentResolver());
    }
}
