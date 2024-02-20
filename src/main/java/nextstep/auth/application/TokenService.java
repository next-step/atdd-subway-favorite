package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubEmailResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.jwt.JwtTokenProvider;
import nextstep.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse createToken(String email, String password) {
        UserDetails userDetails = userDetailsService.findMemberByEmail(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = createToken(userDetails);

        return new TokenResponse(token);
    }


    public TokenResponse createTokenGithub(String code) {
        GithubAccessTokenResponse tokenResponse = githubClient.requestAccessToken(code);
        GithubEmailResponse emailResponse = githubClient.requestGithubEmail(tokenResponse.getAccessToken());

        UserDetails userDetails = userDetailsService.findOrCreate(emailResponse.getEmail());

        String token = createToken(userDetails);

        return new TokenResponse(token);
    }

    private String createToken(UserDetails userDetails) {
        return jwtTokenProvider.createToken(userDetails.getEmail());
    }
}
