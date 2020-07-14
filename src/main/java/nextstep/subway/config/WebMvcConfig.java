package nextstep.subway.config;

import nextstep.subway.auth.ui.interceptor.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private CustomUserDetailsService userDetailsService;

    public WebMvcConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService)).addPathPatterns("/login/session");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
    }
}
