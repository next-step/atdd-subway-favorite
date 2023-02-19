package nextstep.subway.utils;

import nextstep.member.application.LoginService;
import nextstep.member.application.MemberService;
import nextstep.member.auth.GithubClient;
import nextstep.member.auth.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class FakeLoginService extends LoginService {

    public FakeLoginService(JwtTokenProvider jwtTokenProvider, MemberService memberService, GithubClient githubClient) {
        super(jwtTokenProvider, memberService, new FakeGithubClient());
    }
}
