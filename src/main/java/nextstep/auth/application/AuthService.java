package nextstep.auth.application;

import nextstep.auth.application.dto.AuthResponse;
import nextstep.auth.application.dto.OAuth2Response;
import nextstep.auth.application.github.GithubOAuth2Client;
import nextstep.common.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubOAuth2Client githubOAuth2Client;

    public AuthService(final UserDetailsService userDetailsService, final JwtTokenProvider jwtTokenProvider, final GithubOAuth2Client githubOAuth2Client) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubOAuth2Client = githubOAuth2Client;
    }

    public AuthResponse login(final String email, final String password) {
        final UserDetail userDetail = userDetailsService.loadUserByEmail(email);

        if (Objects.isNull(userDetail) || userDetail.isPasswordMismatch(password)) {
            throw new UnauthorizedException("아이디와 비밀번호를 확인해주세요.");
        }

        final String token = jwtTokenProvider.createToken(userDetail.getId(), userDetail.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse loginGithub(final String code) {
        final String githubToken = githubOAuth2Client.requestGithubToken(code);
        final OAuth2Response oAuth2Response = githubOAuth2Client.requestGithubProfile(githubToken);
        final UserDetail userDetail = userDetailsService.loadOrCreateUser(oAuth2Response);

        final String token = jwtTokenProvider.createToken(userDetail.getId(), userDetail.getEmail());

        return new AuthResponse(token);
    }
}
