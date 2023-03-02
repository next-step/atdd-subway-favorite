package nextstep.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.config.auth.AuthenticationArgumentResolver;
import nextstep.config.auth.interceptor.AuthenticationInterceptor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticationArgumentResolver authenticationArgumentResolver;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private static List<String> blackList;

    static {
        blackList = List.of(
                "/favorites/**",
                "/members/me"
        );
    }

    public WebConfig(AuthenticationArgumentResolver authenticationArgumentResolver, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper, MemberService memberService) {
        this.authenticationArgumentResolver = authenticationArgumentResolver;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.memberService = memberService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(jwtTokenProvider, objectMapper, memberService))
                .addPathPatterns(blackList);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }
}
