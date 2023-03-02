package nextstep.member.config;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.filters.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private JwtTokenProvider jwtTokenProvider;

    public WebMvcConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(new AuthenticationInterceptor(jwtTokenProvider))
                .addPathPatterns("/members/me");
    }
}
