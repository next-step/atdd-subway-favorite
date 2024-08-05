package nextstep.subway.github.application;

import nextstep.subway.auth.application.JwtTokenProvider;
import nextstep.subway.auth.application.MemberDetailsService;
import nextstep.subway.auth.application.UserTokenService;
import nextstep.subway.auth.application.dto.UserTokenRequest;
import nextstep.subway.auth.domain.MemberDetails;
import nextstep.subway.github.application.dto.GithubProfileResponse;
import nextstep.subway.github.domain.GithubClient;
import org.springframework.stereotype.Service;

@Service
public class GithubUserTokenServiceImpl implements UserTokenService {
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;
    private MemberDetailsService memberDetailsService;

    public GithubUserTokenServiceImpl(JwtTokenProvider jwtTokenProvider
            , GithubClient githubClient
            , MemberDetailsService memberDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
        this.memberDetailsService = memberDetailsService;
    }

    @Override
    public String createToken(UserTokenRequest userTokenRequest) {
        if (isInvalidRequest(userTokenRequest)) {
            return null;
        }

        String githubAccessToken = githubClient.requestGithubToken(userTokenRequest.getCode());

        GithubProfileResponse githubProfileResponse = githubClient.requestGithubUserInfo(githubAccessToken);
        String email = githubProfileResponse.getEmail();

        MemberDetails memberDetails = memberDetailsService.findByEmailOrCreateMember(email);

        return jwtTokenProvider.createToken(memberDetails.getEmail());
    }

    private boolean isInvalidRequest(UserTokenRequest userTokenRequest) {
        return userTokenRequest.getCode() == null || userTokenRequest.getCode().isEmpty();
    }
}
