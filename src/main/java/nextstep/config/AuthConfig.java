package nextstep.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthMemberArgumentResolver;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

	private final AuthMemberArgumentResolver authMemberArgumentResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authMemberArgumentResolver);
	}
}
