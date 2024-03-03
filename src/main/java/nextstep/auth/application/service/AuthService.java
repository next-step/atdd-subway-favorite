package nextstep.auth.application.service;

import nextstep.auth.application.dto.AuthResponse;
import nextstep.exception.AuthenticationException;
import nextstep.auth.application.GithubClient;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserDetailService userDetailService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public AuthService(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public AuthResponse createToken(String email, String password) {
        if (userDetailService.isNotMember(email, password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(email);

        return new AuthResponse(token);
    }

    public AuthResponse createGithubToken(String code) {
        String githubToken = githubClient.requestGithubToken(code);
        String email = githubClient.requestGithubProfile(githubToken).getEmail();

        userDetailService.createMemberIfNotExist(email, "", 0);

        return new AuthResponse(jwtTokenProvider.createToken(email));
    }
}
