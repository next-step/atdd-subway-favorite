package nextstep.auth.domain;

import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.util.AuthUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubAuthService implements AuthService {

    private static final String AUTH_HEADER_PREFIX = "token ";

    private final MemberService memberService;

    @Override
    public Member findMember(String header) {
        String token = AuthUtil.parseAccessToken(header, AUTH_HEADER_PREFIX);
        return memberService.findByAccessToken(token);
    }

    @Override
    public void validate(String header) {
        if (!AuthUtil.match(header, AUTH_HEADER_PREFIX)) {
            throw new AuthenticationException();
        }
    }

    @Override
    public String getPrefix() {
        return AUTH_HEADER_PREFIX;
    }
}
