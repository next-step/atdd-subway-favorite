package nextstep.member.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import nextstep.member.UsernameNotFoundException;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.GithubClient;
import nextstep.member.domain.Member;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.AuthService;
import nextstep.member.domain.UserDetails;
import nextstep.member.domain.UserDetailsService;

@Service
@Qualifier("githubAuthService")
public class GithubAuthService implements AuthService {
    private final String clientId;
    private final String clientSecret;
    private final GithubClient githubClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public GithubAuthService(
        @Value("${github.client-id}") String clientId,
        @Value("${github.client-secret}") String clientSecret,
        GithubClient githubClient,
        JwtTokenProvider jwtTokenProvider,
        UserDetailsService userDetailsService
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.githubClient = githubClient;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public TokenResponse login(TokenRequest request) {
        GithubAccessTokenRequest githubAccessTokenRequest = GithubAccessTokenRequest.of(
            clientId,
            clientSecret,
            request.getCode(),
            null,
            null
        );
        GithubAccessTokenResponse accessTokenResponse = githubClient.getAccessToken(githubAccessTokenRequest);
        GithubProfileResponse profile = githubClient.getUserProfile(accessTokenResponse.getAccessToken());

        UserDetails userDetails = userDetailsService.loadUserByGithubProfile(profile);
        String token = jwtTokenProvider.createToken(userDetails.getUsername());

        return TokenResponse.of(token);
    }
}
