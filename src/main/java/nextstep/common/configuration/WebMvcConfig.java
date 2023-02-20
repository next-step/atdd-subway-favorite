package nextstep.common.configuration;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.filter.BasicAuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberService memberService;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new BasicAuthorizationInterceptor(memberService))
                .addPathPatterns("/members/me");
    }
}
