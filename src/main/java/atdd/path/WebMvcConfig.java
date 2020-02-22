package atdd.path;

import atdd.path.dao.UserDao;
import atdd.path.security.JwtAuthInterceptor;
import atdd.path.security.TokenAuthenticationService;
import atdd.path.security.UserHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private UserDao userDao;

    private static String[] INTERCEPTOR_WITHE_LIST = {
            "/users/login",
            "/users/sigh-up",
    };

    public WebMvcConfig(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(INTERCEPTOR_WITHE_LIST);
    }

    @Bean
    public JwtAuthInterceptor jwtAuthInterceptor() {
        return new JwtAuthInterceptor(new TokenAuthenticationService(), userDao);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserHandlerMethodArgumentResolver());
    }
}
