package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TokenService {

    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse authenticateWithCredentials(String email, String password) {
        var userDetails = userDetailService.findByEmail(email);
        if (!userDetails.getCredential().equals(password)) {
            throw new AuthenticationException();
        }

        return new TokenResponse(jwtTokenProvider.createToken(userDetails.getPrincipal()));
    }

    @Transactional
    public TokenResponse authenticateWithGithub(final String code) {
        var tokenResponse = githubClient.getAccessTokenFromGithub(code);
        var githubProfile = githubClient.requestGithubProfile(tokenResponse.getAccessToken());
        var email = githubProfile.getEmail();

        var userDetails = userDetailService.findOrElseGet(email);

        return new TokenResponse(jwtTokenProvider.createToken(userDetails.getPrincipal()));
    }
}
