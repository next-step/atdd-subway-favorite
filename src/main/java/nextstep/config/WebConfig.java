package nextstep.config;

import java.util.List;
import nextstep.member.ui.argumentresolver.BearerTokenArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final BearerTokenArgumentResolver bearerTokenArgumentResolver;

    public WebConfig(final BearerTokenArgumentResolver bearerTokenArgumentResolver) {
        this.bearerTokenArgumentResolver = bearerTokenArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(bearerTokenArgumentResolver);
    }
}
