package nextstep.auth;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.filter.chain.BasicAuthenticationFilter;
import nextstep.auth.authentication.filter.chain.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.filter.nonchain.TokenAuthenticationFilter;
import nextstep.auth.authentication.filter.nonchain.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authentication.provider.ProviderManager;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.extractor.BasicAuthorizationExtractor;
import nextstep.auth.authorization.extractor.BearerAuthorizationExtractor;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    private static final String LOGIN_FORM = "/login/form";
    private static final String LOGIN_TOKEN = "/login/token";

    private final JwtTokenProvider jwtTokenProvider;
    private final ProviderManager providerManager;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(providerManager)).addPathPatterns(LOGIN_FORM);
        registry.addInterceptor(new TokenAuthenticationFilter(providerManager, jwtTokenProvider)).addPathPatterns(LOGIN_TOKEN);
        registry.addInterceptor(new BasicAuthenticationFilter(providerManager, new BasicAuthorizationExtractor())).excludePathPatterns(LOGIN_FORM, LOGIN_TOKEN);
        registry.addInterceptor(new BearerTokenAuthenticationFilter(providerManager, new BearerAuthorizationExtractor())).excludePathPatterns(LOGIN_FORM, LOGIN_TOKEN);
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
