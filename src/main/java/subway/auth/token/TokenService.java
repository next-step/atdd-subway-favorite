package subway.auth.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.auth.token.oauth2.OAuth2User;
import subway.auth.token.oauth2.OAuth2UserService;
import subway.auth.token.oauth2.github.GithubClient;
import subway.auth.token.oauth2.github.GithubProfileResponse;
import subway.auth.userdetails.UserDetails;
import subway.auth.userdetails.UserDetailsService;
import subway.constant.SubwayMessage;
import subway.exception.AuthenticationException;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserDetailsService userDetailsService;
    private final OAuth2UserService oAuth2UserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse createToken(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException(SubwayMessage.AUTH_PASSWORD_IS_NOT_VALID);
        }
        String token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getRole());
        return TokenResponse.builder().accessToken(token).build();
    }

    public TokenResponse createTokenFromGithub(String code) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);
        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        OAuth2User oAuth2User = oAuth2UserService.loadUser(githubProfile);

        String token = jwtTokenProvider.createToken(oAuth2User.getUsername(), oAuth2User.getRole());

        return TokenResponse.builder().accessToken(token).build();
    }
}
