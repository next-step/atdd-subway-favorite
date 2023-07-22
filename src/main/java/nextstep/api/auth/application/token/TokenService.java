package nextstep.api.auth.application.token;

import org.springframework.stereotype.Service;

import nextstep.api.auth.AuthenticationException;
import nextstep.api.auth.application.token.oauth2.OAuth2User;
import nextstep.api.auth.application.token.oauth2.OAuth2UserService;
import nextstep.api.auth.application.token.oauth2.github.GithubClient;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubProfileResponse;
import nextstep.api.auth.application.userdetails.UserDetails;
import nextstep.api.auth.application.userdetails.UserDetailsService;
import nextstep.api.auth.support.JwtTokenProvider;

@Service
public class TokenService {
    private UserDetailsService userDetailsService;
    private OAuth2UserService oAuth2UserService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(
            UserDetailsService userDetailsService,
            OAuth2UserService oAuth2UserService,
            JwtTokenProvider jwtTokenProvider,
            GithubClient githubClient
    ) {
        this.userDetailsService = userDetailsService;
        this.oAuth2UserService = oAuth2UserService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException();
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
