package nextstep.auth;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nextstep.auth.authentication.BasicAuthenticationFilter2;
import nextstep.auth.authentication.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.UsernamePasswordAuthenticationFilter2;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.service.CustomUserDetails;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor2;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
	private JwtTokenProvider jwtTokenProvider;
	private CustomUserDetails customUserDetails;

	public AuthConfig(JwtTokenProvider jwtTokenProvider, CustomUserDetails customUserDetails) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.customUserDetails = customUserDetails;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SecurityContextPersistenceFilter());
		registry.addInterceptor(new UsernamePasswordAuthenticationFilter2(customUserDetails))
			.addPathPatterns("/login/form");
		registry.addInterceptor(new TokenAuthenticationInterceptor2(customUserDetails, jwtTokenProvider))
			.addPathPatterns("/login/token");
		registry.addInterceptor(new BasicAuthenticationFilter2(customUserDetails));
		registry.addInterceptor(new BearerTokenAuthenticationFilter(jwtTokenProvider));
	}

	@Override
	public void addArgumentResolvers(List argumentResolvers) {
		argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
	}
}
