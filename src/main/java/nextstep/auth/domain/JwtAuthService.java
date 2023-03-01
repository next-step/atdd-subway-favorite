package nextstep.auth.domain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.dto.AuthMember;
import nextstep.exception.AuthenticationException;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.util.AuthUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthService implements AuthService {

    private static final String AUTH_HEADER_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    public AuthMember findMember(String header) {
        String token = AuthUtil.parseAccessToken(header, AUTH_HEADER_PREFIX);
        String principal = jwtTokenProvider.getPrincipal(token);
        Member member = memberService.findByEmail(principal)
                .orElseThrow(() -> new IllegalArgumentException("이메일로 회원을 찾을 수 업습니다. " + principal));

        return AuthMember.of(member);
    }

    @Override
    public void validate(String header) {
        if (!AuthUtil.match(header, AUTH_HEADER_PREFIX)) {
            throw new AuthenticationException("jwt 인증 헤더 정보가 유효하지 않습니다");
        }

        String token = AuthUtil.parseAccessToken(header, AUTH_HEADER_PREFIX);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException("유효하지 않은 jwt 토큰 입니다.");
        }
    }

    @Override
    public String getPrefix() {
        return AUTH_HEADER_PREFIX;
    }
}
