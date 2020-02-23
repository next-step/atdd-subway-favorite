package atdd;

import atdd.Constant;
import atdd.user.jwt.JwtTokenProvider;
import atdd.user.jwt.ReadProperties;
import atdd.user.web.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static atdd.Constant.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;
    private ReadProperties readProperties;

    public WebConfig(JwtTokenProvider jwtTokenProvider, ReadProperties readProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.readProperties = readProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider, readProperties))
                .addPathPatterns(USER_BASE_URI + "/me",
                        FAVORITE_STATION_BASE_URI+"/**",
                        FAVORITE_PATH_BASE_URI+"/**"
                        );
    }
}
