package nextstep.auth.domain;

import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthenticationException;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthType extends AbstractAuthType {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    public Member findMember(String header) {
        String token = parseAccessToken(header);
        String principal = jwtTokenProvider.getPrincipal(token);
        return memberService.findByEmail(principal)
                .orElseThrow(() -> new IllegalArgumentException("이메일로 회원을 찾을 수 업습니다. " + principal));
    }

    @Override
    public void validate(String header) {
        if (!match(header)) {
            throw new AuthenticationException("jwt 인증 헤더 정보가 유효하지 않습니다");
        }

        String token = parseAccessToken(header);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException("유효하지 않은 jwt 토큰 입니다.");
        }
    }

    @Override
    protected String getPrefix() {
        return BEARER_PREFIX;
    }
}
