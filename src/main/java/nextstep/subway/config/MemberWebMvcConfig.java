package nextstep.subway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class MemberWebMvcConfig implements WebMvcConfigurer {
    private final FavoriteInterceptor favoriteInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(favoriteInterceptor)
                .addPathPatterns("/favorites/**");
    }
}
