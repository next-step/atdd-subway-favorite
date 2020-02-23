package atdd.config;

import atdd.user.web.LoginUserArgumentResolver;
import atdd.user.web.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserInterceptor userInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    public WebMvcConfig(UserInterceptor userInterceptor, LoginUserArgumentResolver loginUserArgumentResolver) {
        this.userInterceptor = userInterceptor;
        this.loginUserArgumentResolver = loginUserArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns(
                        "/users/me",
                        "/favorites/**"
                );
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }
}
