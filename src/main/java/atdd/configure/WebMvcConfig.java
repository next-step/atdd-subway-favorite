package atdd.configure;

import atdd.member.interceptor.LoginInterceptor;
import atdd.member.security.LoginHandlerMethodArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    private final LoginInterceptor loginInterceptor;
    private final LoginHandlerMethodArgumentResolver loginUserArgumentResolver;

    public WebMvcConfig(LoginInterceptor loginInterceptor,
        LoginHandlerMethodArgumentResolver loginUserArgumentResolver) {
        this.loginInterceptor = loginInterceptor;
        this.loginUserArgumentResolver = loginUserArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
            .addPathPatterns("/members/me");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }

}
