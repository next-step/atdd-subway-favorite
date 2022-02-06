package nextstep.auth;

import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.authentication.convert.AuthenticationConverter;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.UrlBasedAuthorizationInterceptor;
import nextstep.auth.service.UserDetailsService;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URI;
import java.util.List;

import static java.util.Arrays.asList;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConverter sessionAuthenticationConverter;
    private final AuthenticationConverter tokenAuthenticationConverter;

    public AuthConfig(UserDetailsService userDetailsService,
                      JwtTokenProvider jwtTokenProvider,
                      AuthenticationConverter sessionAuthenticationConverter,
                      AuthenticationConverter tokenAuthenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
        this.tokenAuthenticationConverter = tokenAuthenticationConverter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                new SessionAuthenticationInterceptor(userDetailsService, sessionAuthenticationConverter))
                .addPathPatterns("/login/session");
        registry.addInterceptor(
                new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, tokenAuthenticationConverter))
                .addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
        registry.addInterceptor(new UrlBasedAuthorizationInterceptor(getAuthorizedUriList()));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    private List<URI> getAuthorizedUriList() {
        return asList(URI.create("/members/me"), URI.create("/favorites/**"));
    }
}
