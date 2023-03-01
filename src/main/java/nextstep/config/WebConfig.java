package nextstep.config;

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
    private final MemberService memberService;
    private static List<String> whiteList;

    static {
        whiteList = List.of(
                "/login/token",
                "/login/github",
                "/stations/**",
                "/members/**",
                "/lines/**",
                "/paths",
                "/access-token",
                "/profile"
        );
    }

    public WebConfig(AuthenticationArgumentResolver authenticationArgumentResolver, JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.authenticationArgumentResolver = authenticationArgumentResolver;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(jwtTokenProvider, memberService))
                .excludePathPatterns(whiteList);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }
}
