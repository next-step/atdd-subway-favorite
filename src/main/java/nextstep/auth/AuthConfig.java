package nextstep.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

	private final AuthArgumentResolver authArgumentResolver;

	public AuthConfig(AuthArgumentResolver authArgumentResolver) {
		this.authArgumentResolver = authArgumentResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authArgumentResolver);
	}
}
