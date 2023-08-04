package nextstep.auth.token;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.auth.token.oauth2.OAuth2User;
import nextstep.auth.token.oauth2.OAuth2UserService;
import nextstep.auth.token.oauth2.github.GithubClient;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserDetailsService userDetailsService;
    private final OAuth2UserService oAuth2UserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    private final MessageSource messageSource;


    public TokenResponse createToken(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException(messageSource.getMessage("auth.0001", null, Locale.KOREA));
        }

        String token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getRole());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenFromGithub(String code) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);
        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        OAuth2User oAuth2User = oAuth2UserService.loadUser(githubProfile);

        String token = jwtTokenProvider.createToken(oAuth2User.getUsername(), oAuth2User.getRole());

        return new TokenResponse(token);
    }
}
