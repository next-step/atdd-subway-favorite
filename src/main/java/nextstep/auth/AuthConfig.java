package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthMemberLoader;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.converter.BasicAuthenticationConverter;
import nextstep.auth.converter.TokenAuthenticationConverter;
import nextstep.auth.converter.UsernamePasswordAuthenticationConverter;
import nextstep.auth.interceptor.BasicAuthenticationInterceptor;
import nextstep.auth.interceptor.BearerTokenAuthenticationInterceptor;
import nextstep.auth.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.interceptor.UsernamePasswordAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final AuthMemberLoader authMemberLoader;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public AuthConfig(AuthMemberLoader authMemberLoader, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.authMemberLoader = authMemberLoader;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationInterceptor(new UsernamePasswordAuthenticationConverter(), authMemberLoader)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(new TokenAuthenticationConverter(objectMapper), authMemberLoader, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationInterceptor(new BasicAuthenticationConverter(), authMemberLoader));
        registry.addInterceptor(new BearerTokenAuthenticationInterceptor(null, null, jwtTokenProvider)).excludePathPatterns("/members");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
