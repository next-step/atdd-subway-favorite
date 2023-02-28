package nextstep.config;

import lombok.RequiredArgsConstructor;
import nextstep.auth.Interceptor.AuthenticationInterceptor;
import nextstep.auth.resolver.AuthHeaderResolver;
import nextstep.auth.domain.AuthTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthHeaderResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor(null))
                .addPathPatterns("/members/**")
                .addPathPatterns("/favorites/**")
                .excludePathPatterns("/members");
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor(AuthTypes authTypes) {
        return new AuthenticationInterceptor(authTypes); // 자동 bean 등록시 bean 순환참조 문제가 발생해 수동 등록으로 변경 (원인 발견실패)
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
