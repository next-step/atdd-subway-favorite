package nextstep.member.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nextstep.member.application.JwtTokenProvider;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final JwtTokenProvider jwtTokenProvider;

	public WebConfig(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new TokenParseMethodHandler(jwtTokenProvider));
	}
}
