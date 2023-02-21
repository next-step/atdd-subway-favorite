package nextstep.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final BearerAuthInterceptor bearerAuthInterceptor;

    private final TokenRequestArgumentResolver tokenRequestArgumentResolver;

    private final MemberInfoArgumentResolver memberInfoArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerAuthInterceptor)
//            .excludePathPatterns("/members/**", "/stations/**", "/lines/**", "/paths/**", "/login/**")
            .addPathPatterns("/members/me");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenRequestArgumentResolver);
        resolvers.add(memberInfoArgumentResolver);
    }


}
