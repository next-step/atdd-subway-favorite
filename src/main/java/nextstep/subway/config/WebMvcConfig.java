package nextstep.subway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.BasicAuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.authentication.FormAuthenticationConverter;
import nextstep.subway.auth.ui.interceptor.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private CustomUserDetailsService userDetailsService;
    private ObjectMapper objectMapper;
    private JwtTokenProvider jwtTokenProvider;

    public WebMvcConfig(CustomUserDetailsService userDetailsService, ObjectMapper objectMapper, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenAuthenticationInterceptor(new BasicAuthenticationConverter(), userDetailsService, jwtTokenProvider, objectMapper)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionAuthenticationInterceptor(new FormAuthenticationConverter(), userDetailsService)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(userDetailsService, jwtTokenProvider));
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
    }
}
