package nextstep.auth;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.authentication.interceptor.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.interceptor.TokenAuthenticationInterceptor;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.application.CustomUserDetailsService;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
	private final CustomUserDetailsService userDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	private final ObjectMapper objectMapper;


	public AuthConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
		this.userDetailsService = userDetailsService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.objectMapper = objectMapper;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(
				new SessionAuthenticationInterceptor(new SessionAuthenticationConverter(), userDetailsService))
			.addPathPatterns("/login/session");
		registry.addInterceptor(
			new TokenAuthenticationInterceptor(new TokenAuthenticationConverter(objectMapper), userDetailsService,
				jwtTokenProvider)).addPathPatterns("/login/token");
		registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
		registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
	}

	@Override
	public void addArgumentResolvers(List argumentResolvers) {
		argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
	}
}
