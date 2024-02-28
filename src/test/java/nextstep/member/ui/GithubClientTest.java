package nextstep.member.ui;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.GithubClient;
import nextstep.auth.GithubResponses;
import nextstep.auth.dto.GithubProfileResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GithubClientTest {


    @Autowired
    private GithubClient githubClient;

    @Test
    @DisplayName("Github 서버에서 인증된 유저는 토큰을 얻는다")
    public void shouldSuccessLogin() {
        String token = githubClient.getAccessTokenFromGithub(GithubResponses.USER_C.getCode());
        assertThat(token).isEqualTo(GithubResponses.USER_C.getToken());
    }

    @Test
    @DisplayName("Github 서버에서 인증되지 않은 유저의 토큰 발급 요청은 예외가 발생한다")
    public void shouldFailUnauthorizedUserRequest() {
        Assertions.assertThrows(AuthenticationException.class,
                () -> githubClient.getAccessTokenFromGithub(GithubResponses.UNAUTHORIZED_USER.getCode())
        );
    }

    @Test
    @DisplayName("인증된 토큰은 Github 서버로 회원 정보 요청이 성공한다")
    public void shouldSuccessGetGithubProfile() {
        GithubProfileResponse userProfile = githubClient.getUserProfile(GithubResponses.USER_A.getToken());
        assertThat(userProfile.getEmail()).isEqualTo(GithubResponses.USER_A.getEmail());
    }


}