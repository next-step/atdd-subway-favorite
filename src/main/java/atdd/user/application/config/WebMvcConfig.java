package atdd.user.application.config;

import atdd.user.application.JwtUtils;
import atdd.user.interceptor.AuthenticationInterceptor;
import atdd.user.web.LoginUserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(jwtUtils));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(jwtUtils))
                .addPathPatterns("/lines/*")
                .addPathPatterns("/lines")
                .addPathPatterns("/users/*")
                .excludePathPatterns("/users/login")
                .excludePathPatterns();
    }
}
