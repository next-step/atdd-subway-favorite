package nextstep.member.domain;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class GithubAuthType implements AuthType {

    private static final String PREFIX = "token ";

    private final MemberService memberService;

    @Override
    public boolean match(String header) {
        if (StringUtils.hasText(header)
                && header.startsWith(PREFIX)) {
            return true;
        }

        return false;
    }

    @Override
    public Member findMember(String header) {
        String token = parseAccessToken(header);
        return memberService.findByAccessToken(token);
    }

    @Override
    public void validate(String header) {
        if (!match(header)) {
            throw new IllegalArgumentException();
        }
    }

    public String parseAccessToken(String header) {
        if (!match(header)) {
            throw new IllegalArgumentException("깃허브 인증 헤더 정보가 유효하지 않습니다");
        }

        return header.substring(PREFIX.length());
    }
}
