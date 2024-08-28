package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenGenerator tokenGenerator;
    private final TokenClient githubClient;

    public TokenResponse createToken(String email, String password) {
        return tokenGenerator.generateToken(email, password);
    }
    public TokenResponse creteGithubAccessToken(String code) {
        String token = githubClient.requestAccessToken(code);
        return new TokenResponse(token);
    }
}
