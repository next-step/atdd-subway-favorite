package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import nextstep.auth.authentication.SessionAuthenticationConverter;
import nextstep.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.TokenAuthenticationConverter;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
	private ObjectMapper objectMapper;

    public AuthConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
		this.objectMapper = objectMapper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(new SessionAuthenticationInterceptor(new SessionAuthenticationConverter(), userDetailsService)).addPathPatterns("/login/session");
	    registry.addInterceptor(new TokenAuthenticationInterceptor(new TokenAuthenticationConverter(objectMapper), userDetailsService, jwtTokenProvider, objectMapper)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
