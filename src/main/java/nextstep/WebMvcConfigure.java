package nextstep;

import java.util.List;
import nextstep.auth.AccessTokenArgumentResolver;
import nextstep.member.application.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {
    private final JwtTokenProvider tokenProvider;

    public WebMvcConfigure(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AccessTokenArgumentResolver(tokenProvider));
    }

}