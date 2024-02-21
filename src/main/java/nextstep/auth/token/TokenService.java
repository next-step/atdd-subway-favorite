package nextstep.auth.token;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.auth.oauth2.OAuth2UserService;
import nextstep.auth.oauth2.github.GithubClient;
import nextstep.auth.oauth2.github.GithubProfileResponse;
import nextstep.auth.oauth2.OAuth2User;
import nextstep.auth.oauth2.OAuth2UserRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.principal.LoginMember;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;
    private final OAuth2UserService oAuth2UserService;

    public TokenResponse createToken(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetails.getId(), userDetails.getUsername());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenFromGithub(String code) {
        String githubToken = githubClient.requestGithubToken(code);
        GithubProfileResponse githubProfile = githubClient.requestGithubProfile(githubToken);

        OAuth2User oAuth2User = oAuth2UserService.loadUserByOAuth2User(githubProfile);

        String token = jwtTokenProvider.createToken(oAuth2User.getId(), oAuth2User.getUsername());

        return new TokenResponse(token);
    }



}
