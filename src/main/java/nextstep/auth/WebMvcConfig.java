package nextstep.auth;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.filter.BearerAuthorizationInterceptor;
import nextstep.member.application.MemberService;
import nextstep.auth.filter.BasicAuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new BasicAuthorizationInterceptor(memberService))
                .addPathPatterns("/members/me")
                .addPathPatterns("/favorites");
        registry.addInterceptor(new BearerAuthorizationInterceptor(memberService, jwtTokenProvider))
                .addPathPatterns("/members/me")
                .addPathPatterns("/favorites");
    }
}
