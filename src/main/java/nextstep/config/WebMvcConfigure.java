package nextstep.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {
    private final AuthorizationResolver authorizationResolver;

    public WebMvcConfigure(AuthorizationResolver authorizationResolver) {
        this.authorizationResolver = authorizationResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizationResolver);
    }
}
