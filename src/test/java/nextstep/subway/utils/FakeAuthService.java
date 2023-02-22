package nextstep.subway.utils;

import nextstep.member.application.AuthService;
import nextstep.member.application.GithubClient;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import org.springframework.stereotype.Service;

@Service
public class FakeAuthService extends AuthService {

    public FakeAuthService(final JwtTokenProvider jwtTokenProvider, final MemberService memberService, final GithubClient fakeGithubClient) {
        super(jwtTokenProvider, memberService, fakeGithubClient);
    }
}
