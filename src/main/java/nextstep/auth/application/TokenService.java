package nextstep.auth.application;

import nextstep.auth.domain.UserDetail;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TokenService {

    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenService(UserDetailService userDetailService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.userDetailService = userDetailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        UserDetail user = userDetailService.getUser(email);
        if (!user.checkPassword(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(user.getEmail());

        return new TokenResponse(token);
    }

    @Transactional
    public TokenResponse createTokenWithGithubUser(String code) {
        // code로 github accessToken 발행
        GithubAccessTokenResponse accessTokenResponse = githubClient.generateAccessToken(code);

        // accessToken을 이용한 github profile 조회
        GithubProfileResponse githubProfile = githubClient.getGithubProfile(accessTokenResponse.getAccessToken());

        // 존재하는 회원이 아니면 회원가입
        UserDetail user = userDetailService.getOrCreateUser(githubProfile.getEmail(), code, githubProfile.getAge());

        // 토큰발생
        return new TokenResponse(
            jwtTokenProvider.createToken(user.getEmail())
        );
    }
}
