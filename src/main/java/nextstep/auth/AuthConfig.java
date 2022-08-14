package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.filter.BasicAuthenticationFilter;
import nextstep.auth.filter.BearerTokenAuthenticationFilter;
import nextstep.auth.filter.TokenAuthenticationFilter;
import nextstep.auth.filter.UsernamePasswordAuthenticationFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.userdetail.UserDetailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(userDetailService))
                .addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationFilter(userDetailService, jwtTokenProvider, objectMapper))
                .addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(userDetailService));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

}
