package nextstep.auth.application.service;

import nextstep.auth.application.dto.AuthResponse;
import nextstep.auth.domain.UserDetail;
import nextstep.exception.AuthenticationException;
import nextstep.auth.application.GithubClient;
import nextstep.auth.application.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public AuthService(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public AuthResponse createToken(String email, String password) {
        UserDetail userDetail = userDetailService.getUserDetailByEmail(email);

        if(!password.equals(userDetail.getPassword())) {
            throw new AuthenticationException("아이디와 비밀번호가 맞지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(userDetail.getId(), userDetail.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse createGithubToken(String code) {
        String githubToken = githubClient.requestGithubToken(code);
        String email = githubClient.requestGithubProfile(githubToken).getEmail();

        UserDetail userDetail = userDetailService.createUserIfNotExist(new UserDetail(email, "", 0));

        return new AuthResponse(jwtTokenProvider.createToken(userDetail.getId(), userDetail.getEmail()));
    }
}
