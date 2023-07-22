package nextstep.api.auth.application.token;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.AuthenticationException;
import nextstep.api.auth.application.token.dto.TokenResponse;
import nextstep.api.auth.application.token.oauth2.OAuth2User;
import nextstep.api.auth.application.token.oauth2.OAuth2UserService;
import nextstep.api.auth.application.token.oauth2.github.GithubClient;
import nextstep.api.auth.application.userdetails.UserDetails;
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
        final var userDetails = loadUser(email, password);
        final var token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getRole());
        return new TokenResponse(token);
    }

    private UserDetails loadUser(final String email, final String password) {
        final var userDetails = userDetailsService.loadUserByUsername(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
        return userDetails;
    }

    public TokenResponse createTokenFromGithub(String code) {
        final var oAuth2User = loadUserFromGithub(code);
        final var token = jwtTokenProvider.createToken(oAuth2User.getUsername(), oAuth2User.getRole());
        return new TokenResponse(token);
    }

    private OAuth2User loadUserFromGithub(final String code) {
        final var accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);
        final var githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);
        return oAuth2UserService.loadUser(githubProfile);
    }
}
