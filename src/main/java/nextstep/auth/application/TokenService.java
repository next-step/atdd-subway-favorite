package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TokenService {

    private final MemberDetailService memberDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse authenticateWithCredentials(String email, String password) {
        var member = memberDetailService.findByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail()));
    }

    @Transactional
    public TokenResponse authenticateWithGithub(final String code) {
        var tokenResponse = githubClient.getAccessTokenFromGithub(code);
        var githubProfile = githubClient.requestGithubProfile(tokenResponse.getAccessToken());
        var email = githubProfile.getEmail();

        var member = memberDetailService.findOrElseGet(email);

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail()));
    }
}
