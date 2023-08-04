package nextstep.auth;

import nextstep.auth.principal.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.domain.member.MemberRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;
    private MemberRepository memberRepository;

    public AuthConfig(JwtTokenProvider jwtTokenProvider,MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider,memberRepository));
    }
}
