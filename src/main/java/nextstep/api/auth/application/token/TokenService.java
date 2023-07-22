package nextstep.api.auth.application.token;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.AuthenticationException;
import nextstep.api.auth.application.token.dto.TokenResponse;
import nextstep.api.auth.application.token.oauth2.OAuth2UserService;
import nextstep.api.auth.application.token.oauth2.github.GithubClient;
import nextstep.api.auth.application.userdetails.UserDetailsService;
import nextstep.api.auth.support.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserDetailsService userDetailsService;
    private final OAuth2UserService oAuth2UserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse createToken(final String email, final String password) {
        final var userDetails = userDetailsService.loadUserByUsername(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        final var token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getRole());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenFromGithub(String code) {
        final var accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);
        final var githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        final var oAuth2User = oAuth2UserService.loadUser(githubProfile);

        final var token = jwtTokenProvider.createToken(oAuth2User.getUsername(), oAuth2User.getRole());
        return new TokenResponse(token);
    }
}
