package nextstep.auth.token.service;

import lombok.RequiredArgsConstructor;
import nextstep.global.error.code.ErrorCode;
import nextstep.global.error.exception.AuthenticationException;
import nextstep.auth.token.dto.TokenResponse;
import nextstep.auth.oauth2.dto.OAuth2User;
import nextstep.auth.oauth2.service.OAuth2UserService;
import nextstep.auth.oauth2.github.client.GithubClient;
import nextstep.auth.oauth2.github.dto.GithubProfileResponse;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {

    private final UserDetailsService userDetailsService;

    private final OAuth2UserService oAuth2UserService;

    private final JwtTokenProvider jwtTokenProvider;

    private final GithubClient githubClient;

    public TokenResponse createToken(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!userDetails.checkPassword(password)) {
            throw new AuthenticationException(ErrorCode.LOGIN_ERROR);
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
