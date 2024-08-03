package nextstep.subway.auth.application;

import nextstep.subway.auth.application.dto.UserDetailsRequest;
import nextstep.subway.github.application.dto.GithubProfileResponse;
import nextstep.subway.github.application.dto.GithubTokenRequest;
import nextstep.subway.github.domain.GithubClient;
import nextstep.subway.exception.AuthenticationException;
import nextstep.subway.auth.application.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private UserDetailsServiceProvider userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(UserDetailsServiceProvider userDetailsService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = userDetailsService.getMember(new UserDetailsRequest(email, password));

        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail()));
    }

    public TokenResponse createGithubToken(GithubTokenRequest request) {
        String githubAccessToken = githubClient.requestGithubToken(request.getCode());

        GithubProfileResponse githubProfileResponse = githubClient.requestGithubUserInfo(githubAccessToken);
        String email = githubProfileResponse.getEmail();

        Member member = userDetailsService.getMember(new UserDetailsRequest(email));

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail()));
    }
}
