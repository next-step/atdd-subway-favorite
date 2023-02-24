package nextstep.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nextstep.member.auth.AuthenticationArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private final AuthenticationArgumentResolver authenticationArgumentResolver;

	public WebMvcConfig(AuthenticationArgumentResolver authenticationArgumentResolver) {
		this.authenticationArgumentResolver = authenticationArgumentResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authenticationArgumentResolver);
	}
}
