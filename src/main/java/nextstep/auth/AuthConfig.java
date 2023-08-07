package nextstep.auth;

import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.MemberRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider, memberRepository));
    }
}
