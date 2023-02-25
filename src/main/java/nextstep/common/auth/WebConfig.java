package nextstep.common.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final List<HandlerInterceptor> interceptors;
    private final List<HandlerMethodArgumentResolver> resolvers;

    public WebConfig(final List<HandlerInterceptor> interceptors, final List<HandlerMethodArgumentResolver> resolvers) {
        this.interceptors = interceptors;
        this.resolvers = resolvers;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        for (HandlerInterceptor interceptor : interceptors) {
            registry.addInterceptor(interceptor);
        }
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(this.resolvers);
    }
}
