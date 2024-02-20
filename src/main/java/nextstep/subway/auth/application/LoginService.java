package nextstep.subway.auth.application;

import nextstep.subway.auth.AuthenticationException;
import nextstep.subway.auth.application.dto.TokenResponse;
import nextstep.subway.auth.application.provider.TokenType;
import nextstep.subway.auth.domain.UserDetail;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final UserDetailsService userDetailsService;
    private final AuthManager authManager;

    public LoginService(UserDetailsService userDetailsService,
                        AuthManager authManager) {
        this.userDetailsService = userDetailsService;
        this.authManager = authManager;
    }

    public TokenResponse createGithubToken(String email,
                                           String password) {
        UserDetail userDetail = userDetailsService.findMemberByEmail(email);
        if (!userDetail.correctPassword(password)) {
            throw new AuthenticationException();
        }

        String token = authManager.createToken(userDetail.getEmail(), TokenType.JWT);

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(String code) {
        String accessToken = authManager.createToken(code, TokenType.GITHUB);

        String email = authManager.getPrincipal(accessToken, TokenType.GITHUB);
        userDetailsService.findMemberByEmailNotExistSave(email);

        return new TokenResponse(accessToken);
    }
}
