package atdd.favorite.application.dto;

import atdd.favorite.application.dto.LoginUserMethodArgumentResolver;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.jwt.ReadProperties;
import atdd.user.web.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private LoginUserMethodArgumentResolver loginUserMethodArgumentResolver;
    private ReadProperties readProperties;
    private JwtTokenProvider jwtTokenProvider;
    public WebConfig(LoginUserMethodArgumentResolver loginUserMethodArgumentResolver,
                     ReadProperties readProperties,
                     JwtTokenProvider jwtTokenProvider) {
        this.loginUserMethodArgumentResolver = loginUserMethodArgumentResolver;
        this.readProperties = readProperties;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider, readProperties))
                .addPathPatterns("/users/me",
                        "/favorite-stations/**",
                        "/favorite-paths/**"
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserMethodArgumentResolver);
    }
}
