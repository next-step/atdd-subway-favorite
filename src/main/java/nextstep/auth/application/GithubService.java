package nextstep.auth.application;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class GithubService {
    private final UserDetailService userDetailService;
    private final GithubClient githubClient;
    private final JwtTokenProvider jwtTokenProvider;
    public GithubService(final UserDetailService userDetailService, final GithubClient githubClient, final JwtTokenProvider jwtTokenProvider) {
        this.userDetailService = userDetailService;
        this.githubClient = githubClient;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(final String code) {
        final String githubToken = githubClient.requestGithubToken(code);
        final GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubToken);

        final UserDetails userDetails = userDetailService.findMemberOrCreate(githubProfileResponse);

        String token = jwtTokenProvider.createToken(userDetails.getEmail());

        return new TokenResponse(token);
    }
}
