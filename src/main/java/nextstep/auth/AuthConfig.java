package nextstep.auth;

import nextstep.auth.authentication.ProviderManager;
import nextstep.auth.authentication.filter.BasicAuthenticationFilter;
import nextstep.auth.authentication.filter.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.filter.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.authentication.provider.BasicAuthenticationProvider;
import nextstep.auth.authentication.provider.BearerAuthenticationProvider;
import nextstep.auth.authentication.provider.UserDetailsAuthenticationProvider;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.application.LoginMemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private LoginMemberService loginMemberService;
    private JwtTokenProvider jwtTokenProvider;

    private UserDetailsService userDetailsService;

    private ProviderManager providerManager;

    public AuthConfig(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider,
                      UserDetailsService userDetailsService) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.providerManager = configure();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(providerManager)).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(loginMemberService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(providerManager));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(providerManager));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Bean
    AuthenticationProvider userDetailsAuthenticationProvider() {
        return new UserDetailsAuthenticationProvider(userDetailsService);
    }

    @Bean
    AuthenticationProvider basicAuthenticationProvider() {
        return new BasicAuthenticationProvider(userDetailsService);
    }

    @Bean
    AuthenticationProvider bearerAuthenticationProvider() {
        return new BearerAuthenticationProvider(jwtTokenProvider);
    }

    public ProviderManager configure() {
        // provider 에게
        List<AuthenticationProvider> providers = Collections.emptyList();

        // provider 추가
        providers.add(userDetailsAuthenticationProvider());
        providers.add(basicAuthenticationProvider());
        providers.add(bearerAuthenticationProvider());

        return new ProviderManager(providers);
    }
}
