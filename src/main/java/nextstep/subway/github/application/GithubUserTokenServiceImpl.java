package nextstep.subway.github.application;

import nextstep.subway.auth.application.JwtTokenProvider;
import nextstep.subway.auth.application.UserTokenService;
import nextstep.subway.auth.application.dto.UserTokenRequest;
import nextstep.subway.github.application.dto.GithubProfileResponse;
import nextstep.subway.github.domain.GithubClient;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class GithubUserTokenServiceImpl implements UserTokenService {
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;
    private MemberService memberService;

    public GithubUserTokenServiceImpl(JwtTokenProvider jwtTokenProvider, GithubClient githubClient, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
        this.memberService = memberService;
    }

    @Override
    public String createToken(UserTokenRequest userTokenRequest) {
        if (isInvalidRequest(userTokenRequest)) {
            return null;
        }

        String githubAccessToken = githubClient.requestGithubToken(userTokenRequest.getCode());

        GithubProfileResponse githubProfileResponse = githubClient.requestGithubUserInfo(githubAccessToken);
        String email = githubProfileResponse.getEmail();

        Member member = memberService.findByEmailOrCreateMember(email);

        return jwtTokenProvider.createToken(member.getEmail());
    }

    private boolean isInvalidRequest(UserTokenRequest userTokenRequest) {
        return userTokenRequest.getCode() == null || userTokenRequest.getCode().isEmpty();
    }
}
