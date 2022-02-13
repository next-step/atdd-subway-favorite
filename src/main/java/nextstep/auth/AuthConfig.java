package nextstep.auth;

import nextstep.auth.authentication.*;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.ObjectMapperBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private UserDetailService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapperBean objectMapper;

    public AuthConfig(UserDetailService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapperBean objectMapper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService, new SesstionAuthenticationConverter())).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, objectMapper, new TokenAuthenticationConverter(objectMapper))).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
