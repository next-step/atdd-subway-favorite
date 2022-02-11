package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.*;
import nextstep.auth.authorization.*;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final AuthenticationConverter sessionAuthenticationConverter;
    private final AuthenticationConverter tokenAuthenticationConverter;
    private final SecurityContextHolderStrategy sessionSecurityContextHolderStrategy;
    private final TokenSecurityContextHolderStrategy tokenSecurityContextHolderStrategy;

    public AuthConfig(CustomUserDetailsService userDetailsService,
                      JwtTokenProvider jwtTokenProvider,
                      ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.sessionAuthenticationConverter = new SessionAuthenticationConverter();
        this.tokenAuthenticationConverter = new TokenAuthenticationConverter();
        sessionSecurityContextHolderStrategy = new SessionSecurityContextHolderStrategy();
        tokenSecurityContextHolderStrategy = new TokenSecurityContextHolderStrategy(jwtTokenProvider);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService, sessionAuthenticationConverter))
                .addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsService, tokenAuthenticationConverter, jwtTokenProvider, objectMapper))
                .addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor(sessionSecurityContextHolderStrategy));
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider, tokenSecurityContextHolderStrategy));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
