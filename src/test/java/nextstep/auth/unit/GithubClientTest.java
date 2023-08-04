package nextstep.auth.unit;


import nextstep.auth.AuthenticationException;
import nextstep.auth.ForbiddenException;
import nextstep.auth.token.oauth2.github.GithubClient;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.auth.util.VirtualUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

    @Autowired
    GithubClient githubClient;

    VirtualUser validUser = VirtualUser.사용자1;
    VirtualUser expiredUser = VirtualUser.만료된사용자;

     // Ref. https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github
    @DisplayName("토큰 요청 API 호출 성공")
    @Test
    void getAccessTokenFromGithubSuccess() {
        // when
        String token = githubClient.getAccessTokenFromGithub(validUser.getCode());

        // then
        assertThat(token).isNotBlank();
    }

    // Ref. https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github
    @DisplayName("토큰 요청 API 호출 실패, 깃허브가 인식할 수 없는 코드 전송")
    @Test
    void getAccessTokenFromGithubFailedByInvalidToken() {
        // when
        assertThatThrownBy(() -> githubClient.getAccessTokenFromGithub("wrong_token"))
                .isInstanceOf(AuthenticationException.class);
    }

    // Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
    @DisplayName("리소스 조회 API 호출 성공")
    @Test
    void getGithubProfileFromGithubSuccess() {
        // when
        GithubProfileResponse profileResponse = githubClient.getGithubProfileFromGithub(validUser.getToken());

        // then
        assertThat(profileResponse).isNotNull();
        assertThat(profileResponse.getEmail()).isNotBlank();
    }

    // Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
    @DisplayName("리소스 조회 API 호출 실패, 깃허브가 인식할 수 없는 토큰 전송")
    @Test
    void getGithubProfileFromGithubFailedByInvalidToken() {
        assertThatThrownBy(() -> githubClient.getGithubProfileFromGithub("wrong_token"))
                .isInstanceOf(AuthenticationException.class);
    }

    //  Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
    @DisplayName("리소스 조회 API 호출 실패, 만료기한 지난 토큰 전송")
    @Test
    void getGithubProfileFromGithubFailedByExpiredToken() {
        assertThatThrownBy(() -> githubClient.getGithubProfileFromGithub(expiredUser.getToken()))
                .isInstanceOf(ForbiddenException.class);
    }
}
