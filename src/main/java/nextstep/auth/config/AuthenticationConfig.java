package nextstep.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {

    private BearerAuthInterceptor bearerAuthInterceptor;

    public AuthenticationConfig(final BearerAuthInterceptor bearerAuthInterceptor) {
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/members/me");
    }
}
