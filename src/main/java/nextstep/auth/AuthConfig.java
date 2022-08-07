package nextstep.auth;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.nonchain.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authentication.chain.AuthenticationChainFilter;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.authentication.nonchain.TokenAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final List<AuthenticationChainFilter> chainFilters;

    @Qualifier("defaultAuthenticationProvider")
    private final AuthenticationProvider<AuthenticationToken> authenticationProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(authenticationProvider)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(jwtTokenProvider, authenticationProvider)).addPathPatterns("/login/token");
        chainFilters.forEach(registry::addInterceptor);
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
