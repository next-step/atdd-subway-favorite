package nextstep.member.domain;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtAuth implements AuthType {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    public boolean match(String header) {
        if (StringUtils.hasText(header)
                && header.startsWith(BEARER_PREFIX)) {
            return true;
        }

        return false;
    }

    @Override
    public Member findMember(String header) {
        String token = parseToken(header);
        String principal = jwtTokenProvider.getPrincipal(token);
        return memberService.findByEmail(principal);
    }

    @Override
    public void validate(String header) {
        if (!match(header)) {
            throw new IllegalArgumentException("jwt 인증 헤더 정보가 유효하지 않습니다");
        }

        String token = parseToken(header);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 jwt 토큰 입니다.");
        }
    }

    private String parseToken(String header) {
        return header.substring(BEARER_PREFIX.length());
    }
}
