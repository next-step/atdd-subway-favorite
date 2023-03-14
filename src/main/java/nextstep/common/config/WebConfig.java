package nextstep.common.config;

import java.util.List;
import nextstep.common.JwtParseResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JwtParseResolver jwtParseResolver;

    public WebConfig(final JwtParseResolver jwtParseResolver) {
        this.jwtParseResolver = jwtParseResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtParseResolver);
    }
}
