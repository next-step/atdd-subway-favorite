package nextstep.common.config;

import nextstep.auth.ui.AppMemberArgumentResolver;
import nextstep.member.domain.MemberRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public WebConfig(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AppMemberArgumentResolver(memberRepository, jwtTokenProvider));
    }
}
