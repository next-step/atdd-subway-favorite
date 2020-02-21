package atdd.path.web;

import atdd.path.jwt.JwtTokenProvider;
import atdd.path.jwt.ReadProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
                .addPathPatterns(Constant.USER_BASE_URI + "/me");
    }
}
