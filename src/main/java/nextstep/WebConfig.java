package nextstep;

import nextstep.auth.AuthenticationUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationUserArgumentResolver authenticationUserArgumentResolver;

    public WebConfig(AuthenticationUserArgumentResolver authenticationUserArgumentResolver) {
        this.authenticationUserArgumentResolver = authenticationUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationUserArgumentResolver);
    }
}
