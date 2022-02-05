package nextstep.auth.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.ui.authentication.session.SessionAuthenticationConverter;
import nextstep.auth.ui.authentication.session.SessionAuthenticationInterceptor;
import nextstep.auth.ui.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.ui.authentication.token.TokenAuthenticationInterceptor;
import nextstep.auth.ui.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.ui.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public AuthConfig(CustomUserDetailsService userDetailsService,
                      JwtTokenProvider jwtTokenProvider,
                      ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(new SessionAuthenticationConverter(), userDetailsService)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(new TokenAuthenticationConverter(objectMapper),userDetailsService, jwtTokenProvider, objectMapper)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
