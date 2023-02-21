package nextstep.subway.fake;

import nextstep.member.application.AuthService;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class FakeAuthService extends AuthService {
    public FakeAuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        super(jwtTokenProvider, memberRepository, new FakeGithubClient());
    }
}
