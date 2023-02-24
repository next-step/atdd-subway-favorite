package nextstep.member.domain;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
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
        return memberService.findByEmail(principal);
    }

    @Override
    public void validate(String header) {
        if (!match(header)) {
            throw new IllegalArgumentException("jwt 인증 헤더 정보가 유효하지 않습니다");
        }

        String token = parseAccessToken(header);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 jwt 토큰 입니다.");
        }
    }

    @Override
    protected String getPrefix() {
        return BEARER_PREFIX;
    }
}
