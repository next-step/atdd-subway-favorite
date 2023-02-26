package nextstep.member.configuration;

import nextstep.member.application.AuthService;
import nextstep.member.infrastructure.AuthInterceptor;
import nextstep.member.infrastructure.AuthPrincipalArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthService authService;
    private final AuthInterceptor authInterceptor;

    public WebMvcConfiguration(AuthService authService, AuthInterceptor authInterceptor) {
        this.authService = authService;
        this.authInterceptor = authInterceptor;
    }

    @Bean
    public AuthPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthPrincipalArgumentResolver(authService);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/favorites/**", "/members/me");
    }
}
