package nextstep.auth.domain;

import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubAuthType extends AbstractAuthType {

    private static final String PREFIX = "token ";

    private final MemberService memberService;

    @Override
    public Member findMember(String header) {
        String token = parseAccessToken(header);
        return memberService.findByAccessToken(token);
    }

    @Override
    public void validate(String header) {
        if (!match(header)) {
            throw new AuthenticationException();
        }
    }

    @Override
    protected String getPrefix() {
        return PREFIX;
    }
}
