package nextstep.subway.fake;

import org.springframework.stereotype.Service;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.LoginService;
import nextstep.member.application.MemberService;

@Service
public class FakeLoginService extends LoginService {
    public FakeLoginService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        super(memberService, jwtTokenProvider, new FakeGithubClient());
    }
}
