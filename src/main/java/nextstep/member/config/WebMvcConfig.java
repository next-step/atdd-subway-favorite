package nextstep.member.config;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.config.argument.resolver.FindMemberOfMineRequestArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final TokenInterceptor tokenInterceptor;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/members/me");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new FindMemberOfMineRequestArgumentResolver(jwtTokenProvider));
    }
}
