package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationProvider;
import nextstep.auth.authentication.ProviderManager;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.authentication.UsernamePasswordAuthenticationProvider;
import nextstep.auth.authentication.session.SessionAuthenticationConverter;
import nextstep.auth.authentication.session.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.token.TokenAuthenticationConverter;
import nextstep.auth.authentication.token.TokenAuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public AuthConfig(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ProviderManager providerManager = new ProviderManager(providers());

        registry.addInterceptor(new SessionAuthenticationInterceptor(new SessionAuthenticationConverter(), providerManager)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(jwtTokenProvider, objectMapper, new TokenAuthenticationConverter(objectMapper), providerManager)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    private List<AuthenticationProvider> providers() {
        return Arrays.asList(
                new UsernamePasswordAuthenticationProvider(userDetailsService)
        );
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
