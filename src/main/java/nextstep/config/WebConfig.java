package nextstep.config;

import nextstep.member.application.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalResolver authenticationPrincipalResolver() {
        return new AuthenticationPrincipalResolver(jwtTokenProvider);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalResolver());
    }
}
